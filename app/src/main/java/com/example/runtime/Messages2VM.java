package com.example.runtime;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
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
import com.example.runtime.model.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Messages2VM extends AndroidViewModel {
    private MutableLiveData<StorageReference> liveDataImage = new MutableLiveData<>();
    private MutableLiveData<String> liveDataFullName = new MutableLiveData<>();
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    private MutableLiveData<List<Message>> messageListLiveData;
    private User user = UserInstance.getInstance().getUser();
    private final String API_TOKEN_KEY = "AAAAfwvvO64:APA91bG6RWYJYEROIIoBMpzKm6kMdCbqDdqpzhynZ4YnFKEiQ0vu5QuLfJdGTtlixdzqBoL2Ul99A5Mf9kspOh8Whz9U-AY1-7rQTBiOUNUeYZM3UHh4A7Tm4Kb-u4Hrv98zApJn76NQ";

    //TODO maybe we should use the user object instead
    private String activeConversationFriendId;

    public Messages2VM(@NonNull Application application) {
        super(application);
        messageListLiveData = new MutableLiveData<>();
    }

    public void setName(String name) {
        liveDataFullName.setValue(name);
    }

    public void setActiveConversationFriendId(String activeConversationFriendId) {
        this.activeConversationFriendId = activeConversationFriendId;
    }

    public void setImage(String id) {
        StorageReference storageReference = dataBaseClass.retrieveImageStorageReference(id);
        liveDataImage.setValue(storageReference);
    }

    //TODO check why we should return LiveData instead of MutableLiveData
    public LiveData<String> getLiveDataFullName() {
        return liveDataFullName;
    }

    public LiveData<StorageReference> getLiveDataImage() {
        return liveDataImage;
    }

    public LiveData<List<Message>> getMessagesLiveData(String id) {
        return messageListLiveData;
    }

    public void addToList(String message){
        List<Message> list = new ArrayList<>(messageListLiveData.getValue() == null ? new ArrayList<Message>() : messageListLiveData.getValue());
        String pattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        list.add(new Message(message,date,list.size(),activeConversationFriendId));
        messageListLiveData.setValue(list);
    }

    public void sendMessage(String message,String token,String name) throws JSONException {
        List<Message> list = new ArrayList<>(messageListLiveData.getValue() == null ? new ArrayList<Message>() : messageListLiveData.getValue());

        //TODO change it to whatever
        String pattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        Message msg = new Message(message, date,list.size(),user.getUserId());
        list.add(msg);
        messageListLiveData.setValue(list);

        dataBaseClass.sendMessage(msg , activeConversationFriendId);
        createNotificationMessage(token,msg,name);
    }

    public void getAllMessages(){
        final List<Message> list = new ArrayList<>(messageListLiveData.getValue() == null ? new ArrayList<Message>() : messageListLiveData.getValue());
        list.clear();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Message userId = snapshot1.getValue(Message.class);
                        list.add(userId);
                    }
                    messageListLiveData.setValue(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dataBaseClass.retrieveMessages(activeConversationFriendId,valueEventListener);
    }

    private void createNotificationMessage(String token,Message message,String name) throws JSONException {

        Log.d("tag2", "inside create friendRequestNotificationMessage");

        final JSONObject rootObject = new JSONObject();
        rootObject.put("to", token);
        rootObject.put("messageType","message");
        JSONObject notificationObject = new JSONObject();
        notificationObject.put("title", "New message from "+ name);
        notificationObject.put("body","from "+message);
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
