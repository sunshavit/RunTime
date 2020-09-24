package com.example.runtime;

import android.location.Location;

import java.sql.Time;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;


public class Event {
    private String eventId;
    private int year;
    private int month;
    private int dayOfMonth;
    private int hourOfDay;
    private int minute;
    private double longitude;
    private double latitude;
    private String manager;
    private String runningLevel;
    private HashMap<String,Boolean> runners = new HashMap<>();

    public Event() {
    }


    public Event( int year, int month, int dayOfMonth, int hourOfDay, int minute, double longitude,double latitude, String manager, String runningLevel) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.longitude = longitude;
        this.latitude = latitude;
        this.manager = manager;
        this.runningLevel = runningLevel;

        runners.put("false",false);
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getRunningLevel() {
        return runningLevel;
    }

    public void setRunningLevel(String runningLevel) {
        this.runningLevel = runningLevel;
    }

    public HashMap<String, Boolean> getRunners() {
        return runners;
    }

    public void setRunners(HashMap<String, Boolean> runners) {
        this.runners = runners;
    }
}
