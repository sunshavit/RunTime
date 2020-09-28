package com.example.runtime.model;

import java.util.Objects;

public class Message {
    private String content;
    private String time;

    public Message() {

    }

    public Message(String content, String time) {
        this.content = content;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return content.equals(message.content) &&
                time.equals(message.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, time);
    }
}
