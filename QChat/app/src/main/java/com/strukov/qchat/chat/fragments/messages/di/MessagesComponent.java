package com.strukov.qchat.chat.fragments.messages.di;

import com.strukov.qchat.chat.fragments.messages.MessagesFragment;

import dagger.Subcomponent;

/**
 * Created by Matthew on 28.12.2017.
 */

@MessagesScope
@Subcomponent(modules = MessagesModule.class)
public interface MessagesComponent {
    void inject(MessagesFragment fragment);
}
