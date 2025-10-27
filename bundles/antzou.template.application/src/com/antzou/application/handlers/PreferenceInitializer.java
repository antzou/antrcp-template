package com.antzou.application.handlers;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.antzou.application.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {
    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault("SHOW_TOOLBAR", true);
        store.setDefault("AUTO_SAVE_INTERVAL", 10);
    }
}