package com.strukov.qchat.chat.fragments.messages;

import com.strukov.qchat.models.Message;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Matthew on 28.12.2017.
 */

public interface MessagesView {
    void setRecyclerView(RealmResults<Message> messages, Realm realm, int userId);
}
