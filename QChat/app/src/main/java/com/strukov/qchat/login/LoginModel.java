package com.strukov.qchat.login;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.strukov.qchat.models.Messages;
import com.strukov.qchat.models.User;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Matthew on 15.12.2017.
 */

public interface LoginModel {
    Observable<Boolean> isNetworkAvailable();

    Observable<Task<GoogleSignInAccount>> getSignedInAccountFromIntent(Intent data);

    Observable<GoogleSignInAccount> getResult(Task<GoogleSignInAccount> completedTask) throws ApiException;

    Observable<Boolean> isUserExist(String email);

    int getUserId(String tokenAuth);

    Observable<Boolean> createUser(User user);

    Observable<Messages> getMessages(int userId, boolean isSent);

    Observable<User> postUser(FirebaseUser firebaseUser);

    void onCloseRealm();

    Observable<User> updateServerTokenFCM(FirebaseUser user);

    void updateClientTokenFCM(User user);

    int getUserPref();
}
