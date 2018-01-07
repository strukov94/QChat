package com.strukov.qchat.models;

import java.util.List;

/**
 * Created by Matthew on 26.12.2017.
 */

public class Messages {
    private List<Message> messages;

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getMessages() {

        return messages;
    }
}
