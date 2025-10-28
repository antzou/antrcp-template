package com.antzou.application.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import jakarta.annotation.PostConstruct;

public class StatusBarFiller {
    
    @PostConstruct
    public void createControl(Composite parent) {
    	parent.setLayout(new GridLayout(2, false));
    	
//    	Text text = new Text(parent, SWT.BORDER);
//    	text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        // 创建一个空的标签作为填充，占据所有剩余空间
        Label filler = new Label(parent, SWT.NONE);
        filler.setText(""); // 空文本
        GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        filler.setLayoutData(gridData);
    }
}