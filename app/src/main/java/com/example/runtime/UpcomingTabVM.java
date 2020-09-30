package com.example.runtime;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpcomingTabVM extends ViewModel {

    private ArrayList<Event> upcomingEvents = new ArrayList<>();
    private ArrayList<String> upcomingEventsIds = new ArrayList<>();
    private MutableLiveData<ArrayList<Event>> upcomingEventsLiveData = new MutableLiveData<>();
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    private RegisterClass registerClass = RegisterClass.getInstance();
    private MutableLiveData<Boolean> swipeLayoutBool = new MutableLiveData<>();

    public UpcomingTabVM() {

        getUpcomingEventsIds();
    }

    public void getUpcomingEventsIds(){
        swipeLayoutBool.setValue(true);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                upcomingEventsIds.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    upcomingEventsIds.add(snapshot1.getKey());
                }
                upcomingEventsIds.remove("false");
                getUpcomingEvents();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dataBaseClass.retrieveUpcomingEventsIds(registerClass.getUserId(), listener);

    }

    private void getUpcomingEvents() {

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                upcomingEvents.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    if (upcomingEventsIds.contains(snapshot1.getKey())){
                        Event event = snapshot1.getValue(Event.class);
                        upcomingEvents.add(event);
                    }
                }
                upcomingEventsLiveData.setValue(upcomingEvents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dataBaseClass.retrieveAllEvents(listener);

    }

    public MutableLiveData<ArrayList<Event>> getUpcomingEventsLiveData(){
        return upcomingEventsLiveData;
    }

    public MutableLiveData<Boolean> getSwipeLayoutBool(){
        return swipeLayoutBool;
    }

    public void RemoveUpcomingEvent(String userId, String eventId){
        dataBaseClass.removeUpcomingEvent(userId,eventId);
        getUpcomingEventsIds();
    }
}
