package com.antzou.application.handlers;

public class DeepSeekHandler extends BaseBrowserHandler {
    @Override
    protected String getUrl() {
        return "https://chat.deepseek.com";
    }
    
    @Override
    protected String getErrorMessage() {
        return "无法打开DeepSeek";
    }
}