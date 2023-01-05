package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.myapplication.App.FCM_CHANNEL_ID;

public class FCMMessageReceiverService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Message","onMessageCalled");
        Log.d("Message","Message received from :"+remoteMessage.getFrom());

        if(remoteMessage.getNotification() != null)
        {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            Intent intent =new Intent(this,Notification.class);
            PendingIntent resPendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(this,FCM_CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(Color.BLUE)
                    .setAutoCancel(true)
                    .setContentIntent(resPendingIntent)
                    .build();

            NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1002,notification);
        }
        if(remoteMessage.getData().size() > 0)
        {
            Log.d("Message","Message received: DATA :"+remoteMessage.getData().toString());
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.d("Delete","onDeleteCalled");
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("Token","onTokenCalled");
    }
}
