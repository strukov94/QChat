package com.strukov.qchat.messaging;

import android.content.SharedPreferences;

import com.strukov.qchat.api.QChatApi;
import com.strukov.qchat.models.Friend;
import com.strukov.qchat.models.Message;
import com.strukov.qchat.models.User;

import java.util.UUID;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Matthew on 26.12.2017.
 */

public class MessagingModelImpl implements MessagingModel {
    private QChatApi mApi;
    private Realm mRealm;
    private SharedPreferences mPref;

    public MessagingModelImpl(QChatApi api, Realm realm, SharedPreferences sharedPreferences) {
        mApi = api;
        mRealm = realm;
        mPref = sharedPreferences;
    }

    @Override
    public Observable<Boolean> isFriend(int userId) {
        Friend friend = mRealm.where(User.class).equalTo("id", getUserPref()).findFirst().getFriends()
                .where().equalTo("friend_id", userId).findFirst();
        return Observable.just(friend != null);
    }

    @Override
    public Flowable<Friend> getFriend(int userId) {
        return mRealm.where(User.class).equalTo("id", getUserPref()).findFirst().getFriends()
                .where().equalTo("friend_id", userId).findFirstAsync().asFlowable();
    }

    @Override
    public Observable<Friend> newFriend(int friendId) {
        Friend friend = new Friend();
        friend.setUser_id(getUserPref());
        friend.setFriend_id(friendId);
        friend.setDialog(UUID.randomUUID().toString());
        return mApi.newFriend(String.valueOf(getUserPref()), friend);
    }

    @Override
    public void saveFriend(Friend friend) {
        mRealm.executeTransactionAsync(realm -> {
            Friend managedFriend = realm.copyToRealm(friend);
            User user = realm.where(User.class).equalTo("id", getUserPref()).findFirst();
            user.getFriends().add(managedFriend);
        });
    }

    @Override
    public Observable<Message> newMessage(String user, Message message) {
        return mApi.newMessage(user, message);
    }

    @Override
    public Message setMessage(String text, int receiverId) {
        Friend friend = mRealm.where(User.class).equalTo("id", getUserPref()).findFirst().getFriends()
                .where().equalTo("friend_id", receiverId).findFirst();
        Message message = new Message();
        message.setClient_id(UUID.randomUUID().toString());
        message.setMessage(text);
        message.setDate(getCurrentTime());
        message.setWriter_id(getUserPref());
        message.setReceiver_id(receiverId);
        message.setIs_sent(false);
        message.setIs_read(false);
        message.setOwner(friend.getDialog());
        saveMessage(message);
        return message;
    }

    @Override
    public void updateMessageStatus(Message message) {
        mRealm.executeTransactionAsync(realm -> {
            Message newMessage = realm.where(Message.class).equalTo("client_id", message.getClient_id()).findFirst();
            if (newMessage != null) {
                newMessage.setIs_sent(message.isIs_sent());
                newMessage.setIs_read(message.isIs_read());
                realm.insertOrUpdate(newMessage);
            }
        });
    }

    private void saveMessage(Message message) {
        mRealm.executeTransactionAsync(realm -> {
            Message managedMessage = realm.copyToRealm(message);
            User user = realm.where(User.class).equalTo("id", getUserPref()).findFirst();
            user.getMessages().add(managedMessage);
        });
    }

    @Override
    public Flowable<User> getUserForMessages() {
        return mRealm.where(User.class).equalTo("id", getUserPref()).findFirstAsync().asFlowable();
    }

    @Override
    public Flowable<RealmResults<Message>> getMessages(User user, int userId) {
        return user.getMessages().where().beginGroup().equalTo("writer_id", getUserPref()).and().equalTo("receiver_id", userId).or()
                .equalTo("writer_id", userId).and().equalTo("receiver_id", getUserPref()).endGroup()
                .sort("date", Sort.DESCENDING).findAllAsync().asFlowable();
    }

    @Override
    public int getUserPref() {
        return mPref.getInt("user_id", 0);
    }

    private long getCurrentTime(){
        return System.currentTimeMillis()/1000;
    }

    @Override
    public void onCloseRealm() {
        mRealm.close();
    }
}
