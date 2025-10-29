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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.antzou.application.theme.ThemeManager;

public class E4LifeCycle {

    @PostContextCreate
    void postContextCreate(IEclipseContext context) {
        // 手动创建并注册 ThemeManager
        ThemeManager themeManager = ContextInjectionFactory.make(ThemeManager.class, context);
        context.set(ThemeManager.class, themeManager);
        System.out.println("ThemeManager 已手动创建并注册: " + (themeManager != null));
    }
    
    @ProcessAdditions
    void processAdditions(IEclipseContext workbenchContext) {
        // 原有的窗口最大化逻辑
        setupWindowMaximization(workbenchContext);
    }

    @PreSave
    void preSave(IEclipseContext workbenchContext) {
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
}