package com.example.runtime;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagedTabVM extends ViewModel {

    private ArrayList<Event> managedEvents = new ArrayList<>();
    private ArrayList<String> managedEventsIds = new ArrayList<>();
    MutableLiveData<ArrayList<Event>> managedEventsLiveData = new MutableLiveData<>();
    private RegisterClass registerClass = RegisterClass.getInstance();
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    private MutableLiveData<Boolean> swipeLayoutBool = new MutableLiveData<>();

    public ManagedTabVM() {
        getManagedEventsIds();
    }

    public void getManagedEventsIds(){
        swipeLayoutBool.setValue(true);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                managedEventsIds.clear();
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        managedEventsIds.add(snapshot1.getKey());
                    }
                    managedEventsIds.remove("false");
                    getManagedEvents();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dataBaseClass.retrieveManagedEventsIds(registerClass.getUserId(), listener);
        //database call here
    }

    private void getManagedEvents() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                managedEvents.clear();
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        if (managedEventsIds.contains(snapshot1.getKey())){
                            Event event = snapshot1.getValue(Event.class);
                            managedEvents.add(event);
                        }
                    }
                    managedEventsLiveData.setValue(managedEvents);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dataBaseClass.retrieveAllEvents(listener);

    }

    public MutableLiveData<ArrayList<Event>> getManagedEventsLiveData(){
        return managedEventsLiveData;
    }

    public MutableLiveData<Boolean> getSwipeLayoutBool(){
        return swipeLayoutBool;
    }
}
