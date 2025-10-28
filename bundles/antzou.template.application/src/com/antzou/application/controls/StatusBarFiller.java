package com.antzou.application.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import jakarta.annotation.PostConstruct;

public class StatusBarFiller {
    
    @PostConstruct
    public void createControl(Composite parent) {
        Label filler = new Label(parent, SWT.NONE);
        filler.setText(""); // 空文本
        GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        filler.setLayoutData(gridData);
    }
}