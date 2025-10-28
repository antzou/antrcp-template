package com.antzou.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.dialogs.AboutDialog;

@SuppressWarnings("restriction")
public class AboutHandler {
    
    @Execute
    public void execute(Shell shell) {
        // 使用 E3 的标准 About 对话框
        AboutDialog aboutDialog = new AboutDialog(shell);
        aboutDialog.open();
    }
}