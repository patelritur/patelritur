package com.demo;

import android.app.Application;

import com.demo.webservice.RestClient;
import com.google.firebase.FirebaseApp;

public class DemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new RestClient(getApplicationContext());
        FirebaseApp.initializeApp(this);
    }
}
