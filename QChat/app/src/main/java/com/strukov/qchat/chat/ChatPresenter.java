package com.strukov.qchat.chat;

/**
 * Created by Matthew on 14.12.2017.
 */

public interface ChatPresenter {
    void onCreate(ChatView view);

    void onDestroy();

    void onStart();

    void onCloseRealm();
}
