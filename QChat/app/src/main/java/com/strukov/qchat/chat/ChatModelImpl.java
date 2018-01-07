package com.strukov.qchat.chat;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.strukov.qchat.models.User;
import com.strukov.qchat.utils.NetworkUtils;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.realm.Realm;

/**
 * Created by Matthew on 14.12.2017.
 */

public class ChatModelImpl implements ChatModel {

    private Context mContext;
    private FirebaseAuth mAuth;
    private Realm mRealm;

    public ChatModelImpl(Context context, FirebaseAuth auth, Realm realm) {
        mContext = context;
        mAuth = auth;
        mRealm = realm;
    }

    @Override
    public Observable<Boolean> isNetworkAvailable() {
        return NetworkUtils.isNetworkAvailableObservable(mContext);
    }

    @Override
    public Observable<Boolean> isSignedInAccount() {
        return mAuth.getCurrentUser() != null ? Observable.just(true) : Observable.just(false);
    }

    @Override
    public Observable<FirebaseUser> getLastSignedInAccount() {
        return Observable.just(mAuth.getCurrentUser());
    }

    @Override
    public Flowable<User> getUser(String token) {
        return mRealm.where(User.class).equalTo("token_auth", token).findFirstAsync().asFlowable();
    }

    @Override
    public void onCloseRealm() {
        mRealm.close();
    }
}
