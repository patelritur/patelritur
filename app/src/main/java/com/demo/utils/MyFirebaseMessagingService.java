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
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cometchat.pro.core.Call;
import com.cometchat.pro.helpers.CometChatHelper;
import com.cometchat.pro.models.BaseMessage;
import com.demo.DemoApp;
import com.demo.R;
import com.demo.home.HomeActivity;
import com.demo.utils.comectChat.UIKitConstants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "2";
    private static final String CHANNEL_ONE_NAME = "Channel One";
    private Intent notificationIntent;
    private PendingIntent resultPendingIntent;
    private JSONObject json;
    private Call call;
    private static final int REQUEST_CODE = 12;
    private boolean isRingtone=false;
    private SharedPrefUtils sharedPrefUtils;


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e("newToken", token);


    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sharedPrefUtils = new SharedPrefUtils(getApplicationContext());
        Log.e("remoteMessage", String.valueOf(remoteMessage.getData()));
        isRingtone=false;
        // {"customerid":"48","demoid":"264","contenturl":"","contentTitle":"Change Demo Status","message":"Your Demo is booked!","notificationtype":"ChangeDemoStatus"}
        if(remoteMessage.getData().get("notificationtype")!=null && (remoteMessage.getData().get("notificationtype").equalsIgnoreCase("AcceptDemoRequest") ||
                remoteMessage.getData().get("notificationtype").equalsIgnoreCase("AcceptMeetRequest"))){

            notificationIntent = new Intent(getApplicationContext(), HomeActivity.class).
                    putExtra("demoid",remoteMessage.getData().get("demoid")).
                    putExtra("notificationtype",remoteMessage.getData().get("notificationtype")).
                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP).
                    setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(notificationIntent);
            if(remoteMessage.getData().get("notificationtype").equalsIgnoreCase("AcceptMeetRequest")){
               sharedPrefUtils.saveData(Constants.BOOK_TYPE_S,"Meeting");
            }
            else
                sharedPrefUtils.saveData(Constants.BOOK_TYPE_S,"Demo");

            sharedPrefUtils.saveData(Constants.BOOKING_ONGOING,remoteMessage.getData().get("demoid"));
            resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            broadcastNotification(remoteMessage);
            sendPushNotification(getApplicationContext(),remoteMessage.getData().get("message"),remoteMessage.getData().get("contentTitle"));
        }
        else  if(remoteMessage.getData().get("notificationtype")!=null && (remoteMessage.getData().get("notificationtype").equalsIgnoreCase("ChangeDemoStatus") ||
                remoteMessage.getData().get("notificationtype").equalsIgnoreCase("ChangeMeetingStatus"))
                ||remoteMessage.getData().get("notificationtype").equalsIgnoreCase("SpecialistDemoVoiceRecording")
                || remoteMessage.getData().get("notificationtype").equalsIgnoreCase("SpecialistMeetVoiceRecording")){
            if(remoteMessage.getData().get("notificationtype").equalsIgnoreCase("SpecialistDemoVoiceRecording")||
                    remoteMessage.getData().get("notificationtype").equalsIgnoreCase("SpecialistMeetVoiceRecording")){
                isRingtone=true;
            }

            if(remoteMessage.getData().get("notificationtype").equalsIgnoreCase("ChangeDemoStatus") ||
                    remoteMessage.getData().get("notificationtype").equalsIgnoreCase("SpecialistDemoVoiceRecording")) {
                Constants.BOOK_TYPE = "Demo";
                Constants.BOOKING_ID = remoteMessage.getData().get("demoid");
            }
            else {
                Constants.BOOK_TYPE = "Meeting";
                Constants.MEETING_ID = remoteMessage.getData().get("demoid");
            }
            if(remoteMessage.getData().get("message").contains("Rejected")||
            remoteMessage.getData().get("message").contains("rejected")
            || remoteMessage.getData().get("message").contains("completed")
            || remoteMessage.getData().get("message").contains("Completed")){
                sharedPrefUtils.saveData(Constants.BOOK_TYPE_S,"null");
                sharedPrefUtils.saveData(Constants.BOOKING_ONGOING,"null");
            }

            notificationIntent = new Intent(getApplicationContext(), HomeActivity.class).
                    putExtra("demoid",remoteMessage.getData().get("demoid")).
                    putExtra("comeFrom",remoteMessage.getData().get("notifications")).
                    putExtra("notificationtype",remoteMessage.getData().get("notificationtype")).
                    putExtra("customerid",remoteMessage.getData().get("customerid")).
                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP).
                    setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(notificationIntent);
            resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            broadcastNotification(remoteMessage);
            sendPushNotification(getApplicationContext(), remoteMessage.getData().get("message"), remoteMessage.getData().get("contentTitle"));
        }
        else{
            try {
                Log.e("remoteMessage", remoteMessage.getData().toString());
                json = new JSONObject(remoteMessage.getData());
                Log.d("TAG", "JSONObject: "+json.toString());
                BaseMessage baseMessage = CometChatHelper.processMessage(new JSONObject(remoteMessage.getData().get("message")));
                if (baseMessage instanceof Call){
                    call = (Call)baseMessage;
                }

                showCallNotifcation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void broadcastNotification(RemoteMessage remoteMessage) {
        Intent notificationBroadcastIntent = new Intent("receiveNotification");
        notificationBroadcastIntent.putExtra("demoid", remoteMessage.getData().get("demoid")).
                putExtra("notificationtype", remoteMessage.getData().get("notificationtype"));
        getApplicationContext().sendBroadcast(notificationBroadcastIntent);
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
                                getApplicationContext().getPackageName() +
                                "/" +
                                R.raw.notification_sound);

                NotificationCompat.Builder notificationBuilder;
                notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(icon)
                        .setContentTitle(remoteMessage)
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
                        getApplicationContext().getPackageName() +
                        "/" +
                        R.raw.notification_sound);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1");
        NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(this);
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




    @SuppressLint("RemoteViewLayout")
    private void showCallNotifcation() {

        try {
            //    if (isCall)
            {
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

                Uri soundUri = Uri.parse(
                        "android.resource://" +
                                getApplicationContext().getPackageName() +
                                "/" +
                                R.raw.incoming_call);

                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .build();
                @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                        CHANNEL_ONE_NAME, notificationManagerCompat.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setShowBadge(true);

                notificationChannel.setSound(soundUri, audioAttributes);
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManagerCompat.createNotificationChannel(notificationChannel);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"2")
                        .setSmallIcon(R.mipmap.app_icon)
                        .setContentTitle(json.getString("title"))
                        .setContentText(json.getString("alert"))
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.mipmap.app_icon))
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

//                if (json.getString("alert").equals("Incoming audio call") || json.getString("alert").equals("Incoming video call"))
                {
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                    builder.setSound(soundUri);
                    RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.custom_call_notification);
                    notificationLayout.setTextViewText(R.id.caller_name,json.getString("title"));
                    notificationLayout.setTextViewText(R.id.call_type,json.getString("alert"));
                    notificationLayout.setOnClickPendingIntent(R.id.decline_incoming, PendingIntent.getBroadcast(getApplicationContext(), 1, getCallIntent("Decline"), PendingIntent.FLAG_UPDATE_CURRENT));
                    notificationLayout.setOnClickPendingIntent(R.id.accept_incoming, PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, getCallIntent("Answers"), PendingIntent.FLAG_UPDATE_CURRENT));

                    builder.setCustomBigContentView(notificationLayout);
                    builder.setCustomContentView(notificationLayout);
                    builder.setSmallIcon(R.mipmap.app_icon);
                    //    builder.addAction(R.id.decline_incoming, "Decline", PendingIntent.getBroadcast(getApplicationContext(), 1, getCallIntent("Decline"), PendingIntent.FLAG_UPDATE_CURRENT));
                    //  builder.addAction(R.id.accept_incoming, "Answer", PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, getCallIntent("Answers"), PendingIntent.FLAG_UPDATE_CURRENT));
                }
                if (!DemoApp.isForeground) {
                    notificationManagerCompat.notify(2, builder.build());
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private Intent getCallIntent(String title){
        Intent callIntent = new Intent(getApplicationContext(), CallNotificationAction.class);
        callIntent.putExtra(UIKitConstants.IntentStrings.SESSION_ID,call.getSessionId());
        callIntent.putExtra(UIKitConstants.IntentStrings.TYPE,call.getType());
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setAction(title);
        return callIntent;
    }


}
