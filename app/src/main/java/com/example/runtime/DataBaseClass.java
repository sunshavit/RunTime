package com.example.runtime;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class DataBaseClass {
    private FirebaseDatabase firebaseDatabase;
    private RegisterClass registerClass;
    private FirebaseStorage firebaseStorage;
    private final String USERSTABLE = "users";
    private DatabaseReference databaseReference;
    private StorageReference storageReference;


    private static DataBaseClass dataBaseClass = null;

    interface OnGetUserImage{
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

    interface OnSaveImageListener{
        void onSuccessImage();
        void onFailedImage();
    }

    interface OnUserListsListener{
        void onSuccessUserLists();
        void onFailedUserLists();
    }


     private OnUserCreateListener callBackCreate;
     private OnUserPreferenceCreateListener callBackPreferenceCreate;
     private OnSaveImageListener callBackImage;
     private OnUserListsListener callBackUserLists;
     private OnGetUserImage callBackGetImage;

    private DataBaseClass(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        registerClass = RegisterClass.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = firebaseDatabase.getReference();
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

    public void createPreferences(UserPreferences userPreferences){
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference databaseReference1 = databaseReference.child("user_preferences");
        databaseReference1.child(registerClass.getUserId()).setValue(userPreferences).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    callBackPreferenceCreate.onSuccessPreferenceCreate();
                else
                    callBackPreferenceCreate.onFailedPreferenceCreate();
            }
        });
    }

    public void createUserLists(UserLists userLists){
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference databaseReference1=databaseReference.child("user_lists");
        databaseReference1.child(registerClass.getUserId()).setValue(userLists).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    callBackUserLists.onSuccessUserLists();
                else
                    callBackUserLists.onFailedUserLists();

            }
        });

    }

    public void saveProfilePicture(Uri imageUri){
        if(imageUri!=null){
            StorageReference reference = storageReference.child("profileImages/"+RegisterClass.getInstance().getUserId());
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

    public void updateActive(boolean isActive){
        DatabaseReference users=databaseReference.child("user");
        DatabaseReference specificUser=users.child(registerClass.getUserId());
        specificUser.child("active").setValue(isActive);
    }

    public void updateLocation(double longitude,double latitude){
        DatabaseReference users=databaseReference.child("user");
        DatabaseReference specificUser=users.child(registerClass.getUserId());
        specificUser.child("longitude").setValue(longitude);
        specificUser.child("latitude").setValue(latitude);
    }

    public void getImage(){
        StorageReference reference = storageReference.child("profileImages/"+RegisterClass.getInstance().getUserId());
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                callBackGetImage.onSuccessGetImage(uri.toString());
            }
        });
    }

    public void getUser(ValueEventListener listener){
        final DatabaseReference users = firebaseDatabase.getReference("user");
        Log.d("sun","sun");
        users.child(registerClass.getUserId()).addListenerForSingleValueEvent(listener);
    }

    public void onCancelled(@NonNull DatabaseError error) {
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

    public void setCallBackUserLists(OnUserListsListener callBackUserLists) {
        this.callBackUserLists = callBackUserLists;
    }


    public void retrieveAllUsersList(ValueEventListener listener){
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("user").addListenerForSingleValueEvent(listener);
    }

    public void retrieveUserPreferences(ValueEventListener listener){
        final UserPreferences[] userPreferences = new UserPreferences[1];
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference userPreferenceTable = databaseReference.child("user_preferences");
        userPreferenceTable.child(registerClass.getUserId()).addListenerForSingleValueEvent(listener);
    }

      public StorageReference retrieveImageStorageReference (String UserId){
        storageReference = FirebaseStorage.getInstance().getReference().child("profileImages/"+ UserId);
        return storageReference;

    public void setCallBackGetImage(OnGetUserImage callBackGetImage) {
        this.callBackGetImage = callBackGetImage;

    }
}
