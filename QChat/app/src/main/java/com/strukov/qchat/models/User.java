package com.strukov.qchat.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Matthew on 18.12.2017.
 */

public class User extends RealmObject {
    @PrimaryKey
    private String token_auth;
    private int id;
    private String eMail;
    private String token_fcm;
    private String name;
    private String user_name;
    private String image;
    private RealmList<Message> messages;
    private RealmList<Friend> friends;

    public String getToken_fcm() {
        return token_fcm;
    }

    public String getName() {
        return name;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getEmail() {
        return eMail;
    }

    public String getImage() {
        return image;
    }

    public RealmList<Message> getMessages() {
        return messages;
    }

    public void setToken_fcm(String token_fcm) {
        this.token_fcm = token_fcm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setEmail(String eMail) {
        this.eMail = eMail;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMessages(RealmList<Message> messages) {
        this.messages = messages;
    }

    public String getToken_auth() {
        return token_auth;
    }

    public void setToken_auth(String token_auth) {
        this.token_auth = token_auth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFriends(RealmList<Friend> friends) {
        this.friends = friends;
    }

    public RealmList<Friend> getFriends() {

        return friends;
    }
}
