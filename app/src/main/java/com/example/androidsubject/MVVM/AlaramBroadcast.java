package com.example.androidsubject.MVVM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.androidsubject.R;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

import static com.example.androidsubject.AppConfig.CHANNEL_ID;
import static com.example.androidsubject.MVVM.AddNote.notificationManager;

public class AlaramBroadcast extends BroadcastReceiver {
    //  NotificationManager   notificationManager=(NotificationManager)gets
    MediaPlayer mp;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onReceive(Context context, Intent intent) {
        context = context;
        sharedPreferences=context.getSharedPreferences("color",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        Intent i = new Intent(context, Myservices.class);
        i.putExtra("key",intent.getStringExtra("key"));
       // i.putExtra("title",)
        editor.putBoolean(intent.getStringExtra("key"),true).commit();
        ContextCompat.startForegroundService(context,i);


    }

    public static class Myservices extends Service {
        MediaPlayer mp;

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }


        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Random random = new Random();


            int randomNumber_temp = random.nextInt(999999);
            if (randomNumber_temp == 0) {
                randomNumber_temp = 48;
            }
            Uri ringtonepath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Intent intents = new Intent(getApplicationContext(), MyActivity.class);
            intents.putExtra("state", true);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intents, 0);
            Notification builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
                    .setOngoing(true)
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_access_alarm_black_24dp))
                    .setContentTitle("Background Service")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    // .setContentIntent(pendingIntent)
                    .addAction(R.drawable.ic_access_alarm_black_24dp, "CANCEL", pendingIntent)
                    .setAutoCancel(false)
                    .setDefaults(NotificationCompat.DEFAULT_ALL).setSound(ringtonepath).build();
            builder.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_ONGOING_EVENT;


            startForeground(randomNumber_temp, builder);

            mp = MediaPlayer.create(this, R.raw.alaram);
            mp.setLooping(true);
            mp.start();
            return START_STICKY;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onTaskRemoved(Intent rootIntent) {
            super.onTaskRemoved(rootIntent);
        }
    }
}

