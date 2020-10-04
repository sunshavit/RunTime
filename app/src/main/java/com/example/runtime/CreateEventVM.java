package com.example.runtime;

import android.app.Application;
import android.net.sip.SipSession;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
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
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateEventVM extends AndroidViewModel {

    private int eventYear;
    private int eventMonth;
    private int eventDayOfMonth;
    private int eventHourOfDay;
    private int eventMinute;
    private double longitude;
    private double latitude;
    private String runningLevel;
    private String eventStatus;
    private UserInstance userInstance = UserInstance.getInstance();
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();

    private String eventDate;
    private String eventTime;
    private int easyImageView;
    private int mediumImageView;
    private int hardImageView;
    private boolean isFirstLaunch=true;
    private int easyTextView;
    private int mediumTextView;
    private int hardTextView;
    private boolean isPublicChecked;
    private boolean isPrivateChecked;


    private final String API_TOKEN_KEY = "AAAAfwvvO64:APA91bG6RWYJYEROIIoBMpzKm6kMdCbqDdqpzhynZ4YnFKEiQ0vu5QuLfJdGTtlixdzqBoL2Ul99A5Mf9kspOh8Whz9U-AY1-7rQTBiOUNUeYZM3UHh4A7Tm4Kb-u4Hrv98zApJn76NQ";

    public CreateEventVM(@NonNull Application application) {
        super(application);
    }


        MutableLiveData<String> streetAddress = new MutableLiveData<>();

    public CreateEventVM(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getStreetAddress(){
        return streetAddress;
    }

    public void setStreetAddress(String address){
        streetAddress.setValue(address);
    }
    public void setLongitudeLatitude (double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public void setEventData ( int eventYear, int eventMonth, int eventDayOfMonth, int eventHourOfDay, int eventMinute, String runningLevel, String eventStatus, ArrayList<String> invitedFriendsIds) {
        this.eventYear = eventYear;
        this.eventMonth = eventMonth;
        this.eventDayOfMonth = eventDayOfMonth;
        this.eventHourOfDay = eventHourOfDay;
        this.eventMinute = eventMinute;
        this.runningLevel = runningLevel;
        this.eventStatus = eventStatus;

        Event event = new Event(eventYear,eventMonth,eventDayOfMonth,eventHourOfDay,eventMinute,longitude,latitude,runningLevel,eventStatus);
        String userId = userInstance.getUser().getUserId();
        dataBaseClass.createNewEvent(event,userId,invitedFriendsIds);

        if(invitedFriendsIds!=null){
            userTokenFromDatabase(invitedFriendsIds);
        }



    }


    public void setEventDate(String eventDate){
        this.eventDate = eventDate;
    }

    public String getEventDate(){
        return eventDate;
    }

    public void setEventTime(String eventTime){
        this.eventTime = eventTime;
    }

    public String getEventTime(){
        return  eventTime;
    }

    public void setEasyImageView(int easyImageView){
        this.easyImageView = easyImageView;
    }

    public int getEasyImageView(){
        return easyImageView;
    }

    public void setMediumImageView(int mediumImageView){
        this.mediumImageView = mediumImageView;
    }

    public int getMediumImageView(){
        return mediumImageView;
    }

    public void setHardImageView(int hardImageView){
        this.hardImageView = hardImageView;
    }

    public int getHardImageView(){
        return hardImageView;
    }

    public void setIsFirstLaunch(boolean isFirstLaunch){
        this.isFirstLaunch = isFirstLaunch;
    }

    public boolean getIsFirstLaunch(){
        return isFirstLaunch;
    }

    public int getEasyTextView() {
        return easyTextView;
    }

    public void setEasyTextView(int easyTextView) {
        this.easyTextView = easyTextView;
    }

    public int getMediumTextView() {
        return mediumTextView;
    }

    public void setMediumTextView(int mediumTextView) {
        this.mediumTextView = mediumTextView;
    }

    public int getHardTextView() {
        return hardTextView;
    }

    public void setHardTextView(int hardTextView) {
        this.hardTextView = hardTextView;
    }

    public boolean isPublicChecked() {
        return isPublicChecked;
    }

    public void setPublicChecked(boolean publicChecked) {
        isPublicChecked = publicChecked;
    }

    public boolean isPrivateChecked() {
        return isPrivateChecked;
    }

    public void setPrivateChecked(boolean privateChecked) {
        isPrivateChecked = privateChecked;
    }

    public void userTokenFromDatabase(final ArrayList<String> invitedFriendsIds){

        for( String invitedFriendId: invitedFriendsIds){
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String invitedFriendToken = snapshot.getValue(String.class);
                        try {
                            createFriendRequestNotificationMessage(invitedFriendToken);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            dataBaseClass.retrieveUserToken(invitedFriendId, listener);

        }
    }


    private void createFriendRequestNotificationMessage(String token) throws JSONException {

        final JSONObject rootObject = new JSONObject();
        rootObject.put("to", token);
        JSONObject notificationObject = new JSONObject();
        notificationObject.put("title", "New invitation to event!");
        notificationObject.put("body", "don't  keep them waiting");
        rootObject.put("notification", notificationObject);

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
}
