package com.example.runtime;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.StorageReference;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "serviceTag";
    NotificationManager manager;
    DataBaseClass dataBaseClass = DataBaseClass.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...


        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (remoteMessage.getData().get("messageType").equals("friendRequestAccepted")){
                //create notification
                String title = remoteMessage.getData().get("title");
                String body = remoteMessage.getData().get("body");
                String userId = remoteMessage.getData().get("userId");
                createRequestAcceptedNotif(title, body, userId);
            }


            else if(remoteMessage.getData().get("messageType").equals("message")){
                String title = remoteMessage.getData().get("title");
                String body = remoteMessage.getData().get("body");
                Intent intent = new Intent("messagesReceiver");
                intent.putExtra("message",body);
                createNotifMessages(title,body);

            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());


            //NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(this);
            if (Build.VERSION.SDK_INT >= 26){
                String channelId  = "channelId";
                NotificationChannel channel = new NotificationChannel(channelId, "channelName", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId( channelId);
            }

            builder.setContentText(remoteMessage.getNotification().getBody()).setContentTitle(remoteMessage.getNotification().getTitle()).setSmallIcon(android.R.drawable.star_on);
            manager.notify(1, builder.build());
        }



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void createRequestAcceptedNotif(String title, String body, String userId) {

        final int NOTIF_ID = 11;
        final Notification.Builder builder = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT >= 26){
            String channelId  = "channelId";
            NotificationChannel channel = new NotificationChannel(channelId, "channelName", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId( channelId);
        }
        builder.setContentText(body).setContentTitle(title).setSmallIcon(android.R.drawable.star_on);
        manager.notify(NOTIF_ID, builder.build());

        StorageReference userImageRef = dataBaseClass.retrieveImageStorageReference(userId);

        Glide.with(this)
                .asBitmap()
                .load(userImageRef)
                .into(new CustomTarget<Bitmap>(300, 300) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        builder.setLargeIcon(resource);
                        manager.notify(NOTIF_ID, builder.build());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });


    }



}

    private void createNotifMessages(String title, String body) {

        final int NOTIF_ID = 11;
        final Notification.Builder builder = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT >= 26){
            String channelId  = "channelId";
            NotificationChannel channel = new NotificationChannel(channelId, "channelName", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId( channelId);
        }
        builder.setContentText(body).setContentTitle(title).setSmallIcon(android.R.drawable.star_on);
        manager.notify(NOTIF_ID, builder.build());




    }

}

