package com.strukov.qchat.messaging;

import com.strukov.qchat.models.Friend;
import com.strukov.qchat.models.Message;

import io.realm.RealmResults;

/**
 * Created by Matthew on 26.12.2017.
 */

public interface MessagingView {
    void setUserInfo(Friend friend);

    void setRecyclerView(RealmResults<Message> messages);
}
