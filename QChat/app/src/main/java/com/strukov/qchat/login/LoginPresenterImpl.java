package com.strukov.qchat.login;

import android.content.Intent;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.strukov.qchat.utils.ErrorUtils;
import com.strukov.qchat.utils.NetworkUtils;
import com.strukov.qchat.utils.rx.RxSchedulers;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Matthew on 15.12.2017.
 */

public class LoginPresenterImpl implements LoginPresenter {

    private LoginModelImpl mModel;
    private GoogleSignInClient mGoogleSignInClient;
    private CompositeDisposable mDisposable;
    private RxSchedulers mRxSchedulers;
    private LoginActivity mView;
    private FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 1000;

    public LoginPresenterImpl(LoginModelImpl model, GoogleSignInClient googleSignInClient, RxSchedulers schedulers,
                              CompositeDisposable disposable, FirebaseAuth auth) {
        mModel = model;
        mGoogleSignInClient = googleSignInClient;
        mDisposable = disposable;
        mRxSchedulers = schedulers;
        mAuth = auth;
    }

    @Override
    public void onCreate(LoginActivity view) {
        mView = view;
    }

    @Override
    public void onClick(View v) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mView.startSignInActivity(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onDestroy() {
        if (!mDisposable.isDisposed()) mDisposable.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            mDisposable.add(handleSignInResult(data));
        }
    }

    private Disposable handleSignInResult(Intent data) {
        return mModel.isNetworkAvailable()
                .doOnNext(aBoolean -> {
                    if (!aBoolean) mView.onNetworkUnavailable();
                })
                .filter(aBoolean -> aBoolean)
                .flatMap(aBoolean -> mModel.getSignedInAccountFromIntent(data))
                .flatMap(completedTask -> {
                    try {
                        return mModel.getResult(completedTask);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                })
                .subscribeOn(mRxSchedulers.internet())
                .observeOn(mRxSchedulers.androidThread())
                .subscribe(account -> mDisposable.add(LoginPresenterImpl.this.firebaseAuthWithGoogle(account))
                        , ErrorUtils::signInError);
    }

    private Disposable firebaseAuthWithGoogle(GoogleSignInAccount account) {
        return Single.create(e -> {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential).addOnCompleteListener(mView, task -> {
                if (task.isSuccessful()) e.onSuccess(task);
                else e.onError(task.getException());
            });
        })
                .subscribeOn(mRxSchedulers.internet())
                .observeOn(mRxSchedulers.androidThread())
                .subscribe(task -> mDisposable.add(createUser(mAuth.getCurrentUser())));
    }

    private Disposable createUser(FirebaseUser firebaseUser) {
        return mModel.isUserExist(firebaseUser.getUid())
                .doOnNext(aBoolean -> {
                    if (aBoolean) {
                        mView.updateMessages(mModel.getUserId(firebaseUser.getUid()));
                        mDisposable.add(updateUserServerFCM(firebaseUser));
                        mView.finishSignInActivity();
                    }
                })
                .filter(aBoolean -> !aBoolean)
                .flatMap(aBoolean -> mModel.postUser(firebaseUser))
                .observeOn(mRxSchedulers.androidThread())
                .subscribe(user -> {
                    mModel.createUser(user);
                    mView.finishSignInActivity();
                }, NetworkUtils::responseError);
    }

    private Disposable updateUserServerFCM(FirebaseUser firebaseUser) {
        return mModel.updateServerTokenFCM(firebaseUser)
                .subscribeOn(mRxSchedulers.internet())
                .observeOn(mRxSchedulers.androidThread())
                .subscribe(user -> mModel.updateClientTokenFCM(user), NetworkUtils::responseError);
    }

    @Override
    public void onCloseRealm() {
        mModel.onCloseRealm();
    }
}
