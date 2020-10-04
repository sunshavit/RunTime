package com.example.runtime.model;

import java.util.Comparator;

public class SortFriendMessage implements Comparator<UserWithLastMessage> {
    @Override
    public int compare(UserWithLastMessage o1, UserWithLastMessage o2) {
        return o2.message.getTime().compareTo(o1.message.getTime());
    }
}
