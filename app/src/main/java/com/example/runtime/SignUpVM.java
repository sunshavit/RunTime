package com.example.runtime;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;

public class SignUpVM extends ViewModel  {
    private String fullName;
    private String email;
    private String password;
    private String passwordConfirm;
    private String gender;
    private String runningLevel;
    private int startAge;
    private int endAge;
    private int year;
    private int month;
    private int dayOfMonth;
    private String partnerLevel;
    private String partnerGender;
    private Uri userImage;
    private RegisterClass registerClass = RegisterClass.getInstance();
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();



    public boolean setDataNext1(String fullName , String password ,String passwordConfirm , String email) {
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.fullName = fullName;
        if (password.equals(passwordConfirm)) {

            registerClass.signUpUser(email, password);
            return true;
        }
        return false;

    }




    public void setDataNext2(Uri userImage ,int year,int month,int dayOfMonth, String gender , String runningLevel){
        this.userImage = userImage;
        this.year=year;
        this.month=month;
        this.dayOfMonth=dayOfMonth;
        this.gender = gender;
        this.runningLevel = runningLevel;

        dataBaseClass.createUser(new User(this.fullName,this.gender,this.year,this.month,this.dayOfMonth,this.runningLevel,false,0,0));



    }

    public void setDataNext3(int startAge , int endAge , String partnerGender , String partnerLevel){
        this.startAge = startAge;
        this.endAge = endAge;
        this.partnerGender = partnerGender;
        this.partnerLevel = partnerLevel;

        UserPreferences userPreferences=new UserPreferences(startAge,endAge,partnerGender,partnerLevel);
        UserLists userLists=new UserLists();
        dataBaseClass.createPreferences(userPreferences);
        dataBaseClass.createUserLists(userLists);

    }

    public void saveProfileImage(Uri imageUri){
        dataBaseClass.saveProfilePicture(imageUri);
    }


}
