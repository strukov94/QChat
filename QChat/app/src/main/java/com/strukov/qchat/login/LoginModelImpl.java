package com.strukov.qchat.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.strukov.qchat.api.QChatApi;
import com.strukov.qchat.models.Messages;
import com.strukov.qchat.models.User;
import com.strukov.qchat.utils.NetworkUtils;

import io.reactivex.Observable;
import io.realm.Realm;

/**
 * Created by Matthew on 15.12.2017.
 */

public class LoginModelImpl implements LoginModel {
    private Context mContext;
    private Realm mRealm;
    private QChatApi mApi;
    private FirebaseInstanceId mInstanceId;
    private SharedPreferences mPref;

    public LoginModelImpl(Context context, Realm realm, QChatApi api, FirebaseInstanceId instanceId, SharedPreferences sharedPreferences) {
        mContext = context;
        mRealm = realm;
        mApi = api;
        mInstanceId = instanceId;
        mPref = sharedPreferences;
    }

    @Override
    public Observable<Boolean> isNetworkAvailable() {
        return NetworkUtils.isNetworkAvailableObservable(mContext);
    }

    @Override
    public Observable<Task<GoogleSignInAccount>> getSignedInAccountFromIntent(Intent data) {
        return Observable.just(GoogleSignIn.getSignedInAccountFromIntent(data));
    }

    @Override
    public Observable<GoogleSignInAccount> getResult(Task<GoogleSignInAccount> completedTask) throws ApiException {
        return Observable.just(completedTask.getResult(ApiException.class));
    }

    @Override
    public Observable<Messages> getMessages(int userId, boolean isSent) {
        return mApi.getMessages(userId, isSent);
    }

    @Override
    public Observable<Boolean> isUserExist(String tokenAuth) {
        User user = mRealm.where(User.class)
                .equalTo("token_auth", tokenAuth)
                .findFirst();

        return Observable.just(user != null);
    }

    @Override
    public int getUserId(String tokenAuth) {
        User user = mRealm.where(User.class)
                .equalTo("token_auth", tokenAuth)
                .findFirst();
        setUserPref(user.getId());
        return user.getId();
    }

    @Override
    public Observable<Boolean> createUser(User user) {
        mRealm.executeTransactionAsync(realm -> {
            realm.copyToRealm(user);
        });
        setUserPref(user.getId());
        return Observable.just(true);
    }

    @Override
    public Observable<User> postUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setToken_fcm(mInstanceId.getToken());
        user.setToken_auth(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(firebaseUser.getDisplayName());
        user.setImage(firebaseUser.getPhotoUrl().toString());
        return mApi.newUser(user);
    }

    @Override
    public void onCloseRealm() {
        mRealm.close();
    }

    @Override
    public Observable<User> updateServerTokenFCM(FirebaseUser firebaseUser) {
        String tokenAuth = firebaseUser.getUid();
        String tokenFCM = mInstanceId.getToken();

        User user = new User();
        user.setToken_auth(tokenAuth);
        user.setToken_fcm(tokenFCM);
        return mApi.updateUser(user);
    }

    @Override
    public void updateClientTokenFCM(User user) {
        mRealm.executeTransactionAsync(realm -> {
            User newUser = realm.where(User.class).equalTo("token_auth", user.getToken_auth()).findFirst();
            if (newUser != null) {
                newUser.setToken_fcm(user.getToken_fcm());
                realm.insertOrUpdate(newUser);
            }
        });
    }

    private void setUserPref(int userId) {
        if (userId != 0) {
            SharedPreferences.Editor editor = mPref.edit();
            editor.putInt("user_id", userId);
            editor.apply();
        }
    }

    @Override
    public int getUserPref() {
        return mPref.getInt("user_id", 0);
    }
}
