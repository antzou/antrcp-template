package com.antzou.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

public class MyAboutHandler {
	@Execute
	public void execute(Shell shell) {
		CustomAboutDialog dialog = new CustomAboutDialog(shell);
		dialog.open();
	}
}
