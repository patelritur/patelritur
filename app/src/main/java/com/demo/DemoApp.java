package com.demo;

import android.app.Application;

import com.demo.webservice.RestClient;

public class DemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new RestClient(getApplicationContext());
    }
}
