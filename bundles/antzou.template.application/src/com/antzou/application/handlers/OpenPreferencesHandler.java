package com.antzou.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.preference.*;
import org.eclipse.swt.widgets.Shell;

public class OpenPreferencesHandler {
    @Execute
    public void execute(Shell shell) {
        PreferenceManager manager = new PreferenceManager();
        
        // 正确使用 PreferenceNode 构造函数
//        manager.addToRoot(new PreferenceNode("general", "General", 
//                null, GeneralPreferencePage.class.getName()));
        
//        manager.addToRoot(new PreferenceNode("editor", "Editor",
//                null, EditorPreferencePage.class.getName()));
        
        PreferenceDialog dialog = new PreferenceDialog(shell, manager);
        dialog.create();
        dialog.open();
    }
}