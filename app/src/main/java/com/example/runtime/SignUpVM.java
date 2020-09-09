package com.example.runtime;

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





    RegisterClass registerClass = RegisterClass.getInstance();
   // private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase ;
   // private FirebaseStorage firebaseStorage;
  //  boolean isSuccess;


    public boolean setDataNext1(String fullName , String password ,String passwordConfirm , String email) {
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.fullName = fullName;
//        isSuccess=false;
        if (password.equals(passwordConfirm)) {
//            firebaseAuth.createUserWithEmailAndPassword(this.email,this.password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful()){
//                        isSuccess=true;
//                    }
//                }
//            });
//            if (isSuccess)
            registerClass.signUpUser(email, password);
            return true;
//        }
        }
        return false;

    }

    public void setDataNext2(Uri userImage , LocalDate birthDate , String gender , String runningLevel){
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
