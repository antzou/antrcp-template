package com.antzou.application.handlers;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.antzou.application.Activator;

public class GeneralPreferencePage extends FieldEditorPreferencePage 
    implements IPreferencePage {
    
    public GeneralPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors() {
        Composite parent = getFieldEditorParent();
        
        // 1. 显示工具栏选项
        addField(new BooleanFieldEditor(
            "SHOW_TOOLBAR", 
            "&Show Toolbar", 
            parent
        ));
        
        // 2. 自动保存间隔
        addField(new IntegerFieldEditor(
            "AUTO_SAVE_INTERVAL", 
            "Auto-save &interval (minutes):", 
            parent
        ));
        
        // 添加分隔线
        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
        
        // 3. 主题选择
        String[][] themes = {
            {"Default", "default"},
            {"Dark", "dark"},
            {"Light", "light"}
        };
        addField(new ComboFieldEditor(
            "THEME", 
            "Application &Theme:", 
            themes, 
            parent
        ));
    }
    
    @Override
    protected void performDefaults() {
        super.performDefaults();
        // 可以添加额外的默认值处理逻辑
    }
    
    @Override
    public boolean performOk() {
        boolean result = super.performOk();
        if (result) {
            // 保存成功后可以执行额外操作
            // 例如：刷新UI或通知其他部分
        }
        return result;
    }
}