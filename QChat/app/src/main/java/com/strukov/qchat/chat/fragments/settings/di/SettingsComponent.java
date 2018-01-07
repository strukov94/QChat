package com.strukov.qchat.chat.fragments.settings.di;

import com.strukov.qchat.chat.fragments.settings.SettingsFragment;

import dagger.Subcomponent;

/**
 * Created by Matthew on 21.12.2017.
 */

@SettingsScope
@Subcomponent(modules = SettingsModule.class)
public interface SettingsComponent {
    void inject(SettingsFragment fragment);
}
