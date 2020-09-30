package com.example.runtime;

import android.net.sip.SipSession;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CreateEventVM extends ViewModel {

    private int eventYear;
    private int eventMonth;
    private int eventDayOfMonth;
    private int eventHourOfDay;
    private int eventMinute;
    private double longitude;
    private double latitude;
    private String runningLevel;
    private String eventStatus;
    private ArrayList<String> runners;
    private UserInstance userInstance = UserInstance.getInstance();
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();

    private String eventDate;
    private String eventTime;
    private int easyImageView;
    private int mediumImageView;
    private int hardImageView;
    private boolean isFirstLaunch=true;
    private int easyTextView;
    private int mediumTextView;
    private int hardTextView;
    private boolean isPublicChecked;
    private boolean isPrivateChecked;

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


    public void setEventData ( int eventYear, int eventMonth, int eventDayOfMonth, int eventHourOfDay, int eventMinute, String runningLevel, String eventStatus) {
        this.eventYear = eventYear;
        this.eventMonth = eventMonth;
        this.eventDayOfMonth = eventDayOfMonth;
        this.eventHourOfDay = eventHourOfDay;
        this.eventMinute = eventMinute;
        this.runningLevel = runningLevel;
        this.eventStatus = eventStatus;

        Event event = new Event(eventYear,eventMonth,eventDayOfMonth,eventHourOfDay,eventMinute,longitude,latitude,runningLevel,eventStatus);
        String userId = userInstance.getUser().getUserId();
        dataBaseClass.createNewEvent(event,userId);

    }

    public void setEventDate(String eventDate){
        this.eventDate = eventDate;
    }

    public String getEventDate(){
        return eventDate;
    }

    public void setEventTime(String eventTime){
        this.eventTime = eventTime;
    }

    public String getEventTime(){
        return  eventTime;
    }

    public void setEasyImageView(int easyImageView){
        this.easyImageView = easyImageView;
    }

    public int getEasyImageView(){
        return easyImageView;
    }

    public void setMediumImageView(int mediumImageView){
        this.mediumImageView = mediumImageView;
    }

    public int getMediumImageView(){
        return mediumImageView;
    }

    public void setHardImageView(int hardImageView){
        this.hardImageView = hardImageView;
    }

    public int getHardImageView(){
        return hardImageView;
    }

    public void setIsFirstLaunch(boolean isFirstLaunch){
        this.isFirstLaunch = isFirstLaunch;
    }

    public boolean getIsFirstLaunch(){
        return isFirstLaunch;
    }

    public int getEasyTextView() {
        return easyTextView;
    }

    public void setEasyTextView(int easyTextView) {
        this.easyTextView = easyTextView;
    }

    public int getMediumTextView() {
        return mediumTextView;
    }

    public void setMediumTextView(int mediumTextView) {
        this.mediumTextView = mediumTextView;
    }

    public int getHardTextView() {
        return hardTextView;
    }

    public void setHardTextView(int hardTextView) {
        this.hardTextView = hardTextView;
    }

    public boolean isPublicChecked() {
        return isPublicChecked;
    }

    public void setPublicChecked(boolean publicChecked) {
        isPublicChecked = publicChecked;
    }

    public boolean isPrivateChecked() {
        return isPrivateChecked;
    }

    public void setPrivateChecked(boolean privateChecked) {
        isPrivateChecked = privateChecked;
    }
}
