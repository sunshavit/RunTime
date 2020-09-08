package com.example.runtime;

import android.location.Location;

import java.time.LocalDate;
import java.util.Date;

public class User {
    private String fullName;
    private String gender;
    private LocalDate birthDate;
    private String runningLevel;
    private Location location;
    private boolean isOnline;
    private String profileImage;


    public User() {
    }

    public User(String fullName, String gender, LocalDate birthDate, String runningLevel, Location location, boolean isOnline, String profileImage) {
        this.fullName = fullName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.runningLevel = runningLevel;
        this.location = location;
        this.isOnline = isOnline;
        this.profileImage = profileImage;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getRunningLevel() {
        return runningLevel;
    }

    public void setRunningLevel(String runningLevel) {
        this.runningLevel = runningLevel;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
