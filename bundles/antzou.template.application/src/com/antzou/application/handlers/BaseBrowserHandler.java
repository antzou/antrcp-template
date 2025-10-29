package com.antzou.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Shell;

public abstract class BaseBrowserHandler {
    
    /**
     * 获取要打开的URL地址
     */
    protected abstract String getUrl();
    
    /**
     * 获取浏览器打开失败的提示信息
     */
    protected String getErrorMessage() {
        return "无法打开浏览器";
    }
    
    /**
     * 获取浏览器打开成功的提示信息（可选）
     */
    protected String getSuccessMessage() {
        return null; // 默认不显示成功消息
    }
    
    @Execute
    public void execute(Shell shell) {
        String url = getUrl();
        System.out.println("Opening URL: " + url);
        
        if (url != null && !url.isEmpty()) {
            boolean success = openBrowser(shell, url);
            if (success && getSuccessMessage() != null) {
                MessageDialog.openInformation(shell, "提示", getSuccessMessage());
            }
        } else {
            MessageDialog.openWarning(shell, "警告", "未指定要打开的网址");
        }
    }
    
    private boolean openBrowser(Shell shell, String url) {
        try {
            boolean result = Program.launch(url);
            if (!result) {
                MessageDialog.openWarning(shell, "提示", getErrorMessage() + ": " + url);
            }
            return result;
        } catch (Exception ex) {
            MessageDialog.openError(shell, "错误", getErrorMessage() + ": " + ex.getMessage());
            return false;
        }
    }
}