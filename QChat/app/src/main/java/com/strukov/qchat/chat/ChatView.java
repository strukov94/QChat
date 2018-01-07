package com.strukov.qchat.chat;

/**
 * Created by Matthew on 14.12.2017.
 */

public interface ChatView {
    void onNetworkUnavailable();

    void onStartLogIn();

    void setUser(String image, String name);

    void deleteFragment();
}
