package com.example.lab2_mobiledevelopment.CloudNotifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.lab2_mobiledevelopment.MessageActivity;
import com.example.lab2_mobiledevelopment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class UserMessagingService extends FirebaseMessagingService {

    private String VERSION_ID = "com.example.lab2_mobiledevelopment";
    private String VERSION_NAME = "publicchat";

    private NotificationManager notificationManager;

    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);

        String sented = remoteMessage.getData().get("sented");
        String user = remoteMessage.getData().get("user");


        SharedPreferences preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        String currentUser = preferences.getString("currentuser", "none");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null && sented.equals(firebaseUser.getUid())){
            if(!currentUser.equals(user)){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    createVersion26plus();
                    showOreoNotification(remoteMessage);
                }
                else{
                    showNotification(remoteMessage);
                }

            }

        }

        //showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());



    }



    @TargetApi(Build.VERSION_CODES.O)
    public void createVersion26plus(){
        NotificationChannel version = new NotificationChannel(VERSION_ID, VERSION_NAME, NotificationManager.IMPORTANCE_DEFAULT);

        version.enableLights(false);
        version.enableVibration(true);
        version.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getNotificationManager().createNotificationChannel(version);
    }

    public NotificationManager getNotificationManager(){

        if(notificationManager == null){
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getOreoNotification(String title, String body, PendingIntent pendingIntent, Uri soundUri){

        return new Notification.Builder(getApplicationContext(), VERSION_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.avatar6)
                .setSound(soundUri)
                .setAutoCancel(true);
    }


    private void showOreoNotification(RemoteMessage remoteMessage){

        String user = remoteMessage.getData().get("user");

        String title = remoteMessage.getData().get("title");

        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int notificationId = (int) System.currentTimeMillis();

        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder builder = getOreoNotification(title, body, pendingIntent, defaultSound);

        getNotificationManager().notify(notificationId, builder.build());
    }

    public void showNotification(RemoteMessage remoteMessage){

        String user = remoteMessage.getData().get("user");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        int notificationId = (int) System.currentTimeMillis();

        //RemoteMessage.Notification notification = remoteMessage.getNotification();
        //int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.avatar6)
                .setAutoCancel(true)
                .setContentText(body)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(notificationId, builder.build());
    }
}
