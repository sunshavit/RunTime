package com.example.runtime;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsTabVM extends ViewModel {

    ArrayList<User> friends = new ArrayList<>();
    ArrayList<String> friendsIds = new ArrayList<>();
    MutableLiveData<ArrayList<User>> friendsLiveData = new MutableLiveData<>();

    DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    RegisterClass registerClass = RegisterClass.getInstance();

    private MutableLiveData<Boolean> swipeLayoutBool = new MutableLiveData<>();

    public FriendsTabVM() {
        getFriendsIds();
    }

    public void getFriendsIds (){
        swipeLayoutBool.setValue(true);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendsIds.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Log.d("friendId", snapshot1.getKey());
                    friendsIds.add(snapshot1.getKey());
                }
                friendsIds.remove("false");
                Log.d("friendId", "num of friends ids" + friendsIds.size());
                getFriendsUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dataBaseClass.retrieveFriendsIds(registerClass.getUserId(), listener);

    }

    private void getFriendsUsers() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friends.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    if (friendsIds.contains(snapshot1.getKey())){
                        User user = snapshot1.getValue(User.class);
                        friends.add(user);
                    }
                }
                Log.d("friendId", "num of friends" + friends.size());
                friendsLiveData.setValue(friends);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dataBaseClass.retrieveAllUsersList(listener);
    }

    public MutableLiveData<ArrayList<User>> getFriendsLiveData(){
        Log.d("friendId", "mutable" + friendsLiveData);
        return friendsLiveData;
    }

    public void removeFriend(String userId, String friendId){
        dataBaseClass.removeFriend(userId, friendId);
        getFriendsIds();
    }

    public MutableLiveData<Boolean> getSwipeLayoutBool(){
        return swipeLayoutBool;
    }

}
