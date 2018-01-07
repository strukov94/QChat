package com.strukov.qchat.di.modules;

import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Matthew on 23.12.2017.
 */

@Module
public class ServiceModule {
    @Provides
    @Singleton
    FirebaseInstanceId providesFirebaseInstanceId() {
        return FirebaseInstanceId.getInstance();
    }
}
