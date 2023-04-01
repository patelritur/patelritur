package com.demo.home.booking;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.demo.R;
import com.demo.home.HomeActivity;
import com.demo.utils.PrintLog;

public class My15MinutesLocalReceiver extends BroadcastReceiver {
    private boolean isRingtone=true;
    private static final String CHANNEL_ONE_NAME = "Channel two";
    private PendingIntent resultPendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Code to display notification
        PrintLog.v("trigger receive");
        Intent notificationIntent = new Intent(context, HomeActivity.class);
        notificationIntent.putExtra("comeFrom","LocalNotification15");
        notificationIntent.putExtra("bookingId", intent.getExtras().getString("bookingId"));
        notificationIntent.putExtra("demoType", intent.getExtras().getString("demoType"));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(notificationIntent);
        resultPendingIntent = stackBuilder.getPendingIntent(0,  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
      {
            PrintLog.v("tvs vs tvs =====scheduleontime 15 min");
            if (intent.getExtras().getString("demoType").equalsIgnoreCase("Demo"))
                sendPushNotification(context, "Your scheduled Demo  will begin in 15 minutes", "Scheduled Demo Booking");
            else
                sendPushNotification(context, "Your scheduled Meeting  will begin in 15 minutes", "Scheduled Meeting Booking");

        }
    }

    @SuppressLint("ResourceAsColor")
    private  void sendPushNotification(Context context, String message, String remoteMessage) {
        {

            Bitmap bitmap;
            int icon;
            {
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.app_icon);
                icon = R.mipmap.notification;
            }
            NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getNotification1(context, message, remoteMessage);
            } else {
//                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Uri defaultSoundUri = Uri.parse(
                        "android.resource://" +
                                context.getApplicationContext().getPackageName() +
                                "/" +
                                R.raw.notification_sound);

                NotificationCompat.Builder notificationBuilder;
                notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(icon)
                        .setContentTitle(message)
                        .setContentText(remoteMessage)
                        .setStyle(bigStyle.bigText(remoteMessage))
                        .setAutoCancel(true)
                        .setLargeIcon(bitmap)
                        .setColor(R.color.color_241e61)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.DEFAULT_SOUND)
                        .setContentIntent(resultPendingIntent)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.FLAG_NO_CLEAR);
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if(isRingtone) {
                    Notification notification = notificationBuilder.build();
                    notification.flags |= Notification.FLAG_INSISTENT;
                    notificationManager.notify(1, notification);
                }
                else
                    notificationManager.notify(1, notificationBuilder.build());

            }

        }

    }



    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getNotification1(Context context, String message, String title) {

        Bitmap bitmap;
        int icon;
        bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.app_icon);
        icon = R.mipmap.notification;

//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri defaultSoundUri = Uri.parse(
                "android.resource://" +
                        context.getApplicationContext().getPackageName() +
                        "/" +
                        R.raw.notification_sound);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1");
        NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(context);
        @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel("1",

                CHANNEL_ONE_NAME, notificationManagerCompat.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        AudioAttributes audioAttributes;

        audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        notificationChannel.setSound(defaultSoundUri,audioAttributes);
        notificationManagerCompat.createNotificationChannel(notificationChannel);
        builder.setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSmallIcon(icon)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setColor(R.color.color_241e61)
//                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_MIN)
                .setContentIntent(resultPendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
        if(isRingtone) {
            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_INSISTENT;
            notificationManagerCompat.notify(1, notification);
        }
        else
            notificationManagerCompat.notify(1, builder.build());

    }

}
