package com.strukov.qchat.chat.fragments.settings;

import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.strukov.qchat.chat.MainActivity;
import com.strukov.qchat.utils.ErrorUtils;
import com.strukov.qchat.utils.rx.RxSchedulers;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Matthew on 21.12.2017.
 */

public class SettingsPresenterImpl implements SettingsPresenter {
    private SettingsModelImpl mModel;
    private SettingsView mView;
    private RxSchedulers mRxSchedulers;
    private CompositeDisposable mDisposable;

    public SettingsPresenterImpl(SettingsModelImpl model, RxSchedulers rxSchedulers, CompositeDisposable disposable) {
        mModel = model;
        mRxSchedulers = rxSchedulers;
        mDisposable = disposable;
    }

    @Override
    public void onCreate(SettingsView view) {
        mView = view;
    }

    @Override
    public void onClick(View v) {
        mDisposable.add(mModel.firebaseSignOut()
                .flatMap(aBoolean -> mModel.deleteToken())
                .flatMap(aBoolean -> mModel.googleSignOut())
                .subscribeOn(mRxSchedulers.internet())
                .observeOn(mRxSchedulers.androidThread())
                .subscribe(aBoolean -> mView.startLoginActivity(), ErrorUtils::signOutError));
    }

    @Override
    public void onDestroy() {
        if (!mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }
}
