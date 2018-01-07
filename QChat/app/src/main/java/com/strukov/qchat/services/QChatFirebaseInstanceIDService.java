package com.strukov.qchat.services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.strukov.qchat.api.QChatApi;
import com.strukov.qchat.di.QChatApp;
import com.strukov.qchat.models.User;
import com.strukov.qchat.utils.NetworkUtils;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Matthew on 23.12.2017.
 */

public class QChatFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Inject
    FirebaseInstanceId instanceId;

    @Inject
    QChatApi api;

    @Inject
    FirebaseAuth auth;

    @Override
    public void onCreate() {
        super.onCreate();
        QChatApp.getApp(this).getAppComponent().injectFirebaseInstanceIDService(this);
    }

    @Override
    public void onTokenRefresh() {
        String tokenAuth, tokenFCM = "";

        try {
            tokenAuth = auth.getCurrentUser().getUid();
            tokenFCM = instanceId.getToken();
            User user = new User();
            user.setToken_auth(tokenAuth);
            user.setToken_fcm(tokenFCM);
            api.updateUser(user).subscribe(this::updateRealm, NetworkUtils::responseError);

        } catch (Exception e) {
            Log.w("TOKEN_AUTH:", e.getMessage());
        }
    }

    private void updateRealm(User user) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(realmAsync -> {
            User newUser = realmAsync.where(User.class).equalTo("token_auth", user.getToken_auth()).findFirst();
            if (newUser != null) {
                newUser.setToken_fcm(user.getToken_fcm());
                realmAsync.insertOrUpdate(newUser);
            }
        });
        realm.close();
    }
}
