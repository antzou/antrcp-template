package com.antzou.application.handlers;
import jakarta.inject.Named;

import java.util.Collections;
import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

public class FullScreenHandler {
    
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
                       MApplication application,
                       EModelService modelService) {
        
        MWindow window = (MWindow) modelService.find("antzou.template.application.window.main", application);
        if (window == null) return;
        
        Object widget = window.getWidget();
        if (widget instanceof Shell) {
            Shell swtShell = (Shell) widget;
            swtShell.setFullScreen(!swtShell.getFullScreen());
            
            // 另一种查找菜单项的方法
    		List<MHandledMenuItem> menuItems = modelService.findElements(application, "window.appearance.fullscreen",
    				MHandledMenuItem.class, Collections.emptyList(), EModelService.IN_MAIN_MENU);

    		MHandledMenuItem menuItem = null;
    		if (!menuItems.isEmpty()) {
    			menuItem = menuItems.get(0);
    			// 更新菜单项文本...
    		}

    		if (menuItem != null) {
    			String currentLabel = menuItem.getLabel();
    			String newLabel = currentLabel.equals("Toggle Full Screen") ? "Exit Full Screen" : "Toggle Full Screen";
    			menuItem.setLabel(newLabel);
    		} else {
    			System.err.println("Menu item not found!");
    		}
        }
    }
}