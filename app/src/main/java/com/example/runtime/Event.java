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
    private LocalDate date;
    private LocalTime startTime;
    private Location location;
    private String manager;
    private String runningLevel;
    private ArrayList<String> runners;

    public Event() {
    }

    public Event(LocalDate date, LocalTime startTime, Location location, String manager, String runningLevel) {
        this.date = date;
        this.startTime = startTime;
        this.location = location;
        this.manager = manager;
        this.runningLevel = runningLevel;
        this.runners = new ArrayList<>();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
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
