package com.antzou.application.theme;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.widgets.Display;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.*;

@Creatable
@Singleton
public class ThemeManager {
    
    public static final String THEME_CHANGED_EVENT = "theme/changed";
    public static final String THEME_PREFERENCE_KEY = "current.theme.id";
    
    @Inject
    private IEventBroker eventBroker;
    
    @Inject
    private IEclipseContext context;
    
    private IThemeEngine themeEngine;
    
    /**
     * 获取所有可用主题
     */
    public List<ThemeInfo> getAvailableThemes() {
        IThemeEngine engine = getThemeEngine();
        List<ThemeInfo> themes = new ArrayList<>();
        
        if (engine == null) {
        	return themes;
        }
        
        for (ITheme theme : engine.getThemes()) {
            themes.add(new ThemeInfo(theme.getId(), theme.getLabel()));
        }
        return themes;
    }
    
    /**
     * 获取当前主题
     */
    public String getCurrentTheme() {
        IThemeEngine engine = getThemeEngine();
        if (engine != null && engine.getActiveTheme() != null) {
            String activeTheme = engine.getActiveTheme().getId();
            return activeTheme;
        }
        
        String savedTheme = getPreference(THEME_PREFERENCE_KEY, "com.antzou.application.theme.classic");
        return savedTheme;
    }
    
    /**
     * 设置主题
     */
    public boolean setTheme(String themeId) {
        IThemeEngine engine = getThemeEngine();
        if (engine == null) {
        	setPreference(THEME_PREFERENCE_KEY, themeId);
            return false;
        }
        
        try {
            engine.setTheme(themeId, true);
            // 通过检查当前主题来验证是否设置成功
            if (isThemeActive(themeId)) {
                setPreference(THEME_PREFERENCE_KEY, themeId);
                
                // 发送主题改变事件
                if (eventBroker != null) {
                    eventBroker.post(THEME_CHANGED_EVENT, themeId);
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 检查主题是否已激活
     */
    private boolean isThemeActive(String themeId) {
        IThemeEngine engine = getThemeEngine();
        if (engine != null && engine.getActiveTheme() != null) {
            return themeId.equals(engine.getActiveTheme().getId());
        }
        return false;
    }
    
    private IThemeEngine getThemeEngine() {
        if (themeEngine == null) {
            themeEngine = context.get(IThemeEngine.class);
        }
        return themeEngine;
    }
    
    private String getPreference(String key, String defaultValue) {
        return PreferenceStore.getValue(key, defaultValue);
    }
    
    private void setPreference(String key, String value) {
    	PreferenceStore.setValue(key, value);
    }
}