package com.strukov.qchat.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.strukov.qchat.api.QChatApi;
import com.strukov.qchat.di.QChatApp;
import com.strukov.qchat.models.Friend;
import com.strukov.qchat.models.Message;
import com.strukov.qchat.models.Messages;
import com.strukov.qchat.models.User;
import com.strukov.qchat.utils.rx.RxSchedulers;

import java.util.UUID;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Matthew on 29.12.2017.
 */

public class UpdateMessagesIntentService extends IntentService {

    @Inject
    QChatApi api;

    @Inject
    RxSchedulers rxSchedulers;

    public UpdateMessagesIntentService() {
        super("UpdateMessagesIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        QChatApp.getApp(this).getAppComponent().injectUpdateMessagesIntentService(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int userId = intent.getIntExtra("userId", 0);
        api.getMessages(userId, false).subscribeOn(rxSchedulers.internet())
                .subscribe(messages -> saveMessages(messages, userId));
    }

    private void saveMessages(Messages messages, int userId) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(realmAsync -> {
            User user = realmAsync.where(User.class).equalTo("id", userId).findFirst();
            if (user != null) {
                for (Message message : messages.getMessages()) {
                    message.setOut(true);
                    message.setClient_id(UUID.randomUUID().toString());
                    user.getMessages().add(message);
                    realmAsync.insertOrUpdate(user);
                    isFriend(message, userId);
                }
            }
        });
        realm.close();
    }

    private void isFriend(Message message, int userId) {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("id", userId).findFirst();
        Friend friend = user.getFriends().where().equalTo("friend_id", message.getWriter_id()).findFirst();
        if (friend == null) {
            Friend newFriend = new Friend();
            newFriend.setUser_id(message.getReceiver_id());
            newFriend.setFriend_id(message.getWriter_id());
            newFriend.setDialog(message.getOwner());
            newFriend(newFriend);
        }
        realm.close();
    }

    private void newFriend(Friend friend) {
        api.newFriend(String.valueOf(friend.getUser_id()), friend)
                .subscribeOn(rxSchedulers.internet())
                .subscribe(this::saveFriend);
    }

    private void saveFriend(Friend friend) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(realmAsync -> {
            Friend managedFriend = realmAsync.copyToRealm(friend);
            User user = realmAsync.where(User.class).equalTo("id", friend.getUser_id()).findFirst();
            user.getFriends().add(managedFriend);
        });
        realm.close();
    }
}