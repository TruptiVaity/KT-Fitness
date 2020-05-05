package com.example.ktfit;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC;

public class ViewNotification extends Application {
    public static final String CHANNEL_1_ID = "channel_workout";
    public static final String CHANNEL_2_ID = "channel_tracker";
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "WorkoutSession",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel1.setDescription("Display Workout");
            channel1.setLockscreenVisibility(VISIBILITY_PUBLIC);
/**
            NotificationChannel trackerChannel = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Tracking Water and Caffeine",
                    NotificationManager.IMPORTANCE_HIGH);
            trackerChannel.setLockscreenVisibility(VISIBILITY_PUBLIC);
**/
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
 //           manager.createNotificationChannel(trackerChannel);
        }
    }

}


