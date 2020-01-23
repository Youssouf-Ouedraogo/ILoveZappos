package com.manoideveloppers.ilovezappos;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationHandler extends Application {

    public static final String notificationId = "Notification";

    @Override
    public void onCreate() {
        super.onCreate();


       if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           NotificationChannel notificationChannel = new NotificationChannel(
                   notificationId,"Notification",
                   NotificationManager.IMPORTANCE_HIGH // alert when receive notification
           );
           NotificationManager manager = getSystemService(NotificationManager.class);
           manager.createNotificationChannel(notificationChannel);
       }


    }
}

