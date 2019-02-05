package com.golaspico.vanhyori.prov_hv3;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMReciever extends FirebaseMessagingService {

    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if(remoteMessage.getData().size() > 0){
            Log.d("FCM","Message data payload : " + remoteMessage.getData());

        }

        if(remoteMessage.getNotification() != null){
            Log.d("FCM","Message Notification : " +remoteMessage.getNotification().getBody());
        }

        String message = remoteMessage.getNotification().getBody();
        String messageTitle = remoteMessage.getNotification().getTitle();
        sendNotification(message,messageTitle);
    }

    private void sendNotification(String messageBody,String messageTitle){

        Log.d("FCM","Trying to make Notification" + messageBody.toString());
        Uri defaultSoundURL = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder notficiationBuilder = new NotificationCompat.Builder(this);
            notficiationBuilder.setSmallIcon(R.drawable.ic_assignment_black_24dp);
        notficiationBuilder.setContentTitle(messageTitle);
        notficiationBuilder.setContentText(messageBody);
        notficiationBuilder.setAutoCancel(true);
        notficiationBuilder.setSound(defaultSoundURL);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notficiationBuilder.build());

        
        
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("FCM","On New Token :" + s);
        sendRegistrationToServer(s);
    }

    public void sendRegistrationToServer(String token){
        Log.d("FCM", "Refreshed token: " + token);
        db.child("fcmTokens").child(FirebaseAuth.getInstance().getUid()).setValue(token);

    }
}
