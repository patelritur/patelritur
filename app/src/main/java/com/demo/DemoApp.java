package com.demo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.demo.utils.Constants;
import com.demo.utils.comectChat.call_manager.listener.CometChatCallListener;
import com.demo.webservice.RestClient;
import com.google.firebase.FirebaseApp;


public class DemoApp extends Application implements LifecycleObserver {
    public static boolean isForeground;

    public static void setBookingConfirmedFragmentVisible(boolean b) {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        new RestClient(getApplicationContext());
        FirebaseApp.initializeApp(this);
        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(Constants.region).build();

        CometChat.init(this, Constants.appID,appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
            }
            @Override
            public void onError(CometChatException e) {
            }
        });
        CometChatCallListener.addCallListener("DemoApp",this);
//        createNotificationChannel();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        isForeground=true;
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        isForeground = true;
    }

}
