package com.strukov.qchat.chat.fragments.settings;

import com.google.android.gms.tasks.Task;

import java.io.IOException;

import io.reactivex.Observable;

/**
 * Created by Matthew on 21.12.2017.
 */

public interface SettingsModel {
    public Observable<Boolean> firebaseSignOut();

    public Observable<Boolean> deleteToken() throws IOException;

    public Observable<Boolean> googleSignOut();
}
