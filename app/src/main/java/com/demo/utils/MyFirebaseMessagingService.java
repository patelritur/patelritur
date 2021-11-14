package com.demo.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.demo.R;
import com.demo.home.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "com.demo";
    private static final String CHANNEL_ONE_NAME = "Channel One";
    private Intent notificationIntent;
    private PendingIntent resultPendingIntent;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e("newToken", token);


    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
              /*  contentTitle=Demo Booking Request, message=You have demo request at the Home Now!!,
            notificationtype=DemoBookingRequest}]*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels(getApplicationContext());
        }
        Log.e("remoteMessage", remoteMessage.getData().get("message"));
        Log.e("remoteMessage", remoteMessage.getData().get("contentTitle"));
        Log.e("remoteMessage", remoteMessage.getData().get("notificationtype"));;

        Log.e("remoteMessage demoid", remoteMessage.getData().get("demoid"));;

        if(remoteMessage.getData().get("notificationtype").equalsIgnoreCase("AcceptDemoRequest") ||
                remoteMessage.getData().get("notificationtype").equalsIgnoreCase("AcceptMeetRequest")){

            notificationIntent = new Intent(getApplicationContext(), HomeActivity.class).
                    putExtra("demoid",remoteMessage.getData().get("demoid")).
                    putExtra("notificationtype",remoteMessage.getData().get("notificationtype")).

                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP).
                    setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(notificationIntent);
// Get the PendingIntent containing the entire back stack
             resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        sendPushNotification(getApplicationContext(),remoteMessage.getData().get("message"));
    }
    @SuppressLint("ResourceAsColor")
    private  void sendPushNotification(Context context, String remoteMessage) {
        {
            Bitmap bitmap;
            int icon;
            {
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.app_icon);
                icon = R.mipmap.app_icon;
            }
            NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getNotification1(context, remoteMessage, context.getString(R.string.app_name));
            } else {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder;
                int notificationId = 1;
                notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(icon)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(remoteMessage)
                        .setStyle(bigStyle.bigText(remoteMessage))
                        .setAutoCancel(true)
                        .setLargeIcon(bitmap)
                        .setColor(R.color.color_E8505B)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_MIN)
                        .setContentIntent(resultPendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.FLAG_NO_CLEAR);
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notificationId, notificationBuilder.build());

            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createChannels(Context context) {

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,

                CHANNEL_ONE_NAME, getManager(context).IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager(context).createNotificationChannel(notificationChannel);


    }


    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationCompat.Builder getNotification1(Context context, String message, String title) {

        Bitmap bitmap;
        int icon;
        bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.app_icon);
        icon = R.mipmap.app_icon;

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(message)
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSmallIcon(icon)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setColor(R.color.color_E8505B)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_MIN)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
        notify(context, 1, builder);
        return builder;


    }


    private static void notify(Context context, int id, NotificationCompat.Builder notification) {
        getManager(context).notify(id, notification.build());
    }


    private static NotificationManager getManager(Context context) {
        NotificationManager notifManager = null;
        if (notifManager == null) {
            notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notifManager;
    }


}
