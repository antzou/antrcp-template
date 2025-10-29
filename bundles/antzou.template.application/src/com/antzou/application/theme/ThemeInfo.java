package com.antzou.application.theme;
/**
     * 主题信息类
     */
    public class ThemeInfo {
        private final String id;
        private final String name;
        
        public ThemeInfo(String id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        
        @Override
        public String toString() {
            return name;
        }
    }