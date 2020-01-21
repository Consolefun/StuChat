//package com.example.lab2_mobiledevelopment.CloudNotifications;
//
//import android.annotation.TargetApi;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.os.Build;
//
//public class UserMessagingService26Up extends ContextWrapper {
//
//    private String VERSION_ID = "com.example.lab2_mobiledevelopment"
//    private String VERSION_NAME = "publicchat";
//
//    private NotificationManager notificationManager;
//
//    public UserMessagingService26Up(Context base){
//        super(base);
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            createVersion();
//        }
//
//
//    }
//
//    @TargetApi(Build.VERSION_CODES.O)
//    private void createVersion(){
//
//        NotificationChannel version = new NotificationChannel(VERSION_ID, VERSION_NAME, NotificationManager.IMPORTANCE_DEFAULT);
//
//        version.enableLights(false);
//        version.enableVibration(true);
//        version.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//
//        getNotificationManager().createNotificationChannel(version);
//    }
//
//    public NotificationManager getNotificationManager(){
//
//        if(notificationManager == null){
//            notificationManager = (NotificationManager)getS
//        }
//    }
//}
