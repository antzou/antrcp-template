package com.antzou.application.parts;

import jakarta.annotation.PostConstruct;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class InputPart {
    
    @PostConstruct
    public void createControls(Composite parent) {
        parent.setLayout(new GridLayout(2, false));
        
        Label label = new Label(parent, SWT.NONE);
        label.setText("姓名:");
        Text nameText = new Text(parent, SWT.BORDER);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label label_1 = new Label(parent, SWT.NONE);
        label_1.setText("年龄:");
        Text ageText = new Text(parent, SWT.BORDER);
        ageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label label_2 = new Label(parent, SWT.NONE);
        label_2.setText("电话:");
        Text phoneText = new Text(parent, SWT.BORDER);
        phoneText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 2, 1));
        
        Button saveBtn = new Button(composite, SWT.PUSH);
        saveBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        saveBtn.setText("保存");
        
        Button resetBtn = new Button(composite, SWT.NONE);
        resetBtn.setText("重置");
        
        saveBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println("保存信息: " + nameText.getText() + ", " + 
                    ageText.getText() + ", " + phoneText.getText());
            }
        });
    }
}