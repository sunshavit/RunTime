package com.example.runtime;
import android.content.Context;
import android.location.Geocoder;

import androidx.lifecycle.ViewModel;

import com.example.runtime.DataBaseClass;

import java.io.IOException;
import java.util.Locale;

public class HomeVM extends ViewModel {


    private DataBaseClass dataBaseClass=DataBaseClass.getInstance();

    public void updateActive(boolean isActive){
        dataBaseClass.updateActive(isActive);
    }

    public String getAddress(Context context,double latitude,double longitude){
        Geocoder geocoder = new Geocoder(context , Locale.getDefault());
        try {
            return geocoder.getFromLocation(latitude,longitude,1).get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
     return null;
    }



}
