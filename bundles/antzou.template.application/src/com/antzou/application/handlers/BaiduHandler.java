package com.antzou.application.handlers;

public class BaiduHandler extends BaseBrowserHandler {
    @Override
    protected String getUrl() {
        return "https://www.baidu.com";
    }
    
    @Override
    protected String getErrorMessage() {
        return "无法打开百度";
    }
}