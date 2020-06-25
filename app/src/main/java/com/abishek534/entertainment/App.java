package com.abishek534.entertainment;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
public static final String CHANNEL_ID="channel1";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotification();
    }

    private void createNotification() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID,"channel1", NotificationManager.IMPORTANCE_UNSPECIFIED);
            channel1.setDescription("this is channel 1");
            channel1.setSound(null,null);
            channel1.enableVibration(false);
            channel1.enableLights(false);
            NotificationManager manager = getSystemService(NotificationManager.class);
           // manager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
            manager.createNotificationChannel(channel1);
        }
    }
}
