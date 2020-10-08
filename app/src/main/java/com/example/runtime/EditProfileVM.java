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
    private MutableLiveData<String> liveDataDate = new MutableLiveData<>();
    private MutableLiveData<String > liveDataName = new MutableLiveData<>();


    public EditProfileVM() {
        dataBaseClass.setCallBackGetImage(this);
        dataBaseClass.setCallBackImage(this);
        liveDataName.setValue(UserInstance.getInstance().getUser().getFullName());
        liveDataDate.setValue(userInstance.getUser().getDayOfMonth()+"."+userInstance.getUser().getMonth()+"."+userInstance.getUser().getYear());
    }

    public void setDate(String date){
        liveDataDate.setValue(date);
    }

    public LiveData<String> getLiveDataDate() {
        return liveDataDate;
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
        Log.d("sun",uri);
    }

    @Override
    public void onFailedGetImage() {
    }

    @Override
    public void onSuccessImage() {
        //add prograss bar
    }

    @Override
    public void onFailedImage() {

    }

    public void setName(String name){
        liveDataName.setValue(name);
    }

    public MutableLiveData<String> getLiveDataName() {
        return liveDataName;
    }
}
