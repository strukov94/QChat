package com.strukov.qchat.chat;

import com.google.firebase.auth.FirebaseUser;
import com.strukov.qchat.models.User;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by Matthew on 14.12.2017.
 */

public interface ChatModel {
    Observable<Boolean> isNetworkAvailable();

    Observable<Boolean> isSignedInAccount();

    Observable<FirebaseUser> getLastSignedInAccount();

    Flowable<User> getUser(String token);

    void onCloseRealm();
}

