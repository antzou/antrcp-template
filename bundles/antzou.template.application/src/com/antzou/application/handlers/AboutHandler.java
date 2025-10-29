package com.antzou.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import com.antzou.application.dialogs.AboutDialog;

public class AboutHandler {
    @Execute
    public void execute(Shell shell) {
    	new AboutDialog(shell).open();
    }
}