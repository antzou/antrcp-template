package com.antzou.application.parts;

import jakarta.annotation.PostConstruct;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class QueryPart {
    
    @PostConstruct
    public void createControls(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        
        Composite searchPanel = new Composite(parent, SWT.NONE);
        searchPanel.setLayout(new GridLayout(3, false));
        searchPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        new Label(searchPanel, SWT.NONE).setText("搜索:");
        Text searchText = new Text(searchPanel, SWT.BORDER);
        searchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        Button searchBtn = new Button(searchPanel, SWT.PUSH);
        searchBtn.setText("查询");
        searchBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        Table table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
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
        addSampleData(table, "张三", "25", "13800138000");
        addSampleData(table, "李四", "30", "13900139000");
        addSampleData(table, "王五", "28", "13700137000");
        
        searchBtn.addListener(SWT.Selection, e -> {
            table.removeAll();
            String keyword = searchText.getText().trim();
            if (keyword.isEmpty()) {
                addSampleData(table, "张三", "25", "13800138000");
                addSampleData(table, "李四", "30", "13900139000");
                addSampleData(table, "王五", "28", "13700137000");
            } else {
                // 模拟搜索
                if (keyword.contains("张")) {
                    addSampleData(table, "张三", "25", "13800138000");
                }
                if (keyword.contains("李")) {
                    addSampleData(table, "李四", "30", "13900139000");
                }
                if (keyword.contains("王")) {
                    addSampleData(table, "王五", "28", "13700137000");
                }
            }
        });
    }
    
    private void addSampleData(Table table, String name, String age, String phone) {
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(new String[] {name, age, phone});
    }
}