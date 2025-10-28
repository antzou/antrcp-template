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
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import jakarta.annotation.PreDestroy;

public class E4LifeCycle {

    private ResourceManager resourceManager;
    private Image appImage32;
    private boolean initialized = false;

    @PostContextCreate
    void postContextCreate(IEclipseContext context) {
        // 初始化资源管理器
        Display.getDefault().asyncExec(() -> {
            resourceManager = new LocalResourceManager(JFaceResources.getResources());
            loadApplicationIcon();
            setupGlobalShellIcons();
            initialized = true;
        });
    }

    @PreSave
    void preSave(IEclipseContext workbenchContext) {
    }

    @ProcessAdditions
    void processAdditions(IEclipseContext workbenchContext) {
        if (!initialized) return;
        
        EModelService modelService = workbenchContext.get(EModelService.class);
        MApplication application = workbenchContext.get(MApplication.class);

        List<MWindow> windows = modelService.findElements(application, null, MWindow.class, null);

        if (!windows.isEmpty()) {
            MWindow mainWindow = windows.get(0);

            Display.getDefault().asyncExec(() -> {
                Shell shell = (Shell) mainWindow.getWidget();
                if (shell != null && !shell.isDisposed()) {
                    shell.setMaximized(true);
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
        if (resourceManager != null) {
            resourceManager.dispose();
        }
        initialized = false;
    }
    
    /**
     * 使用 ResourceManager 安全加载图标
     */
    @SuppressWarnings("deprecation")
	private void loadApplicationIcon() {
        if (resourceManager == null) return;
        
        try {
            ImageDescriptor descriptor = ImageDescriptor.createFromFile(
                getClass(), "/icons/app32.png");
            
            if (descriptor != null) {
                appImage32 = resourceManager.createImage(descriptor);
                System.out.println("应用图标加载成功");
            } else {
                System.err.println("无法创建图像描述符");
            }
        } catch (Exception e) {
            System.err.println("加载应用图标失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 设置全局Shell图标监听
     */
    private void setupGlobalShellIcons() {
        if (appImage32 == null) return;
        
        Display display = Display.getDefault();
        if (display == null || display.isDisposed()) {
            return;
        }
        
        // 为当前已存在的Shell设置图标
        display.asyncExec(() -> {
            Shell[] existingShells = display.getShells();
            for (Shell shell : existingShells) {
                if (!shell.isDisposed()) {
                    setShellIcon(shell);
                }
            }
        });
        
        // 监听新创建的Shell
        display.addListener(SWT.Show, event -> {
            if (event.widget instanceof Shell) {
                Shell shell = (Shell) event.widget;
                display.asyncExec(() -> setShellIcon(shell));
            }
        });
        
        System.out.println("全局Shell图标监听器已设置");
    }
    
    /**
     * 为Shell设置应用图标
     */
    private void setShellIcon(Shell shell) {
        if (shell == null || shell.isDisposed() || appImage32 == null || appImage32.isDisposed()) {
            return;
        }
        
        try {
            shell.setImage(appImage32);
        } catch (Exception e) {
            System.err.println("设置Shell图标失败: " + e.getMessage());
        }
    }
}