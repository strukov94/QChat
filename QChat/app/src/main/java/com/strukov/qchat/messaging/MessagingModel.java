package com.strukov.qchat.messaging;

import com.strukov.qchat.models.Friend;
import com.strukov.qchat.models.Message;
import com.strukov.qchat.models.User;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.realm.RealmResults;

/**
 * Created by Matthew on 26.12.2017.
 */

public interface MessagingModel {
    void onCloseRealm();

    Observable<Message> newMessage(String user, Message message);

    Observable<Boolean> isFriend(int userId);

    Flowable<Friend> getFriend(int userId);

    Message setMessage(String text, int receiverId);

    void updateMessageStatus(Message message);

    Flowable<User> getUserForMessages();

    Flowable<RealmResults<Message>> getMessages(User user, int userId);

    int getUserPref();

    Observable<Friend> newFriend(int friendId);

    void saveFriend(Friend friend);
}
