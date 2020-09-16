package com.example.runtime;

import android.location.Location;

import java.sql.Time;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


public class Event {
    private String eventId;
    private int year;
    private int month;
    private int dayOfMonth;
    private int hourOfDay;
    private int minute;
    private Location location;
    private String manager;
    private String runningLevel;
    private ArrayList<String> runners;

    public Event() {
    }

    public Event(String eventId, int year, int month, int dayOfMonth, int hourOfDay, int minute, Location location, String manager, String runningLevel, ArrayList<String> runners) {
        this.eventId = eventId;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.location = location;
        this.manager = manager;
        this.runningLevel = runningLevel;
        this.runners = new ArrayList<>();

        runners.add("false");
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public ArrayList<String> getRunners() {
        return runners;
    }

    public void setRunners(ArrayList<String> runners) {
        this.runners = runners;
    }
}
