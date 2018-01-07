package com.strukov.qchat.chat.di;

import com.strukov.qchat.chat.MainActivity;
import com.strukov.qchat.chat.fragments.messages.MessagesFragment;
import com.strukov.qchat.chat.fragments.messages.di.MessagesComponent;
import com.strukov.qchat.chat.fragments.messages.di.MessagesModule;
import com.strukov.qchat.chat.fragments.search.SearchFragment;
import com.strukov.qchat.chat.fragments.search.di.SearchComponent;
import com.strukov.qchat.chat.fragments.search.di.SearchModule;
import com.strukov.qchat.chat.fragments.settings.SettingsFragment;
import com.strukov.qchat.chat.fragments.settings.di.SettingsComponent;
import com.strukov.qchat.chat.fragments.settings.di.SettingsModule;

import dagger.Subcomponent;

/**
 * Created by Matthew on 14.12.2017.
 */

@ChatScope
@Subcomponent(modules = ChatModule.class)
public interface ChatComponent {
    void inject(MainActivity activity);
    SettingsFragment settingsFragment();
    MessagesFragment messagesFragment();
    SearchFragment searchFragment();
    SettingsComponent settingsComponent(SettingsModule module);
    SearchComponent searchComponent(SearchModule module);
    MessagesComponent messagesComponent(MessagesModule module);
}
