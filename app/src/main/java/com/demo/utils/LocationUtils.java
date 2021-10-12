package com.demo.utils;

import static com.demo.utils.Constants.LONGITUDE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class LocationUtils implements OnMapReadyCallback {

    private final LocationManager locationManager;
    private final LocationListener locationListener;
    private GoogleMap map;
    private static final int DEFAULT_ZOOM = 15;
    private Context context;
    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    public Location getLoc() {
        return loc;
    }

    private Location loc;


    public LocationUtils(Context context, SupportMapFragment mapFragment)
   {
       mapFragment.getMapAsync(this);
       this.context = context;
       locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

       //To get users permissions and updates about device movement
       locationListener = new LocationListener() {
           @Override
           public void onLocationChanged(Location location) {
               map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                       new LatLng(location.getLatitude(),
                               location.getLongitude()), DEFAULT_ZOOM));
               loc = location;
               Constants.LATITUDE = String.valueOf(location.getLatitude());
               Constants.LONGITUDE = String.valueOf(location.getLongitude());


           }

           @Override
           public void onStatusChanged(String provider, int status, Bundle extras) {

           }

           @Override
           public void onProviderEnabled(String provider) {

           }

           @Override
           public void onProviderDisabled(String provider) {

           }
       };

   }
    private boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        getLocationPermission();
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (Permissionsutils.CheckForLocationPermission(context)) {
            locationPermissionGranted = true;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,locationListener);
            if(isLocationEnabled(context))
                getDeviceLocation();
            else
                askForLocation();
        } else {
            Permissionsutils.askForLocationPermission(context);
        }
    }
    private void askForLocation() {
        new GPSUtils(context).turnGPSOn(new GPSUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                getDeviceLocation();
            }
        });
    }

    private void getDeviceLocation() {

        try {
            if (locationPermissionGranted) {
                if(map==null)
                    return;
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null) {
                    Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastKnownLocationGPS != null) {
                        loc = lastKnownLocationGPS;
                    } else {
                        Location loc =  locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        this.loc = loc;
                    }
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(this.loc.getLatitude(),
                                    this.loc.getLongitude()), DEFAULT_ZOOM));
                }
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,locationListener);
                    if(isLocationEnabled(context))
                        getDeviceLocation();
                    else
                        askForLocation();
                }
            }
        }

    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            getDeviceLocation();

    }

}
