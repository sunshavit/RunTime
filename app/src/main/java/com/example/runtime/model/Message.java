package com.example.runtime.model;

import java.util.Objects;

public class Message {
    private String content;
    private String time;
    private int id;
    private String userIdSent;

    public Message() {

    }

    public Message(String content, String time, int id,String userIdSent) {
        this.content = content;
        this.time = time;
        this.id = id;
        this.userIdSent = userIdSent;

    }

    public String getUserIdSent() {
        return userIdSent;
    }

    public void setUserIdSent(String userIdSent) {
        this.userIdSent = userIdSent;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
