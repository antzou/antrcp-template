package com.antzou.application;

import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import jakarta.annotation.PreDestroy;

/**
 * This is a stub implementation containing e4 LifeCycle annotated
 * methods.<br />
 * There is a corresponding entry in <em>plugin.xml</em> (under the
 * <em>org.eclipse.core.runtime.products' extension point</em>) that references
 * this class.
 **/
public class E4LifeCycle {

    private Image appImage32; // 只使用32像素图标
    private boolean iconLoaded = false;

    @PostContextCreate
    void postContextCreate(IEclipseContext context) {
        // 加载32像素应用图标
        loadApplicationIcon();
        
        // 设置全局Shell图标监听
        setupGlobalShellIcons();
    }

    @PreSave
    void preSave(IEclipseContext workbenchContext) {
    }

    @ProcessAdditions
    void processAdditions(IEclipseContext workbenchContext) {
        EModelService modelService = workbenchContext.get(EModelService.class);
        MApplication application = workbenchContext.get(MApplication.class);

        List<MWindow> windows = modelService.findElements(application, null, MWindow.class, null);

        if (!windows.isEmpty()) {
            MWindow mainWindow = windows.get(0);

            Display.getDefault().asyncExec(() -> {
                Shell shell = (Shell) mainWindow.getWidget();
                if (shell != null && !shell.isDisposed()) {
                    shell.setMaximized(true);
                    // 确保主窗口设置了图标
                    setShellIcon(shell);
                }
            });
        }
    }

    @ProcessRemovals
    void processRemovals(IEclipseContext workbenchContext) {
    }
    
    @PreDestroy
    public void dispose() {
        // 清理图标资源
        disposeIcon();
    }
    
    /**
     * 加载32像素应用程序图标
     */
    private void loadApplicationIcon() {
        if (iconLoaded) return;
        
        Display display = Display.getDefault();
        try {
            appImage32 = new Image(display, getClass().getResourceAsStream("/icons/app32.png"));
            iconLoaded = true;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 设置全局Shell图标监听
     */
    private void setupGlobalShellIcons() {
        if (!iconLoaded) return;
        
        Display display = Display.getDefault();
        
        // 监听所有新创建的Shell并设置图标
        display.addFilter(SWT.Show, event -> {
            if (event.widget instanceof Shell) {
                Shell shell = (Shell) event.widget;
                // 延迟设置，确保Shell完全创建
                display.asyncExec(() -> setShellIcon(shell));
            }
        });
        
        // 同时为当前已存在的Shell设置图标（如果有的话）
        display.asyncExec(() -> {
            Shell[] existingShells = display.getShells();
            for (Shell shell : existingShells) {
                if (!shell.isDisposed()) {
                    setShellIcon(shell);
                }
            }
        });
        
        System.out.println("全局Shell图标监听器已设置");
    }
    
    /**
     * 为Shell设置应用图标
     */
    private void setShellIcon(Shell shell) {
        if (shell == null || shell.isDisposed() || !iconLoaded || appImage32 == null) {
            return;
        }
        
        try {
            // 使用setImage方法设置单个图标
            shell.setImage(appImage32);
        } catch (Exception e) {
            System.err.println("设置Shell图标失败: " + e.getMessage());
        }
    }
    
    /**
     * 清理图标资源
     */
    private void disposeIcon() {
        if (appImage32 != null && !appImage32.isDisposed()) {
            appImage32.dispose();
            appImage32 = null;
        }
        iconLoaded = false;
    }
}