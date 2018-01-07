package com.strukov.qchat.messaging;

import io.reactivex.disposables.Disposable;

/**
 * Created by Matthew on 26.12.2017.
 */

public interface MessagingPresenter {
    void onCreate(MessagingView view, int userId);

    void sendMessage(String text);

    void onDestroy();

    void onCloseRealm();

    Disposable getMessages();
}
