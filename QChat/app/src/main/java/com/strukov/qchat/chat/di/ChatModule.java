package com.strukov.qchat.chat.di;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.strukov.qchat.chat.ChatModelImpl;
import com.strukov.qchat.chat.ChatPresenterImpl;
import com.strukov.qchat.chat.fragments.messages.MessagesFragment;
import com.strukov.qchat.chat.fragments.search.SearchFragment;
import com.strukov.qchat.chat.fragments.settings.SettingsFragment;
import com.strukov.qchat.utils.rx.RxSchedulers;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

/**
 * Created by Matthew on 14.12.2017.
 */

@Module
public class ChatModule {
    @Provides
    @ChatScope
    SettingsFragment providesSettingsFragment() {
        return new SettingsFragment();
    }

    @Provides
    @ChatScope
    MessagesFragment providesMessagesFragment() {
        return new MessagesFragment();
    }

    @Provides
    @ChatScope
    SearchFragment providesSearchFragment() {
        return new SearchFragment();
    }

    @Provides
    @ChatScope
    Realm providesRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @ChatScope
    ChatModelImpl providesModel(Context context, FirebaseAuth auth, Realm realm) {
        return new ChatModelImpl(context, auth, realm);
    }

    @Provides
    @ChatScope
    CompositeDisposable CompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    @ChatScope
    ChatPresenterImpl provides(ChatModelImpl model, RxSchedulers schedulers, CompositeDisposable disposable) {
        return new ChatPresenterImpl(model, schedulers, disposable);
    }
}
