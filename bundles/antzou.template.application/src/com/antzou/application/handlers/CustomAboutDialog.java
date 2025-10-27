package com.antzou.application.handlers;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class CustomAboutDialog extends Dialog {
    public CustomAboutDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(1, false));

        Label lblTitle = new Label(container, SWT.NONE);
        lblTitle.setText("我的 RCP 应用");
        lblTitle.setFont(parent.getFont());

        Label lblVersion = new Label(container, SWT.NONE);
        lblVersion.setText("版本: 1.0.0");

        Label lblAuthor = new Label(container, SWT.NONE);
        lblAuthor.setText("作者: Your Name");

        return container;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("关于");
    }
}