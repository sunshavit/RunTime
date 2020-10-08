package com.example.runtime;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InvitationsTabVM extends ViewModel {

    private ArrayList<Event> invitations = new ArrayList<>();
    private ArrayList<String> invitationsIds = new ArrayList<>();
    MutableLiveData<ArrayList<Event>> invitationsLiveData = new MutableLiveData<>();
    DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    RegisterClass registerClass = RegisterClass.getInstance();
    private MutableLiveData<Boolean> swipeLayoutBool = new MutableLiveData<>();

    public InvitationsTabVM() {
        //getInvitationsIds();
    }

    public void getInvitationsIds(){
        swipeLayoutBool.setValue(true);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invitationsIds.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    invitationsIds.add(snapshot1.getKey());
                }
                invitationsIds.remove("false");
                getInvitations();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dataBaseClass.retrieveInvitationsIds(registerClass.getUserId(), listener);
    }

    private void getInvitations() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invitations.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    if (invitationsIds.contains(snapshot1.getKey())){
                        Event event = snapshot1.getValue(Event.class);
                        invitations.add(event);
                    }
                }
                invitationsLiveData.setValue(invitations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dataBaseClass.retrieveAllEvents(listener);
    }

    public MutableLiveData<ArrayList<Event>> getInvitationsLiveData(){
        return invitationsLiveData;
    }

    public void onJoinToEvent(String userId, String eventId){
        dataBaseClass.joinToEvent(userId, eventId);
        getInvitationsIds();
    }

    public void onRemoveEvent(String userId, String eventId){
        dataBaseClass.removeEventFromInvitations(userId, eventId);
        getInvitationsIds();
    }

    public MutableLiveData<Boolean> getSwipeLayoutBool(){
        return swipeLayoutBool;
    }
}
