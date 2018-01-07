package com.strukov.qchat.chat.fragments.search;

import com.strukov.qchat.models.Users;
import com.strukov.qchat.utils.NetworkUtils;
import com.strukov.qchat.utils.rx.RxSchedulers;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Matthew on 24.12.2017.
 */

public class SearchPresenterImpl implements SearchPresenter {
    private RxSchedulers mRxSchedulers;
    private SearchModelImpl mModel;
    private SearchView mView;
    private Users mUsers;
    private CompositeDisposable mDisposable;

    public SearchPresenterImpl(SearchModelImpl model, RxSchedulers schedulers, CompositeDisposable disposable) {
        mRxSchedulers = schedulers;
        mModel = model;
        mDisposable = disposable;
    }

    @Override
    public void onCreate(SearchView view) {
        mView = view;
        if (mUsers == null)
            mDisposable.add(getUsers());
        else mView.swapAdapter(mUsers);
    }

    @Override
    public Disposable getUsers() {
        return mModel.isNetworkAvailable()
                .doOnNext(aBoolean -> {
                    if (!aBoolean) mView.onNetworkUnavailable();
                })
                .filter(aBoolean -> aBoolean)
                .flatMap(aBoolean -> mModel.getUsers())
                .subscribeOn(mRxSchedulers.internet())
                .observeOn(mRxSchedulers.androidThread())
                .subscribe(users -> {
                    mView.swapAdapter(users);
                    mUsers = users;
                    }, NetworkUtils::responseError);
    }

    @Override
    public void onDestroy() {
        if (!mDisposable.isDisposed()) mDisposable.clear();
    }
}
