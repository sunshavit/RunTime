package com.example.runtime;


import android.app.Application;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FindPeopleVM extends AndroidViewModel {

    MutableLiveData<ArrayList<User>> relevantUsers = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> recentSentRequests = new MutableLiveData<>();

    MutableLiveData<String> addressLiveData = new MutableLiveData<>();
    ArrayList<String> recentSentRequestsArrayList = new ArrayList<>();
    ArrayList<User> relevant = new ArrayList<>();

    ArrayList<String> userFriendsIds = new ArrayList<>();
    ArrayList<String> userFriendRequestsIds = new ArrayList<>();
    private User currentUser;
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    private ArrayList<User> usersFromDatabase = new ArrayList<>();
    private UserPreferences userPreferences;
    private final String API_TOKEN_KEY = "AAAAfwvvO64:APA91bG6RWYJYEROIIoBMpzKm6kMdCbqDdqpzhynZ4YnFKEiQ0vu5QuLfJdGTtlixdzqBoL2Ul99A5Mf9kspOh8Whz9U-AY1-7rQTBiOUNUeYZM3UHh4A7Tm4Kb-u4Hrv98zApJn76NQ";

    private MutableLiveData<Boolean> swipeLayoutBool = new MutableLiveData<>();

    public FindPeopleVM(@NonNull Application application) {
        super(application);
        retrieveUsersList();
        getSentRequests();
    }


    public void retrieveUsersList(){

        swipeLayoutBool.setValue(true);
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


    //trying

    private void getAllUsersList(){

        usersFromDatabase.clear();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        User user = snapshot1.getValue(User.class);
                        Log.d("tag", "inside first listener");
                       /* if(user != null){
                         //   Log.d("tag1", user.toString());
                           // Log.d("tag1", user.getFullName());
                        }*/

                        usersFromDatabase.add(user);

                    }
                    //Log.d("tag1", usersFromDatabase.get(0).getGender());
                   // Log.d("tag1", usersFromDatabase.toString());
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
        getFriendsIds();
        getUserAddress();
    }

    private void getUserAddress() {
        double longitude = currentUser.getLongitude();
        double latitude = currentUser.getLatitude();
        Geocoder geocoder = new Geocoder(getApplication().getApplicationContext(), Locale.getDefault());

        String address = "";
        try {
            if (geocoder.getFromLocation(latitude,longitude,1) != null){
                if (geocoder.getFromLocation(latitude,longitude,1).size() > 0){

                   // address = geocoder.getFromLocation(latitude,longitude,1).get(0).getLocality();
                    address = geocoder.getFromLocation(latitude,longitude,1).get(0).getAddressLine(0);

                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        addressLiveData.setValue(address);
    }


    //use sun's class
        //dataBaseClass.retrieveUserDetails(listener);

    private void getFriendsIds(){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userFriendsIds.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){

                    userFriendsIds.add(snapshot1.getKey());
                }
                userFriendsIds.remove("false");
                getFriendRequestsIds();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dataBaseClass.retrieveFriendsIds(currentUser.getUserId(), listener);
    }

    private void getFriendRequestsIds(){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userFriendRequestsIds.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    userFriendRequestsIds.add(snapshot1.getKey());
                }
                userFriendRequestsIds.remove("false");
                findRelevantUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dataBaseClass.retrieveFriendsRequestsIds(currentUser.getUserId(), listener);
    }



    private void findRelevantUsers() {

        relevant.clear();
        Log.d("tag", "inside final function");
        Log.d("tag", userPreferences.getRuningLevel());
        Log.d("tag", currentUser.getFullName());
        Log.d("tag", usersFromDatabase.get(0).getFullName());
        Log.d("tag", usersFromDatabase.get(0).getFullName());
        //user location
        double longitude = currentUser.getLongitude();
        double latitude = currentUser.getLatitude();
        //user preferences
        String preferredGender = userPreferences.getGender();

        Log.d("gender", preferredGender);
        String preferredLevel = userPreferences.getRuningLevel();
        int preferredFromAge = userPreferences.getFromAge();
        int preferredToAge = userPreferences.getToAge();

        //choosing only relevant users from the list
       // ArrayList<User> relevant = new ArrayList<>();

        for (User user : usersFromDatabase){

            int age = getAge(user.getYear(), user.getMonth(), user.getDayOfMonth());
            double distance = haversine(user.getLatitude(), user.getLongitude(), latitude, longitude);
            Log.d("distance", distance+"");

            //also check if user not on friends list already!
            if (!userFriendsIds.contains(user.getUserId()) && !userFriendRequestsIds.contains(user.getUserId())){
                Log.d("user", user.getGender());
                if(user.getGender().equals(preferredGender)
                        && user.getRunningLevel().equals(preferredLevel)
                        && age >= preferredFromAge && age <= preferredToAge
                        && distance < 20
                        && !(user.getUserId().equals(currentUser.getUserId()))
                ) {
                    relevant.add(user);
                    Log.d("distance", "relevant user distance " +distance+"");
                    Log.d("tag2", "inside findRelevant users "+relevant.size()+"");
                }
            }

        }

     //   Log.d("tag", "relevant" + relevant.get(0).getFullName());
        relevantUsers.setValue(relevant);//not here
    }

    public void getSentRequests(){

        recentSentRequestsArrayList.clear();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        recentSentRequestsArrayList.add(snapshot1.getKey());
                        Log.d("snapshot", snapshot1.getKey());
                    }
                }
                Log.d("snapshot", "" +recentSentRequestsArrayList.size());
                updateSentRequestsMutable();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dataBaseClass.retrieveSentRequests(listener);
        //get from database

    }

    private void updateSentRequestsMutable() {
        recentSentRequests.setValue(recentSentRequestsArrayList);
        //notify adapter
    }


    //friend requests
    //updating list of recent requests that can be canceled

    public void onSendFriendRequest(final String strangerId) {//from recycler

        //update firebase
        dataBaseClass.updateSentFriendRequest(strangerId, currentUser.getUserId());
        //send fcm message
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String strangerToken = snapshot.getValue(String.class);
                    try {
                        createFriendRequestNotificationMessage(strangerToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dataBaseClass.retrieveUserToken(strangerId, listener);
    }

    private void createFriendRequestNotificationMessage(String token) throws JSONException {

        Log.d("tag2", "inside create friendRequestNotificationMessage");

        final JSONObject rootObject = new JSONObject();
        rootObject.put("to", token);
        JSONObject data = new JSONObject();
        String friendRequest = getApplication().getString(R.string.new_friend_request);
        String dontKeep = getApplication().getString(R.string.do_not_keep_wait);
        data.put("messageType", "friendRequest");
        data.put("title", friendRequest);
        data.put("body", dontKeep);
        rootObject.put("data", data);

        String url = "https://fcm.googleapis.com/fcm/send";

        RequestQueue queue = Volley.newRequestQueue(getApplication().getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return rootObject.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key=" + API_TOKEN_KEY);
                return headers;
            }
        };

        queue.add(request);
        queue.start();

    }

    public void onCancelFriendRequest(String strangerId) {//from recycler

        //update firebase
        dataBaseClass.updateCanceledFriendRequest(strangerId, currentUser.getUserId());

    }

    public MutableLiveData<ArrayList<String>> getRecentSentRequests(){
        return recentSentRequests;
    }

    public MutableLiveData<Boolean> getSwipeLayoutBool(){
        return swipeLayoutBool;
    }

    public MutableLiveData<String> getAddressLiveData(){
        return addressLiveData;
    }

}
