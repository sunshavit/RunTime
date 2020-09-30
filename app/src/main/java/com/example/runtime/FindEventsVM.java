package com.example.runtime;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindEventsVM extends AndroidViewModel {

    MutableLiveData<ArrayList<Event>> relevantEvents = new MutableLiveData<>();
    private ArrayList<Event> eventsFromDatabase = new ArrayList<>();
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    ArrayList<Event> relevant = new ArrayList<>();
    private User currentUser;

    public FindEventsVM(@NonNull Application application) {
        super(application);
        retrieveEventsList();
    }

    //distance calculation
    private double haversine(double lat1, double lon1,
                             double lat2, double lon2)
    {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c; //distance in km
    }


    public MutableLiveData<ArrayList<Event>> getRelevantEvents(){
        return relevantEvents;
    }

    public void retrieveEventsList(){
        getAllEventsList();
    }

    private void getAllEventsList(){

        eventsFromDatabase.clear();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        Event event = snapshot1.getValue(Event.class);
                        eventsFromDatabase.add(event);
                    }
                    getCurrentUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dataBaseClass.retrieveAllEventsList(listener);

    }

    private void getCurrentUser() {
        currentUser = UserInstance.getInstance().getUser();
        findRelevantEvents();
    }

    private void findRelevantEvents(){

        double longitude = currentUser.getLongitude();
        double latitude = currentUser.getLatitude();

        for (Event event: eventsFromDatabase) {

            double distance = haversine(event.getLatitude(), event.getLongitude(), latitude, longitude);

            if(distance < 20 && event.getEventStatus().equals("publicEvent") && !event.getManager().equals(currentUser.getUserId())){
                relevant.add(event);
            }
        }

        relevantEvents.setValue(relevant);

    }

    public void onJoinEvent(String eventId,String userId) {
        dataBaseClass.addEventToMyEventsList(eventId,userId);
    }

    public void onCancelJoinEvent(String eventId,String userId) {
        dataBaseClass.removeEventFromMyEventsList(eventId,userId);
    }
}

