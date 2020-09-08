package com.example.runtime;

import java.util.ArrayList;

public class UserLists {
    private ArrayList<String> myFriends ;
    private ArrayList<String> myEvent ;
    private ArrayList<String> friendsRequests;
    private ArrayList<String> eventRequests;
    private ArrayList<String> managedEvent;

    public UserLists() {
        myFriends = new ArrayList<>();
        myEvent = new ArrayList<>();
        friendsRequests = new ArrayList<>();
        eventRequests = new ArrayList<>();
        managedEvent = new ArrayList<>();
    }



    public ArrayList<String> getMyFriends() {
        return myFriends;
    }

    public void setMyFriends(ArrayList<String> myFriends) {
        this.myFriends = myFriends;
    }

    public ArrayList<String> getMyEvent() {
        return myEvent;
    }

    public void setMyEvent(ArrayList<String> myEvent) {
        this.myEvent = myEvent;
    }

    public ArrayList<String> getFriendsRequests() {
        return friendsRequests;
    }

    public void setFriendsRequests(ArrayList<String> friendsRequests) {
        this.friendsRequests = friendsRequests;
    }

    public ArrayList<String> getEventRequests() {
        return eventRequests;
    }

    public void setEventRequests(ArrayList<String> eventRequests) {
        this.eventRequests = eventRequests;
    }

    public ArrayList<String> getManagedEvent() {
        return managedEvent;
    }

    public void setManagedEvent(ArrayList<String> managedEvent) {
        this.managedEvent = managedEvent;
    }
}
