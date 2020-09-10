package com.example.runtime;

import android.location.Location;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.time.LocalDate;
import java.util.Date;

public class SignUpVM extends ViewModel  {
    private String fullName;
    private String email;
    private String password;
    private String passwordConfirm;
    private LocalDate birthDate;
    private String gender;
    private String runningLevel;
    private int startAge;
    private int endAge;
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

    public void setDataNext2(Uri userImage , LocalDate birthDate , String gender , String runningLevel){
        this.userImage = userImage;
        this.birthDate = birthDate;
        this.gender = gender;
        this.runningLevel = runningLevel;
        dataBaseClass.createUser(new User(this.fullName,this.gender,this.birthDate,this.runningLevel,true));



    }

    public void setDataNext3(int startAge , int endAge , String partnerGender , String partnerLevel){
        this.startAge = startAge;
        this.endAge = endAge;
        this.partnerGender = partnerGender;
        this.partnerLevel = partnerLevel;

        UserPreferences userPreferences=new UserPreferences(startAge,endAge,partnerGender,partnerLevel);
        dataBaseClass.createPreferences(userPreferences);

    }


}
