package com.strukov.qchat.di.modules;

import com.strukov.qchat.utils.rx.AppRxSchedulers;
import com.strukov.qchat.utils.rx.RxSchedulers;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Matthew on 14.12.2017.
 */

@Module
public class RxModule {
    @Singleton
    @Provides
    RxSchedulers providesRxSchedulers() {
        return new AppRxSchedulers();
    }
}
