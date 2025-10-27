package com.antzou.application.parts;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
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
    @Inject private MWindow window;  // 直接注入当前窗口
    
    @PostConstruct
    public void createControls(Composite parent) {
        parent.setLayout(new FillLayout());
        
        TreeViewer treeViewer = new TreeViewer(parent, SWT.BORDER | SWT.SINGLE);
        treeViewer.setContentProvider(new NavContentProvider());
        treeViewer.setLabelProvider(new NavLabelProvider());
        treeViewer.setInput(new Object()); // 触发内容提供器
        
        treeViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                IStructuredSelection selection = (IStructuredSelection)event.getSelection();
                if (selection.isEmpty()) return;
                
                NavItem item = (NavItem)selection.getFirstElement();
                switchToPart(item.getPartId());
            }
        });
    }
    
    private void switchToPart(String partId) {
        sync.asyncExec(() -> {
            MPart part = partService.findPart(partId);
            if (part == null) {
                // 如果部件不存在则创建
                part = partService.createPart(partId);
                MPartStack stack = (MPartStack)modelService.find(
                    "com.example.e4.rcp.right.editorstack", window);
                if (stack != null) {
                    stack.getChildren().add(part);
                }
            }
            partService.activate(part);
        });
    }
}