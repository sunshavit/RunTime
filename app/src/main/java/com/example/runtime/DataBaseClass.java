package com.example.runtime;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.runtime.model.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DataBaseClass {
    private FirebaseDatabase firebaseDatabase;
    private RegisterClass registerClass;
    private FirebaseStorage firebaseStorage;
    private final String USERSTABLE = "users";
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    //private UserInstance userInstance = UserInstance.getInstance();


    private static DataBaseClass dataBaseClass = null;

    interface OnChangeUserListener {
        void onChangeUserSuccess();

        void onChangeUserFailed();
    }

    interface OnLocationUpdateListener {
        void onLocationUpdate();
    }

    interface OnGetUserImage {
        void onSuccessGetImage(String uri);

        void onFailedGetImage();
    }

    interface OnUserCreateListener {
        void onSuccessCreate();

        void onFailedCreate(String problem);
    }

    interface OnUserPreferenceCreateListener {
        void onSuccessPreferenceCreate();

        void onFailedPreferenceCreate();
    }

    interface OnSaveImageListener {
        void onSuccessImage();

        void onFailedImage();
    }

    interface OnUserListsListener {
        void onSuccessUserLists();

        void onFailedUserLists();
    }


    private OnUserCreateListener callBackCreate;
    private OnUserPreferenceCreateListener callBackPreferenceCreate;
    private OnSaveImageListener callBackImage;
    private OnUserListsListener callBackUserLists;
    private OnGetUserImage callBackGetImage;
    private OnLocationUpdateListener updateListener;
    private OnChangeUserListener onChangeUserListener;

    private DataBaseClass() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        registerClass = RegisterClass.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = firebaseDatabase.getReference();

    }

    static DataBaseClass getInstance() {
        if (dataBaseClass == null) {
            dataBaseClass = new DataBaseClass();
        }
        return dataBaseClass;

    }

    public void sendMessage(Message model, String activeConversationFriendId) {
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference userMsgs = databaseReference.child("user_lists").child(registerClass.getUserId()).child("myMessages").child(activeConversationFriendId).child(model.getId()+"");
        userMsgs.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Log.d("sun","sun");
            }
        });
        sendMessageToPartner(model,activeConversationFriendId);
    }

    public void sendMessageToPartner(Message model, String activeConversationFriendId){
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference userMsgs = databaseReference.child("user_lists").child(activeConversationFriendId).child("myMessages").child(registerClass.getUserId()).child(model.getId()+"");
        userMsgs.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Log.d("sun","sun");
            }
        });
    }



    public void retrieveMessages(String partnerId ,ValueEventListener listener ){
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference userMsgs = databaseReference.child("user_lists").child(registerClass.getUserId()).child("myMessages").child(partnerId);
        userMsgs.addListenerForSingleValueEvent(listener);
    }

    public void saveUserToken(String token) {
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference users = databaseReference.child("user");
        DatabaseReference currentUser = users.child(registerClass.getUserId());
        currentUser.child("userToken").setValue(token);
    }


    public void changeUser(User user) {
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference databaseReference1 = databaseReference.child("user");
        databaseReference1.child(registerClass.getUserId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    onChangeUserListener.onChangeUserSuccess();
                else
                    onChangeUserListener.onChangeUserFailed();
            }
        });
    }

    public void createUser(User user) {
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference databaseReference1 = databaseReference.child("user");
        databaseReference1.child(registerClass.getUserId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    callBackCreate.onSuccessCreate();
                else
                    callBackCreate.onFailedCreate(task.getException().getMessage());
            }
        });
    }

    public void isUserExists(String userId, ValueEventListener listener) {
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference user = databaseReference.child("user");
        DatabaseReference currentUser = user.child(userId);
        currentUser.addListenerForSingleValueEvent(listener);
    }

    public void createPreferences(UserPreferences userPreferences) {
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference databaseReference1 = databaseReference.child("user_preferences");
        databaseReference1.child(registerClass.getUserId()).setValue(userPreferences).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    callBackPreferenceCreate.onSuccessPreferenceCreate();
                else
                    callBackPreferenceCreate.onFailedPreferenceCreate();
            }
        });
    }

    public void createUserLists(UserLists userLists) {
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference databaseReference1 = databaseReference.child("user_lists");
        databaseReference1.child(registerClass.getUserId()).setValue(userLists).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    callBackUserLists.onSuccessUserLists();
                else
                    callBackUserLists.onFailedUserLists();

            }
        });

    }

    public void saveProfilePicture(Uri imageUri) {
        if (imageUri != null) {
            StorageReference reference = storageReference.child("profileImages/" + RegisterClass.getInstance().getUserId());
            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    callBackImage.onSuccessImage();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callBackImage.onFailedImage();
                }
            });
        }

    }

    public void updateActive(boolean isActive) {
        DatabaseReference users = databaseReference.child("user");
        DatabaseReference specificUser = users.child(registerClass.getUserId());
        specificUser.child("active").setValue(isActive);
    }

    public void updateLocation(double longitude, double latitude) {

        if (registerClass.getCurrentUser() != null) {
            DatabaseReference users = databaseReference.child("user");
            DatabaseReference specificUser = users.child(registerClass.getUserId());
            specificUser.child("longitude").setValue(longitude);
            specificUser.child("latitude").setValue(latitude);
            if (updateListener != null)
                updateListener.onLocationUpdate();
        }
    }

    public void updateSentFriendRequest(String strangerId, String userId) {
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference userLists = databaseReference.child("user_lists");
        DatabaseReference currentUserLists = userLists.child(userId);
        DatabaseReference strangerLists = userLists.child(strangerId);
        currentUserLists.child("sentFriendsRequests").child(strangerId).setValue(true);
        strangerLists.child("friendsRequests").child(userId).setValue(true);
    }

    public void updateCanceledFriendRequest(String strangerId, String userId) {
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference userLists = databaseReference.child("user_lists");
        DatabaseReference currentUserLists = userLists.child(userId);
        DatabaseReference strangerLists = userLists.child(strangerId);
        currentUserLists.child("sentFriendsRequests").child(strangerId).removeValue();
        strangerLists.child("friendsRequests").child(userId).removeValue();
    }

    public void retrieveUserById(String userId, ValueEventListener listener) {
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference users = databaseReference.child("user");
        users.child(userId).addListenerForSingleValueEvent(listener);
    }

    public void retrieveSentRequests(ValueEventListener listener) {
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference userLists = databaseReference.child("user_lists");
        DatabaseReference currentUser = userLists.child(registerClass.getUserId());
        currentUser.child("sentFriendsRequests").addListenerForSingleValueEvent(listener);
    }

    public void retrieveUserToken(String userId, ValueEventListener listener) {
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference users = databaseReference.child("user");
        DatabaseReference specificUser = users.child(userId);
        specificUser.child("userToken").addListenerForSingleValueEvent(listener);
    }


    public void getImage() {
        StorageReference reference = storageReference.child("profileImages/" + RegisterClass.getInstance().getUserId());
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                callBackGetImage.onSuccessGetImage(uri.toString());
            }
        });
    }


    public void getUser(ValueEventListener listener) {
        final DatabaseReference users = firebaseDatabase.getReference("user");
        users.child(registerClass.getUserId()).addListenerForSingleValueEvent(listener);
    }

    public void onCancelled(@NonNull DatabaseError error) {
    }


    public void setOnChangeUserListener(OnChangeUserListener onChangeUserListener) {
        this.onChangeUserListener = onChangeUserListener;
    }

    public void setCallBackCreate(OnUserCreateListener callBackCreate) {
        this.callBackCreate = callBackCreate;
    }

    public void setCallBackPreferenceCreate(OnUserPreferenceCreateListener callBackPreferenceCreate) {
        this.callBackPreferenceCreate = callBackPreferenceCreate;
    }

    public void setCallBackImage(OnSaveImageListener callBackImage) {
        this.callBackImage = callBackImage;
    }

    public void setUpdateLocationListener(OnLocationUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public void setCallBackUserLists(OnUserListsListener callBackUserLists) {
        this.callBackUserLists = callBackUserLists;
    }


    public void retrieveAllUsersList(ValueEventListener listener) {
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("user").addListenerForSingleValueEvent(listener);
    }

    public void getUserWithId(ValueEventListener listener, String id) {
        final DatabaseReference users = firebaseDatabase.getReference("user");
        users.child(id).addListenerForSingleValueEvent(listener);
    }

    public void retrieveAllFriends(ValueEventListener listener) {
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("user_lists").child(registerClass.getUserId()).child("myFriends").addValueEventListener(listener);
    }

    public void retrieveUserPreferences(ValueEventListener listener) {
        //final UserPreferences[] userPreferences = new UserPreferences[1];
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference userPreferenceTable = databaseReference.child("user_preferences");
        userPreferenceTable.child(registerClass.getUserId()).addListenerForSingleValueEvent(listener);
    }

    public StorageReference retrieveImageStorageReference(String UserId) {
        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child("profileImages/" + UserId);
        return storageReference1;
    }

    public void setCallBackGetImage(OnGetUserImage callBackGetImage) {
        this.callBackGetImage = callBackGetImage;

    }

    public void createNewEvent(Event event, String userId) {
        databaseReference = firebaseDatabase.getReference(); //to get root
        DatabaseReference events = databaseReference.child("events");
        DatabaseReference newEvent = events.push();
        String eventKey = newEvent.getKey();
        event.setEventId(eventKey);
        events.child(eventKey).setValue(event); //save new event to events.

        DatabaseReference userLists = databaseReference.child("user_lists");
        DatabaseReference currentUser = userLists.child(userId);
        DatabaseReference managedEvents = currentUser.child("managedEvents");
        DatabaseReference myEvents = currentUser.child("myEvents");

        myEvents.child(eventKey).setValue(eventKey);
        managedEvents.child(eventKey).setValue(eventKey);


        // public void updateName(String name){
        //  DatabaseReference databaseReferenceNew = firebaseDatabase.getReference().child("user").child(registerClass.getUserId());
        // databaseReferenceNew.child("fullName").setValue(name);
        //    }

        // public void updateRunningLevel(String level){
        //  DatabaseReference databaseReferenceNew = firebaseDatabase.getReference().child("user").child(registerClass.getUserId());
        //  databaseReferenceNew.child("runningLevel").setValue(level);
        // }


    }

}



