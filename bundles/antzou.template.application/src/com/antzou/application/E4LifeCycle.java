package com.antzou.application;

import java.util.List;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.antzou.application.common.IconLoader;
import com.antzou.application.theme.ThemeManager;
import com.antzou.application.tray.TrayManager;

public class E4LifeCycle {

    @PostContextCreate
    void postContextCreate(IEclipseContext context) {
        // 手动创建并注册 ThemeManager
        ThemeManager themeManager = ContextInjectionFactory.make(ThemeManager.class, context);
        context.set(ThemeManager.class, themeManager);
        
        // 创建并注册 TrayManager
        TrayManager trayManager = ContextInjectionFactory.make(TrayManager.class, context);
        context.set(TrayManager.class, trayManager);
    }
    
    @ProcessAdditions
    void processAdditions(IEclipseContext workbenchContext) {
        // 原有的窗口最大化逻辑
        setupWindowMaximization(workbenchContext);
        
        // 初始化系统托盘
        setupSystemTray(workbenchContext);
    }

    @PreSave
    void preSave(IEclipseContext workbenchContext) {
        // 清理托盘资源
        TrayManager trayManager = workbenchContext.get(TrayManager.class);
        if (trayManager != null) {
            trayManager.disposeTray();
        }
        
        IconLoader.disposeInstance();
    }

    @ProcessRemovals
    void processRemovals(IEclipseContext workbenchContext) {
    }
    
    /**
     * 设置窗口最大化
     */
    private void setupWindowMaximization(IEclipseContext workbenchContext) {
        EModelService modelService = workbenchContext.get(EModelService.class);
        MApplication application = workbenchContext.get(MApplication.class);

        List<MWindow> windows = modelService.findElements(application, null, MWindow.class, null);

        if (!windows.isEmpty()) {
            MWindow mainWindow = windows.get(0);

            Display.getDefault().asyncExec(() -> {
                Shell shell = (Shell) mainWindow.getWidget();
                if (shell != null && !shell.isDisposed()) {
                    shell.setMaximized(true);
                }
            });
        }
    }
    
    /**
     * 设置系统托盘
     */
    private void setupSystemTray(IEclipseContext workbenchContext) {
        EModelService modelService = workbenchContext.get(EModelService.class);
        MApplication application = workbenchContext.get(MApplication.class);
        TrayManager trayManager = workbenchContext.get(TrayManager.class);

        List<MWindow> windows = modelService.findElements(application, null, MWindow.class, null);

        if (!windows.isEmpty() && trayManager != null) {
            MWindow mainWindow = windows.get(0);

            Display.getDefault().asyncExec(() -> {
                Shell shell = (Shell) mainWindow.getWidget();
                if (shell != null && !shell.isDisposed()) {
                    // 创建系统托盘
                    trayManager.createTray(shell);
                    
                    // 监听窗口最小化事件
                    shell.addListener(SWT.Iconify, event -> {
                        if (shell.getMinimized()) {
                            shell.setVisible(false);
                        }
                    });
                }
            });
        }
    }
}