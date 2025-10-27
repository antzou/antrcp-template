package com.antzou.application.parts;

import jakarta.annotation.PostConstruct;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class DefaultEditorPart {
    
    @PostConstruct
    public void createControls(Composite parent) {
        // 设置基础编辑器内容
        Label label = new Label(parent, SWT.NONE);
        label.setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, true)); // 关键：填充可用空间
        label.setText("Welcome to Eclipse RCP Editor");
        
        // 可扩展区域：添加文本框、按钮等控件
        // Text text = new Text(parent, SWT.BORDER);
    }
}