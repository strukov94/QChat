package com.strukov.qchat.messaging.di;

import com.strukov.qchat.messaging.MessagingActivity;

import dagger.Subcomponent;

/**
 * Created by Matthew on 26.12.2017.
 */

@MessagingScope
@Subcomponent(modules = MessagingModule.class)
public interface MessagingComponent {
    void inject(MessagingActivity activity);
}
