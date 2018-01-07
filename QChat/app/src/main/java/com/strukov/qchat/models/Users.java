package com.strukov.qchat.models;

import java.util.List;

/**
 * Created by Matthew on 24.12.2017.
 */

public class Users {
    private List<User> users;

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {

        return users;
    }
}
