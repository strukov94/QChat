package com.strukov.qchat.messaging;

import com.strukov.qchat.models.Friend;
import com.strukov.qchat.utils.NetworkUtils;
import com.strukov.qchat.utils.rx.RxSchedulers;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;


/**
 * Created by Matthew on 26.12.2017.
 */

public class MessagingPresenterImpl implements MessagingPresenter {
    private MessagingModelImpl mModel;
    private RxSchedulers mRxSchedulers;
    private CompositeDisposable mDisposable;
    private MessagingView mView;
    private int mFriendId;

    public MessagingPresenterImpl(MessagingModelImpl model, RxSchedulers schedulers, CompositeDisposable disposable) {
        mModel = model;
        mRxSchedulers = schedulers;
        mDisposable = disposable;
    }

    @Override
    public void onCreate(MessagingView view, int userId) {
        mView = view;
        mFriendId = userId;
        mDisposable.add(getUserInfo());
        mDisposable.add(getMessages());
    }

    @Override
    public Disposable getMessages() {
        return mModel.getUserForMessages()
                .filter(user -> user.isLoaded())
                .flatMap(user -> mModel.getMessages(user, mFriendId))
                .observeOn(mRxSchedulers.androidThread())
                .subscribe(messages -> mView.setRecyclerView(messages));
    }

    @Override
    public void sendMessage(String text) {
        mDisposable.add(mModel.newMessage(String.valueOf(mFriendId), mModel.setMessage(text, mFriendId))
                .subscribeOn(mRxSchedulers.internet())
                .observeOn(mRxSchedulers.androidThread())
                .subscribe(messageResponse -> mModel.updateMessageStatus(messageResponse)
                        , NetworkUtils::responseError));
    }

    private Disposable getUserInfo() {
        return mModel.isFriend(mFriendId)
                .doOnNext(aBoolean -> {
                    if (aBoolean) setUserInfo();
                })
                .filter(aBoolean -> !aBoolean)
                .flatMap(aBoolean -> mModel.newFriend(mFriendId))
                .observeOn(mRxSchedulers.androidThread())
                .subscribe(friend -> {
                    mModel.saveFriend(friend);
                    mView.setUserInfo(friend);
                }, NetworkUtils::responseError);
    }

    private Disposable setUserInfo() {
        return mModel.getFriend(mFriendId)
                .filter(new Predicate<Friend>() {
                    @Override
                    public boolean test(Friend friend) throws Exception {
                        return friend.isLoaded();
                    }
                })
                .observeOn(mRxSchedulers.androidThread())
                .subscribe(friend -> mView.setUserInfo(friend));
    }

    @Override
    public void onDestroy() {
        if (!mDisposable.isDisposed()) mDisposable.clear();
    }

    @Override
    public void onCloseRealm() {
        mModel.onCloseRealm();
    }
}
