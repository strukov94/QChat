package com.strukov.qchat.di.modules;

import com.strukov.qchat.api.QChatApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Matthew on 23.12.2017.
 */

@Module
public class QChatApiModule {
    @Provides
    @Singleton
    QChatApi providesQChatApi(Retrofit retrofit) {
        return retrofit.create(QChatApi.class);
    }
}
