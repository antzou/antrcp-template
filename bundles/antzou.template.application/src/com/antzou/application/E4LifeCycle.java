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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class E4LifeCycle {

    @PostContextCreate
    void postContextCreate(IEclipseContext context) {
        // 可以在这里设置默认主题
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
                }
            });
        }
    }

    @ProcessRemovals
    void processRemovals(IEclipseContext workbenchContext) {
    }
}