package com.example.runtime;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

public class EditProfileVM extends ViewModel {

    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    private UserInstance userInstance = UserInstance.getInstance();

    public void saveUserEdit(Uri uri){
        dataBaseClass.saveProfilePicture(uri);
        dataBaseClass.createUser(userInstance.getUser());
    }
}
