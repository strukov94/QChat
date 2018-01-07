package com.strukov.qchat.models;

import io.realm.RealmObject;

/**
 * Created by Matthew on 26.12.2017.
 */

public class Friend extends RealmObject {
    private int id;
    private int friend_id;
    private int user_id;
    private String name;
    private String image;
    private String user_name;
    private String dialog;

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public String getDialog() {

        return dialog;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getImage() {

        return image;
    }

    public String getName() {

        return name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {

        return user_name;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setFriend_id(int friend_id) {

        this.friend_id = friend_id;
    }

    public int getUser_id() {

        return user_id;
    }
}
