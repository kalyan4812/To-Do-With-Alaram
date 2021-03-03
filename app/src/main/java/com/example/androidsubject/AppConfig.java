package com.example.androidsubject;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class AppConfig extends Application {

    public static final String CHANNEL_ID = "LocationServiceChannel";
    private static Context context;

    private static WebServiceProvider webServiceProvider;


    public static enum API_ENDPOINTS {
        localhost, remote
    }


    @Override
    public void onCreate() {
        super.onCreate();
      createNotificationChannel();

        context = getApplicationContext();

        webServiceProvider = WebServiceProvider.getInstance(context);

    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "LOCATIONSERVICE", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public static Context getContext() {
        return context;
    }

    public static WebServiceProvider getWebServiceProvider() {
        return webServiceProvider;
    }


}
