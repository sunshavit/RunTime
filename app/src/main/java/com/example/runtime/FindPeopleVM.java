package com.example.runtime;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FindPeopleVM extends ViewModel {

    MutableLiveData<ArrayList<User>> relevantUsers = new MutableLiveData<>();
    private User currentUser;
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    private ArrayList<User> usersFromDatabase = new ArrayList<>();
    UserPreferences userPreferences;




    public void retrieveUsersList(){

        getAllUsersList();

    }

    //distance calculation
    private double haversine(double lat1, double lon1,
                            double lat2, double lon2)
    {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c; //distance in km
    }

    //age calculation
    private int getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = Integer.valueOf(age);
        //String ageS = ageInt.toString();

        return ageInt;
    }


    public MutableLiveData<ArrayList<User>> getRelevantUsers(){
        //returning relevantUsersList
        return relevantUsers;
    }

    public String getLocationGeoCode(){
        //retrieve location and geocode it
        return null;
    }


    //trying

    private void getAllUsersList(){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        User user = snapshot1.getValue(User.class);
                        Log.d("tag", "inside first listener");
                        if(user != null){
                            Log.d("tag", user.toString());
                            Log.d("tag", user.getFullName());
                        }

                        usersFromDatabase.add(user);

                    }
                    Log.d("tag", usersFromDatabase.get(3).getGender());
                    Log.d("tag", usersFromDatabase.toString());
                    getUserPreferences();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        //database method that gets this listener
        dataBaseClass.retrieveAllUsersList(listener);

    }

    private void getUserPreferences() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    userPreferences = snapshot.getValue(UserPreferences.class);
                    Log.d("tag", "inside second listener");
                    getCurrentUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        //database method that gets this listener
        dataBaseClass.retrieveUserPreferences(listener);
    }

    private void getCurrentUser() {

        currentUser = UserInstance.getInstance().getUser();
        findRelevantUsers();
    }

        //use sun's class
        //dataBaseClass.retrieveUserDetails(listener);



    private void findRelevantUsers() {
        Log.d("tag", "inside final function");
        Log.d("tag", userPreferences.getRuningLevel());
        Log.d("tag", currentUser.getFullName());
        Log.d("tag", usersFromDatabase.get(2).getFullName());
        Log.d("tag", usersFromDatabase.get(3).getFullName());
        //user location
        double longitude = currentUser.getLongitude();
        double latitude = currentUser.getLatitude();
        //user preferences
        String preferredGender = userPreferences.getGender();
        String preferredLevel = userPreferences.getRuningLevel();
        int preferredFromAge = userPreferences.getFromAge();
        int preferredToAge = userPreferences.getToAge();

        //choosing only relevant users from the list
        ArrayList<User> relevant = new ArrayList<>();

        for (User user : usersFromDatabase){

            int age = getAge(user.getYear(), user.getMonth(), user.getDayOfMonth());
            double distance = haversine(user.getLatitude(), user.getLongitude(), longitude, latitude);

            if(user.getGender().equals(preferredGender)
                    //&&
                   // user.getRunningLevel().equals(preferredLevel)&&
                  //  distance < 1 && age >= preferredFromAge && age <= preferredToAge
                      ){
                relevant.add(user);
            }
        }

        Log.d("tag", "relevant" + relevant.get(0).getFullName());

        relevantUsers.setValue(relevant);
    }


}
