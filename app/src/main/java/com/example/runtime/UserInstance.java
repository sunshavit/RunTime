package com.example.runtime;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class UserInstance {

    interface OnGetUserListener{
        void onGetUser();
    }

    private UserInstance.OnGetUserListener callBackUserGet;
    private static UserInstance userInstance = null;
    private User user;
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            User userNew;
            Log.d("sun","sunsun");
            userNew = snapshot.getValue(User.class);
            user = userNew;
            callBackUserGet.onGetUser();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };



    private UserInstance(){
        dataBaseClass.getUser(valueEventListener);
    }

    public static UserInstance getInstance()
    {
        if (userInstance == null)
            userInstance = new UserInstance();

        return userInstance;
    }

    public void getUserFromDataBase(){
        dataBaseClass.getUser(valueEventListener);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCallBackUserGet(UserInstance.OnGetUserListener callBackUserGet) {
        this.callBackUserGet = callBackUserGet;
    }
}
