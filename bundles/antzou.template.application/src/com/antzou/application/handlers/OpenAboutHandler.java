package com.antzou.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

public class OpenAboutHandler {
    @Execute
    public void execute(Shell shell) {
        // 在e4中，通常通过部件服务显示部件
        // 假设"AboutDialog"是您的部件ID
//        partService.showPart("com.example.e4.part.about", PartState.ACTIVATE);
    	
    	new AboutDialog().createComposite(shell);
    }
}