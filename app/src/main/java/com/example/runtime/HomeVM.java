package com.example.runtime;
import androidx.lifecycle.ViewModel;

import com.example.runtime.DataBaseClass;

public class HomeVM extends ViewModel {
    private User user;

    private DataBaseClass dataBaseClass=DataBaseClass.getInstance();

    public void updateActive(boolean isActive){
        dataBaseClass.updateActive(isActive);
    }





}
