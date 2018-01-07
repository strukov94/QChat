package com.strukov.qchat.chat;

import com.google.firebase.auth.FirebaseUser;
import com.strukov.qchat.models.User;
import com.strukov.qchat.utils.rx.RxSchedulers;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


/**
 * Created by Matthew on 14.12.2017.
 */

public class ChatPresenterImpl implements ChatPresenter {

    private ChatView mView;
    private ChatModelImpl mModel;
    private RxSchedulers mRxSchedulers;
    private CompositeDisposable mDisposable;

    public ChatPresenterImpl(ChatModelImpl model, RxSchedulers schedulers, CompositeDisposable disposable) {
        mModel = model;
        mRxSchedulers = schedulers;
        mDisposable = disposable;
    }

    @Override
    public void onCreate(ChatView view) {
        mView = view;
        mDisposable.add(getNetworkState());
    }

    private Disposable getNetworkState() {
        return mModel.isNetworkAvailable()
                .doOnNext(network -> {
                    if (!network) mView.onNetworkUnavailable();
                })
                .subscribe();
    }

    private Disposable getSignedAccount() {
        return mModel.isSignedInAccount()
                .doOnNext(signIn -> {
                    if (!signIn) mView.onStartLogIn();
                })
                .filter(signIn -> signIn)
                .flatMap(signIn -> mModel.getLastSignedInAccount())
                .toFlowable(BackpressureStrategy.BUFFER)
                .switchMap(firebaseUser -> mModel.getUser(firebaseUser.getUid()))
                .filter(user -> user.isLoaded())
                .observeOn(mRxSchedulers.androidThread())
                .subscribe(user -> mView.setUser(user.getImage(), user.getName()));
    }

    @Override
    public void onDestroy() {
        if (!mDisposable.isDisposed())
            mDisposable.clear();
    }

    @Override
    public void onStart() {
        mDisposable.add(getSignedAccount());
    }

    @Override
    public void onCloseRealm() {
        mModel.onCloseRealm();
    }
}
