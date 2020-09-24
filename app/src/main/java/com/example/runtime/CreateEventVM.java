package com.example.runtime;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CreateEventVM extends ViewModel {

    private String eventId;
    private int eventYear;
    private int eventMonth;
    private int eventDayOfMonth;
    private int eventHourOfDay;
    private int eventMinute;
    private double longitude;
    private double latitude;
    private String manager;
    private String runningLevel;
    private ArrayList<String> runners;
    private UserInstance userInstance = UserInstance.getInstance();
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();

    MutableLiveData<String> streetAddress = new MutableLiveData<>();

    public MutableLiveData<String> getStreetAddress(){
        return streetAddress;
    }

    public void setStreetAddress(String address){
        streetAddress.setValue(address);
    }

    public void setLongitudeLatitude (double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public void setEventData ( int eventYear, int eventMonth, int eventDayOfMonth, int eventHourOfDay, int eventMinute, String manager, String runningLevel) {
        this.eventYear = eventYear;
        this.eventMonth = eventMonth;
        this.eventDayOfMonth = eventDayOfMonth;
        this.eventHourOfDay = eventHourOfDay;
        this.eventMinute = eventMinute;
        this.manager = manager;
        this.runningLevel = runningLevel;

        Event event = new Event(eventYear,eventMonth,eventDayOfMonth,eventHourOfDay,eventMinute,longitude,latitude,manager,runningLevel);
        String userId = userInstance.getUser().getUserId();
        dataBaseClass.createNewEvent(event,userId);

    }
}
