package com.example.runtime;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.runtime.model.Message;
import com.example.runtime.model.ModelWithID;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Messages2VM extends ViewModel {
    private MutableLiveData<StorageReference> liveDataImage = new MutableLiveData<>();
    private MutableLiveData<String> liveDataFullName = new MutableLiveData<>();
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    private MutableLiveData<List<ModelWithID<Message>>> messageListLiveData;

    //TODO maybe we should use the user object instead
    private String activeConversationFriendId;

    public Messages2VM() {
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

    public LiveData<List<ModelWithID<Message>>> getMessagesLiveData(String id) {
        return messageListLiveData;
    }

    public void sendMessage(String message) {
        List<ModelWithID<Message>> list = new ArrayList<>(messageListLiveData.getValue() == null ? new ArrayList<ModelWithID<Message>>() : messageListLiveData.getValue());

        //TODO change it to whatever
        String pattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        list.add(new ModelWithID<>(String.valueOf(new Random().nextInt(50000)), new Message(message, date)));
        messageListLiveData.setValue(list);
    }
}
