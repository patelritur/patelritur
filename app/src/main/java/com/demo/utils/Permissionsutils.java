package com.demo.utils;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class Permissionsutils {


    public static boolean checkForStoragePermission(Context context)
    {
        //File write logic here
        return checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static boolean checkForNotificationPermission(Context context)
    {
        //File write logic here
        return checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED ;
    }



    public static boolean CheckForLocationPermission(Context context)
    {
        //File write logic here
        return checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static void askForLocationPermission(Context context)
    {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    public static void askForStoragePermission(Context context)
    {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }
    public static void askForStoragePermission(Context context,int reqCode)
    {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, reqCode);
    }
    public static boolean checkForRecordPermission(Context activity) {
        // this method is used to check permission
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkForCameraPermission(Context activity) {
        // this method is used to check permission
        return checkSelfPermission(activity, CAMERA) == PackageManager.PERMISSION_GRANTED &&
        checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ;

    }


    public static void askForCameraPermission(Context context,int reqCode)
    {
        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, reqCode);

    }

    public static void askForRecordPermission(Context context) {
        // this method is used to request the
        // permission for audio recording and storage.
        ActivityCompat.requestPermissions((Activity) context, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, 1);
    }



    public static void askForNotificationPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{ POST_NOTIFICATIONS}, 10);

    }
}
