package com.example.runtime;

import android.content.Context;
import android.location.Geocoder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.Locale;

public class ProfileVM extends ViewModel implements DataBaseClass.OnGetUserImage {
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    private MutableLiveData<String> liveDataImage = new MutableLiveData<>();
    private MutableLiveData<User> liveDataUser = new MutableLiveData<>();
    private UserInstance userInstance;

    public ProfileVM() {
        dataBaseClass.setCallBackGetImage(this);
        this.userInstance = UserInstance.getInstance();
        liveDataUser.setValue(userInstance.getUser());
    }

    public MutableLiveData<User> getLiveDataUser() {
        return liveDataUser;
    }

    public User getUser() {
        return userInstance.getUser();
    }

    public String getAddress(Context context, double latitude, double longitude){
        Geocoder geocoder = new Geocoder(context , Locale.getDefault());
        try {
            return geocoder.getFromLocation(latitude,longitude,1).get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void getImageFromData(){
        dataBaseClass.getImage();
    }

    public MutableLiveData<String> getImageLivedata() {
        return liveDataImage;
    }

    @Override
    public void onSuccessGetImage(String uri) {
        Log.d("sun",uri);
        liveDataImage.setValue(uri);
    }

    @Override
    public void onFailedGetImage() {
    }
}
