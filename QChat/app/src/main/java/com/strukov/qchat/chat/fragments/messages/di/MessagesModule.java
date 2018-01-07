package com.strukov.qchat.chat.fragments.messages.di;

import android.content.SharedPreferences;

import com.strukov.qchat.chat.fragments.messages.MessagesModelImpl;
import com.strukov.qchat.chat.fragments.messages.MessagesPresenterImpl;
import com.strukov.qchat.utils.rx.RxSchedulers;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

/**
 * Created by Matthew on 28.12.2017.
 */

@Module
public class MessagesModule {
    @Named("messages")
    @MessagesScope
    @Provides
    CompositeDisposable CompositeDisposable() {
        return new CompositeDisposable();
    }

    @MessagesScope
    @Provides
    MessagesModelImpl providesMessagesModelImpl(Realm realm, SharedPreferences sharedPreferences) {
        return new MessagesModelImpl(realm, sharedPreferences);
    }

    @MessagesScope
    @Provides
    MessagesPresenterImpl providesMessagesPresenterImpl(RxSchedulers schedulers, MessagesModelImpl model, CompositeDisposable disposable) {
        return new MessagesPresenterImpl(model, schedulers, disposable);
    }
}
