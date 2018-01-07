package com.strukov.qchat.chat.fragments.messages;

import com.strukov.qchat.models.Message;
import com.strukov.qchat.utils.rx.RxSchedulers;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.realm.RealmResults;

/**
 * Created by Matthew on 28.12.2017.
 */

public class MessagesPresenterImpl implements MessagesPresenter {
    private MessagesModel mModel;
    private MessagesView mView;
    private RxSchedulers mRxSchedulers;
    private CompositeDisposable mDisposable;

    public MessagesPresenterImpl(MessagesModelImpl model, RxSchedulers schedulers, CompositeDisposable disposable) {
        mModel = model;
        mRxSchedulers = schedulers;
        mDisposable = disposable;
    }

    @Override
    public void onCreate(MessagesView view) {
        mView = view;
        mDisposable.add(getLastMessages());
    }

    private Disposable getLastMessages() {
        return mModel.getLastMessages()
                .filter(RealmResults::isLoaded)
                .observeOn(mRxSchedulers.androidThread())
                .subscribe(messages -> mView.setRecyclerView(messages, mModel.getRealm(), mModel.getUserPref()));
    }

    @Override
    public void onDestroy() {
        if (!mDisposable.isDisposed()) mDisposable.clear();
    }
}
