package com.strukov.qchat.di;

import android.app.Application;
import android.content.Context;

import com.strukov.qchat.chat.di.ChatComponent;
import com.strukov.qchat.chat.di.ChatModule;
import com.strukov.qchat.chat.fragments.messages.di.MessagesComponent;
import com.strukov.qchat.chat.fragments.messages.di.MessagesModule;
import com.strukov.qchat.chat.fragments.search.di.SearchComponent;
import com.strukov.qchat.chat.fragments.search.di.SearchModule;
import com.strukov.qchat.chat.fragments.settings.di.SettingsComponent;
import com.strukov.qchat.chat.fragments.settings.di.SettingsModule;
import com.strukov.qchat.di.components.AppComponent;
import com.strukov.qchat.di.components.DaggerAppComponent;
import com.strukov.qchat.di.modules.AppModule;
import com.strukov.qchat.di.modules.AuthModule;
import com.strukov.qchat.di.modules.DataModule;
import com.strukov.qchat.di.modules.NetModule;
import com.strukov.qchat.di.modules.QChatApiModule;
import com.strukov.qchat.di.modules.RxModule;
import com.strukov.qchat.di.modules.ServiceModule;
import com.strukov.qchat.login.di.LoginComponent;
import com.strukov.qchat.login.di.LoginModule;
import com.strukov.qchat.messaging.di.MessagingComponent;
import com.strukov.qchat.messaging.di.MessagingModule;

import io.realm.Realm;

/**
 * Created by Matthew on 14.12.2017.
 */

public class QChatApp extends Application {

    private static AppComponent sAppComponent;
    private static ChatComponent sChatComponent;
    private static LoginComponent sLoginComponent;
    private static SettingsComponent sSettingsComponent;
    private static SearchComponent sSearchComponent;
    private static MessagesComponent sMessagesComponent;
    private static MessagingComponent sMessagingComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule())
                .rxModule(new RxModule())
                .authModule(new AuthModule())
                .serviceModule(new ServiceModule())
                .netModule(new NetModule("server uri"))
                .qChatApiModule(new QChatApiModule())
                .build();
    }

    public static QChatApp getApp(Context context) {
        return (QChatApp) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return sAppComponent;
    }

    public ChatComponent getChatComponent() {
        if (sChatComponent == null) {
            sChatComponent = sAppComponent.createChatComponent(new ChatModule());
        }
        return sChatComponent;
    }

    public MessagingComponent getMessagingComponent() {
        if (sMessagingComponent == null) {
            sMessagingComponent = sAppComponent.createMessagingComponent(new MessagingModule());
        }
        return sMessagingComponent;
    }

    public SettingsComponent getSettingsComponent() {
        if (sSettingsComponent == null) {
            sSettingsComponent = sChatComponent.settingsComponent(new SettingsModule());
        }
        return sSettingsComponent;
    }

    public SearchComponent getSearchComponent() {
        if (sSearchComponent == null) {
            sSearchComponent = sChatComponent.searchComponent(new SearchModule());
        }
        return sSearchComponent;
    }

    public MessagesComponent getMessagesComponent() {
        if (sMessagesComponent == null) {
            sMessagesComponent = sChatComponent.messagesComponent(new MessagesModule());
        }
        return sMessagesComponent;
    }

    public LoginComponent getLoginComponent() {
        if (sLoginComponent == null) {
            sLoginComponent = sAppComponent.createLoginComponent(new LoginModule());
        }
        return sLoginComponent;
    }

    public void releaseChatComponent() {
        sChatComponent = null;
    }

    public void releaseLoginComponent() {
        sLoginComponent = null;
    }

    public void releaseMessagingComponent() {
        sMessagingComponent = null;
    }

    public void releaseSettingsComponent() {
        sSettingsComponent = null;
    }

    public void releaseSearchComponent() {
        sSearchComponent = null;
    }

    public void releaseMessagesComponent() {
        sMessagesComponent = null;
    }
}
