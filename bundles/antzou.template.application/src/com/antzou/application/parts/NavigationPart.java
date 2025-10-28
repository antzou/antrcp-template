package com.antzou.application.parts;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;

public class NavigationPart {
    
    public static class NavItem {
        private String name;
        private String partId;
        
        public NavItem(String name, String partId) {
            this.name = name;
            this.partId = partId;
        }
        
        public String getName() { return name; }
        public String getPartId() { return partId; }
        
        @Override
        public String toString() { return name; }
    }
    
    public static class NavContentProvider implements ITreeContentProvider {
        @Override
        public Object[] getElements(Object inputElement) {
            return new NavItem[] {
                new NavItem("信息录入", "com.antzou.application.parts.input"),
                new NavItem("信息查询", "com.antzou.application.parts.query"),
                new NavItem("信息修改", "com.antzou.application.parts.edit")
            };
        }
        
        @Override public Object[] getChildren(Object parentElement) { return null; }
        @Override public Object getParent(Object element) { return null; }
        @Override public boolean hasChildren(Object element) { return false; }
    }
    
    public static class NavLabelProvider extends LabelProvider {
        @Override
        public String getText(Object element) {
            return ((NavItem)element).getName();
        }
    }
    
    @Inject private EPartService partService;
    @Inject private EModelService modelService;
    @Inject private UISynchronize sync;
    @Inject private MWindow window;
    @Inject private IEventBroker eventBroker;
    
    @PostConstruct
    public void createControls(Composite parent) {
        parent.setLayout(new FillLayout());
        
        TreeViewer treeViewer = new TreeViewer(parent, SWT.BORDER | SWT.SINGLE);
        treeViewer.setContentProvider(new NavContentProvider());
        treeViewer.setLabelProvider(new NavLabelProvider());
        treeViewer.setInput(new Object());
        
        // 添加独立的点击监听器 - 用于状态栏同步
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection)event.getSelection();
                if (selection.isEmpty()) return;
                
                NavItem item = (NavItem)selection.getFirstElement();
                // 立即同步到状态栏
                updateStatusBar(item.getName());
            }
        });
        
        // 原有的双击监听器保持不变 - 用于打开部件
        treeViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                IStructuredSelection selection = (IStructuredSelection)event.getSelection();
                if (selection.isEmpty()) return;
                
                NavItem item = (NavItem)selection.getFirstElement();
                switchToPart(item.getPartId(), item.getName());
            }
        });
    }
    
    /**
     * 独立的点击事件处理 - 只更新状态栏
     */
    private void updateStatusBar(String itemName) {
        if (eventBroker != null) {
            eventBroker.post("NAVIGATION/STATUS", "选中: " + itemName);
        }
    }
    
    /**
     * 双击事件处理 - 打开对应的部件
     */
    private void switchToPart(String partId, String partName) {
        // 更新状态栏显示当前操作
        if (eventBroker != null) {
            eventBroker.post("NAVIGATION/STATUS", "正在打开: " + partName);
        }
        
        sync.asyncExec(() -> {
            MPart part = partService.findPart(partId);
            if (part == null) {
                part = partService.createPart(partId);
                MPartStack stack = (MPartStack)modelService.find(
                    "antzou.template.application.right.editorstack", window);
                if (stack != null) {
                    stack.getChildren().add(part);
                }
            }
            partService.activate(part);
            
            // 激活后再次更新状态栏
            if (eventBroker != null) {
                eventBroker.post("NAVIGATION/STATUS", "当前: " + partName);
            }
        });
    }
}