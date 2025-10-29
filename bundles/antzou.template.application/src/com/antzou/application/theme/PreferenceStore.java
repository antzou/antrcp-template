package com.antzou.application.theme;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

public class PreferenceStore {
    
    private static final String QUALIFIER = "com.antzou.application";
    //InstanceScope 通常更适合应用级别的偏好设置，而 ConfigurationScope 更适合整个产品的全局配置。
    public static void setValue(String key, String value) {
//        IEclipsePreferences prefs = ConfigurationScope.INSTANCE.getNode(QUALIFIER);
        IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(QUALIFIER);
        prefs.put(key, value);
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }
    
    public static String getValue(String key, String defaultValue) {
        IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(QUALIFIER);
        return prefs.get(key, defaultValue);
    }
}