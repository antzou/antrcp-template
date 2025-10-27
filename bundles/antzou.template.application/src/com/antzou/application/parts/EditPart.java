package com.antzou.application.parts;

import jakarta.annotation.PostConstruct;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class EditPart {
    
    private Text nameText, ageText, phoneText;
    private Table table;
    
    @PostConstruct
    public void createControls(Composite parent) {
        parent.setLayout(new GridLayout(2, true));
        
        // 左侧表单
        Composite formPanel = new Composite(parent, SWT.BORDER);
        formPanel.setLayout(new GridLayout(2, false));
        formPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
        
        Label label = new Label(formPanel, SWT.NONE);
        label.setText("姓名:");
        nameText = new Text(formPanel, SWT.BORDER);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label label_1 = new Label(formPanel, SWT.NONE);
        label_1.setText("年龄:");
        ageText = new Text(formPanel, SWT.BORDER);
        ageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label label_2 = new Label(formPanel, SWT.NONE);
        label_2.setText("电话:");
        phoneText = new Text(formPanel, SWT.BORDER);
        phoneText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Composite composite = new Composite(formPanel, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 2, 1));
        
        Button updateBtn = new Button(composite, SWT.PUSH);
        updateBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        updateBtn.setText("更新");
        
        Button resetBtn = new Button(composite, SWT.NONE);
        resetBtn.setText("重置");
        
        // 更新按钮事件
        updateBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem[] selection = table.getSelection();
                if (selection.length > 0) {
                    selection[0].setText(0, nameText.getText());
                    selection[0].setText(1, ageText.getText());
                    selection[0].setText(2, phoneText.getText());
                }
            }
        });
        
        // 右侧表格
        table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        TableColumn nameCol = new TableColumn(table, SWT.LEFT);
        nameCol.setText("姓名");
        nameCol.setWidth(100);
        
        TableColumn ageCol = new TableColumn(table, SWT.CENTER);
        ageCol.setText("年龄");
        ageCol.setWidth(60);
        
        TableColumn phoneCol = new TableColumn(table, SWT.RIGHT);
        phoneCol.setText("电话");
        phoneCol.setWidth(150);
        
        // 添加示例数据
        addSampleData("张三", "25", "13800138000");
        addSampleData("李四", "30", "13900139000");
        addSampleData("王五", "28", "13700137000");
        
        // 表格选择事件
        table.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem[] selection = table.getSelection();
                if (selection.length > 0) {
                    nameText.setText(selection[0].getText(0));
                    ageText.setText(selection[0].getText(1));
                    phoneText.setText(selection[0].getText(2));
                }
            }
        });
    }
    
    private void addSampleData(String name, String age, String phone) {
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(new String[] {name, age, phone});
    }
}