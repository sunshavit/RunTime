package com.example.runtime;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditProfileVM extends ViewModel implements DataBaseClass.OnGetUserImage,DataBaseClass.OnSaveImageListener {

    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    private UserInstance userInstance = UserInstance.getInstance();
    private MutableLiveData<String> liveDataImage = new MutableLiveData<>();
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public EditProfileVM() {
        dataBaseClass.setCallBackGetImage(this);
        dataBaseClass.setCallBackImage(this);

    }

    public MutableLiveData<User> getUserLiveData(){
        return userLiveData;
    }

    public void setUserLiveData(User user){
        userLiveData.setValue(user);
    }

    public void getImageFromData(){
        dataBaseClass.getImage();
    }

    public MutableLiveData<String> getImageLivedata() {
        return liveDataImage;
    }

    public void saveUserImageEdit(Uri uri){
        dataBaseClass.saveProfilePicture(uri);
        dataBaseClass.changeUser(userInstance.getUser());
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

    @Override
    public void onSuccessImage() {

    }

    @Override
    public void onFailedImage() {

    }

}
