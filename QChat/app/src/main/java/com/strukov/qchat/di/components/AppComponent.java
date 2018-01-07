package com.strukov.qchat.di.components;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.strukov.qchat.api.QChatApi;
import com.strukov.qchat.chat.di.ChatComponent;
import com.strukov.qchat.chat.di.ChatModule;
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
import com.strukov.qchat.services.QChatFirebaseInstanceIDService;
import com.strukov.qchat.services.QChatFirebaseMessagingService;
import com.strukov.qchat.services.UpdateMessagesIntentService;
import com.strukov.qchat.utils.rx.RxSchedulers;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by Matthew on 14.12.2017.
 */

@Singleton
@Component(modules = {AppModule.class, DataModule.class, RxModule.class, AuthModule.class, ServiceModule.class, NetModule.class, QChatApiModule.class})
public interface AppComponent {
    Context getContext();
    SharedPreferences getSharedPreferences();
    RxSchedulers getRxSchedulers();
    FirebaseAuth getFirebaseAuth();
    FirebaseInstanceId getFirebaseInstanceId();
    Retrofit getRetrofit();
    QChatApi getQChatApi();

    ChatComponent createChatComponent(ChatModule module);
    LoginComponent createLoginComponent(LoginModule module);
    MessagingComponent createMessagingComponent(MessagingModule module);

    void injectFirebaseInstanceIDService(QChatFirebaseInstanceIDService service);
    void injectFirebaseMessagingService (QChatFirebaseMessagingService service);
    void injectUpdateMessagesIntentService (UpdateMessagesIntentService service);
}
