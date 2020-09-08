package com.example.runtime;

import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.Date;

public class SignUpVM extends ViewModel {
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
    private String userImage;


    public boolean setDataNext1(String fullName , String password ,String passwordConfirm , String email){
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.fullName = fullName;
        if(password.equals(passwordConfirm))
            return true;
        return false;
    }

    public void setDataNext2(String userImage , LocalDate birthDate , String gender , String runningLevel){
        this.userImage = userImage;
        this.birthDate = birthDate;
        this.gender = gender;
        this.runningLevel = runningLevel;
    }

    public void setDataNext3(int startAge , int endAge , String partnerGender , String partnerLevel){
        this.startAge = startAge;
        this.endAge = endAge;
        this.partnerGender = partnerGender;
        this.partnerLevel = partnerLevel;
    }





}
