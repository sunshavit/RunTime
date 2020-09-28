package com.example.runtime;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditProfileVM extends ViewModel implements DataBaseClass.OnGetUserImage {

    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    private UserInstance userInstance = UserInstance.getInstance();
    private MutableLiveData<String> liveDataImage = new MutableLiveData<>();


    public EditProfileVM() {
        dataBaseClass.setCallBackGetImage(this);
    }

    public void getImageFromData(){
        dataBaseClass.getImage();
    }

    public MutableLiveData<String> getImageLivedata() {
        return liveDataImage;
    }

    public void saveUserImageEdit(Uri uri){
        dataBaseClass.saveProfilePicture(uri);
    }

    public void saveUserEdit(){
        dataBaseClass.changeUser(userInstance.getUser());
    }

    @Override
    public void onSuccessGetImage(String uri) {
        liveDataImage.setValue(uri);
    }

    @Override
    public void onFailedGetImage() {

    }
}
