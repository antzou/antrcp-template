package com.antzou.application;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
    private static Activator plugin;
    private IPreferenceStore preferenceStore;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static Activator getDefault() {
        return plugin;
    }

    @Override
    public IPreferenceStore getPreferenceStore() {
        if (preferenceStore == null) {
            preferenceStore = new ScopedPreferenceStore(
                InstanceScope.INSTANCE, 
                "antzou.template.application.preferences"
            );
        }
        return preferenceStore;
    }
}