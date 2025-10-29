package com.antzou.application.handlers;

public class ProjectHandler extends BaseBrowserHandler {
    @Override
    protected String getUrl() {
        return "https://gitee.com/antzou/antrcp-template";
    }
    
    @Override
    protected String getErrorMessage() {
        return "无法打开项目主页";
    }
}