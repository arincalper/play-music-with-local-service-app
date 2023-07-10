package com.example.eee335_lab3_alper_arinc;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class LocalService extends Service {
    private MediaPlayer player;
    private NotificationManager mNM;
    private int NOTIFICATION = 0;
    private final IBinder mBinder = new LocalBinder();
    private static final String NOTIFICATION_ID_STRING = "My Notifications";

    //----------------------------------------------------------------------------------------------
    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }
    //----------------------------------------------------------------------------------------------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LocalService", "onStartCommand()");
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.start();
        player.setLooping(true);
        return START_STICKY;
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onCreate() {
        Log.d("LocalService", "onCreate()");
        Toast.makeText(this, "local service started", Toast.LENGTH_SHORT).show();
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onDestroy() {
        Log.d("LocalService", "onDestroy()");
        player.stop();
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, "local service stopped", Toast.LENGTH_SHORT).show();
    }
    //----------------------------------------------------------------------------------------------
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    //----------------------------------------------------------------------------------------------
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "Tap to return to the application!";

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_ONE_SHOT);
        }
        else
        {
            contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        }

        //Create the channel. Android will automatically check if the channel already exists
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID_STRING, "My Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("My notification channel description");
            mNM.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notifyBuilder
                = new NotificationCompat.Builder(this, NOTIFICATION_ID_STRING)
                .setContentTitle("Exercise 06")
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.umbrella);
        Notification myNotification = notifyBuilder.build();
        mNM.notify(NOTIFICATION, myNotification);
    }
    //----------------------------------------------------------------------------------------------
    public String getPlayInfo() {
        return player.getCurrentPosition() + " / " + player.getDuration();
    }
}