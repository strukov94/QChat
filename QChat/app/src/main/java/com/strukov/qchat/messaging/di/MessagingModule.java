package com.strukov.qchat.messaging.di;

import android.content.SharedPreferences;

import com.strukov.qchat.api.QChatApi;
import com.strukov.qchat.messaging.MessagingModelImpl;
import com.strukov.qchat.messaging.MessagingPresenterImpl;
import com.strukov.qchat.utils.rx.RxSchedulers;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

/**
 * Created by Matthew on 26.12.2017.
 */

@Module
public class MessagingModule {
    @Provides
    @MessagingScope
    Realm providesRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @MessagingScope
    CompositeDisposable CompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    @MessagingScope
    MessagingModelImpl providesMessagingModelImpl(QChatApi api, Realm realm, SharedPreferences sharedPreferences) {
        return new MessagingModelImpl(api, realm, sharedPreferences);
    }

    @Provides
    @MessagingScope
    MessagingPresenterImpl providesMessagingPresenterImpl(MessagingModelImpl model, RxSchedulers schedulers, CompositeDisposable disposable) {
        return new MessagingPresenterImpl(model, schedulers, disposable);
    }
}
