package com.example.runtime;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessagesVM extends ViewModel {
    private MutableLiveData<ArrayList<User>> friends = new MutableLiveData<>();
    private ArrayList<String> usersIdFromDatabase = new ArrayList<>();
    private ArrayList<User> usersFromDatabase = new ArrayList<>();
    private DataBaseClass dataBaseClass;

    public MessagesVM() {
        this.dataBaseClass = DataBaseClass.getInstance();
        getFriendsId();
    }

    private void getFriendsId() {
        usersIdFromDatabase.clear();
        usersFromDatabase.clear();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String userId = snapshot1.getKey();
                        Log.d("sun", "inside first listener");
                        Log.d("sun",userId );
                        usersIdFromDatabase.add(userId);
                        getFriends(userId);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dataBaseClass.retrieveAllFriends(valueEventListener);

    }

    private void getFriends(String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                usersFromDatabase.add(user);
                friends.setValue(usersFromDatabase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
            dataBaseClass.getUserWithId(valueEventListener,id);
    }

    public MutableLiveData<ArrayList<User>> getFriends() {
        return friends;
    }
}
