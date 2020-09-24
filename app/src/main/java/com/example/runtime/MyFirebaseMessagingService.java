package com.example.runtime;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "serviceTag";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...


        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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

}
