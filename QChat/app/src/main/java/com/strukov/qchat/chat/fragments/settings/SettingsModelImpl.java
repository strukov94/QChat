package com.strukov.qchat.chat.fragments.settings;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import io.reactivex.Observable;
import io.realm.Realm;

/**
 * Created by Matthew on 21.12.2017.
 */

public class SettingsModelImpl implements SettingsModel {
    private Context mContext;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Realm mRealm;

    public SettingsModelImpl(Context context, Realm realm, FirebaseAuth auth, GoogleSignInClient googleSignInClient){
        mContext = context;
        mAuth = auth;
        mGoogleSignInClient = googleSignInClient;
        mRealm = realm;
    }

    @Override
    public Observable<Boolean> firebaseSignOut() {
        mAuth.signOut();
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> deleteToken() throws IOException {
        FirebaseInstanceId.getInstance().deleteInstanceId();
        FirebaseInstanceId.getInstance().getToken();
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> googleSignOut() {
        return Observable.just(mGoogleSignInClient.signOut().isSuccessful());
    }
}
