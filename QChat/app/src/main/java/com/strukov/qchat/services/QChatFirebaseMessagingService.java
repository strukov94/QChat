package com.strukov.qchat.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.strukov.qchat.api.QChatApi;
import com.strukov.qchat.di.QChatApp;
import com.strukov.qchat.messaging.MessagingActivity;
import com.strukov.qchat.models.Friend;
import com.strukov.qchat.models.Message;
import com.strukov.qchat.models.User;
import com.strukov.qchat.utils.notification.NotificationUtils;
import com.strukov.qchat.utils.rx.RxSchedulers;

import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Matthew on 23.12.2017.
 */

public class QChatFirebaseMessagingService extends FirebaseMessagingService {

    @Inject
    QChatApi api;

    @Inject
    RxSchedulers schedulers;

    @Inject
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        QChatApp.getApp(this).getAppComponent().injectFirebaseMessagingService(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            Message message = new Message();
            message.setClient_id(UUID.randomUUID().toString());
            message.setMessage_id(Long.parseLong(data.get("message_id")));
            message.setMessage(data.get("message"));
            message.setDate(Long.parseLong(data.get("date")));
            message.setWriter_id(Integer.parseInt(data.get("writer_id")));
            message.setReceiver_id(Integer.parseInt(data.get("receiver_id")));
            message.setIs_sent(Boolean.parseBoolean(data.get("is_sent")));
            message.setIs_read(Boolean.parseBoolean(data.get("is_read")));
            message.setOut(true);
            message.setOwner(data.get("owner"));

            if (message.getReceiver_id() == message.getWriter_id()) {
                message.setDate(message.getDate() + 1);
            }

            isFriend(message);
            saveMessage(message);
            setNotification(message);
        }
    }

    private void setNotification(Message message){
        Realm realm = Realm.getDefaultInstance();
        Friend friend = realm.where(Friend.class).equalTo("friend_id", message.getWriter_id()).findFirst();
        Intent intent = new Intent(context, MessagingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", friend.getFriend_id());
        intent.putExtra("bundle", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, friend.getFriend_id(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationUtils.setNotification(this, friend.getImage(), friend.getName(), message.getDate(),
                message.getMessage(), friend.getFriend_id(), pendingIntent);
    }

    private void isFriend(Message message) {
        Realm realm = Realm.getDefaultInstance();
        Friend friend = realm.where(Friend.class).equalTo("friend_id", message.getWriter_id()).findFirst();
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
                .subscribeOn(schedulers.internet())
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

    private void saveMessage(Message message) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(realmAsync -> {
            Message managedMessage = realmAsync.copyToRealm(message);
            User user = realmAsync.where(User.class).equalTo("id", message.getReceiver_id()).findFirst();
            user.getMessages().add(managedMessage);
        });
        realm.close();
    }
}
