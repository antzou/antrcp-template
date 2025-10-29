package com.antzou.application.handlers;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import com.antzou.application.dialogs.PreferenceDialog;
import com.antzou.application.theme.ThemeManager;

import jakarta.inject.Inject;

public class PreferenceHandler {
    @Inject
    private IEclipseContext context;
    
    @Execute
    public void execute(Shell shell) {
    	
    	System.out.println("打开首选项对话框");
        
        // 从上下文中获取 ThemeManager
        ThemeManager themeManager = context.get(ThemeManager.class);
        System.out.println("Handler 中的 ThemeManager: " + themeManager);
        
        if (themeManager == null) {
            System.err.println("ThemeManager 为 null，无法打开首选项");
            return;
        }
        
    	
    	PreferenceDialog dialog = new PreferenceDialog(shell, themeManager);
        dialog.open();
    }
}