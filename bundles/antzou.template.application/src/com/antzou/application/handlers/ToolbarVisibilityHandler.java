package com.antzou.application.handlers;

import java.util.Collections;
import java.util.List;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Shell;

import jakarta.inject.Named;

public class ToolbarVisibilityHandler {

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, MApplication application,
			EModelService modelService, @Named(IServiceConstants.ACTIVE_PART) MPart part) {

		MWindow window = (MWindow) modelService.find("antzou.template.application.window.main", application);
		if (window == null) {
			System.err.println("Main window not found!");
			return;
		}

		// 1. 查找工具栏
		MUIElement toolbar = modelService.find("antzou.template.application.trimbar.top", window);
		if (toolbar == null) {
			System.err.println("Toolbar not found!");
			return;
		}

		boolean isVisible = !toolbar.isVisible();
		toolbar.setVisible(isVisible);

		// 另一种查找菜单项的方法
		List<MHandledMenuItem> menuItems = modelService.findElements(application, "window.appearance.hidetoolbar",
				MHandledMenuItem.class, Collections.emptyList(), EModelService.IN_MAIN_MENU);

		MHandledMenuItem menuItem = null;
		if (!menuItems.isEmpty()) {
			menuItem = menuItems.get(0);
			// 更新菜单项文本...
		}

		if (menuItem != null) {
			String currentLabel = menuItem.getLabel();
			String newLabel = currentLabel.equals("Hide Toolbar") ? "Show Toolbar" : "Hide Toolbar";
			menuItem.setLabel(newLabel);
		} else {
			System.err.println("Menu item not found!");
		}

		// 3. 刷新UI
		if (shell != null && !shell.isDisposed()) {
			shell.layout(true, true);
		}
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}
}