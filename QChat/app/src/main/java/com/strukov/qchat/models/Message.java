package com.strukov.qchat.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Matthew on 18.12.2017.
 */

public class Message extends RealmObject {
    @PrimaryKey
    private String client_id;
    private long message_id;
    private String message;
    private long date;
    private int writer_id;
    private int receiver_id;
    private boolean is_sent;
    private boolean is_read;
    private boolean out;
    private String owner;

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {

        return owner;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getMessage() {
        return message;
    }

    public long getDate() {
        return date;
    }

    public int getWriter_id() {
        return writer_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setWriter_id(int writer_id) {
        this.writer_id = writer_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public int getReceiver_id() {

        return receiver_id;
    }

    public void setIs_sent(boolean is_sent) {
        this.is_sent = is_sent;
    }

    public boolean isIs_sent() {

        return is_sent;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public boolean isIs_read() {

        return is_read;
    }

    public long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(long message_id) {
        this.message_id = message_id;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }
}
