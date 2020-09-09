package com.example.runtime;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class DataBaseClass {
    private FirebaseDatabase firebaseDatabase;
    private RegisterClass registerClass;
    private FirebaseStorage firebaseStorage;
    private final String USERSTABLE = "users";
    private DatabaseReference databaseReference;

    private static DataBaseClass dataBaseClass = null;

    interface OnUserCreateListener {
        void onSuccessCreate();
        void onFailedCreate(String problem);
    }

    private OnUserCreateListener callBackCreate;


    private DataBaseClass(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        registerClass = RegisterClass.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    static DataBaseClass getInstance(){
        if(dataBaseClass == null){
            dataBaseClass=new DataBaseClass();
        }
        return dataBaseClass;
    }

    public void createUser(User user){
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference databaseReference1 = databaseReference.child("user");
        databaseReference1.child(registerClass.getUserId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    callBackCreate.onSuccessCreate();
                else
                    callBackCreate.onFailedCreate(task.getException().getMessage());
            }
        });
    }

    public void setCallBackCreate(OnUserCreateListener callBackCreate) {
        this.callBackCreate = callBackCreate;
    }
}
