package com.strukov.qchat.chat.fragments.messages;

import com.strukov.qchat.models.Message;

import io.reactivex.Flowable;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Matthew on 28.12.2017.
 */

public interface MessagesModel {
    Flowable<RealmResults<Message>> getLastMessages();

    int getUserPref();

    Realm getRealm();
}
