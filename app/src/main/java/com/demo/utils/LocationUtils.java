package com.demo.utils;

import static com.demo.utils.Constants.LONGITUDE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.demo.R;
import com.demo.home.booking.model.BookingResponseModel;
import com.demo.home.booking.model.MapLocationResponseModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class LocationUtils implements OnMapReadyCallback {

    private final LocationManager locationManager;
    private final LocationListener locationListener;
    private GoogleMap map;
    private static final int DEFAULT_ZOOM = 24;
    private Context context;
    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private MarkerOptions currentMarker;
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

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
            /*   map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                       new LatLng(location.getLatitude(),
                               location.getLongitude()), DEFAULT_ZOOM));*/
                loc = location;
                Constants.LATITUDE = String.valueOf(location.getLatitude());
                Constants.LONGITUDE = String.valueOf(location.getLongitude());
                currentMarker = (new MarkerOptions().position(new LatLng(loc.getLatitude(),loc.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.mipmap.customer)));



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

                UiSettings uiSettings = map.getUiSettings();
                uiSettings.setZoomGesturesEnabled(true);
                uiSettings.setZoomControlsEnabled(true);
                uiSettings.setMyLocationButtonEnabled(true);

                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null) {
                    Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastKnownLocationGPS != null) {
                        loc = lastKnownLocationGPS;
                    } else {
                        Location loc =  locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        this.loc = loc;
                    }
                    if(loc==null)
                        return;
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(this.loc.getLatitude(),
                                    this.loc.getLongitude()), DEFAULT_ZOOM));
                    currentMarker = (new MarkerOptions().position(new LatLng(loc.getLatitude(),loc.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.mipmap.customer)));

                    map.addMarker(currentMarker);
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

    public void setLocationOnMap(List<MapLocationResponseModel.Maplocationlist> mapLocationResponseModel) {

        for(int i=0;i<mapLocationResponseModel.size();i++) {
            final BitmapDescriptor[] bitmapDescriptor = new BitmapDescriptor[1];
            Double latitude = Double.valueOf(mapLocationResponseModel.get(i).getLatitude());
            Double longitude = Double.valueOf(mapLocationResponseModel.get(i).getLongitude());
            int finalI = i;
            Glide.with(context)
                    .asBitmap()
                    .load(mapLocationResponseModel.get(i).getMapIcon())
                    .into(new SimpleTarget<Bitmap>() {


                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                             bitmapDescriptor[0] = BitmapDescriptorFactory.fromBitmap(resource);
                            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude)).title(mapLocationResponseModel.get(finalI).getLocationID()).snippet(mapLocationResponseModel.get(finalI).getLocationType()).icon(bitmapDescriptor[0]);
                            map.addMarker(marker);
                            builder.include(marker.getPosition());
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

        }
        zoomMapTocontainsAll(builder);

    }

    private void zoomMapTocontainsAll(LatLngBounds.Builder builder) {

        builder.include(currentMarker.getPosition());
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        map.animateCamera(cu);

    }

    public void setLocationOnMapForDealer(List<BookingResponseModel.Locationlist> maplocationlist) {
        map.clear();
        for(int i=0;i<maplocationlist.size();i++) {
            final BitmapDescriptor[] bitmapDescriptor = new BitmapDescriptor[1];
            Double latitude = Double.valueOf(maplocationlist.get(i).getLatitude());
            Double longitude = Double.valueOf(maplocationlist.get(i).getLongitude());
            int finalI = i;
            Glide.with(context)
                    .asBitmap()
                    .load(maplocationlist.get(i).getMapIcon())
                    .into(new SimpleTarget<Bitmap>() {




                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            bitmapDescriptor[0] = BitmapDescriptorFactory.fromBitmap(resource);
                            MarkerOptions marker = (new MarkerOptions().position(new LatLng(latitude,longitude)).title(maplocationlist.get(finalI).getShowroomID()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.dealership)));
                            map.addMarker(marker);
                            builder.include(marker.getPosition());
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });




        }
        map.addMarker(currentMarker);
        zoomMapTocontainsAll(builder);
    }

    public void drawOnMap(String specialistLatitude, String specialistLongitude) {
        map.clear();
        map.addMarker(currentMarker);
        Double latitude = Double.valueOf(specialistLatitude);
        Double longitude = Double.valueOf(specialistLongitude);
        MarkerOptions marker = (new MarkerOptions().position(new LatLng(latitude,longitude)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.dealership)));
        map.addMarker(marker);
        builder.include(marker.getPosition());
        zoomMapTocontainsAll(builder);
        Polyline polyline1 = map.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(latitude,longitude),
                        new LatLng(loc.getLatitude(),loc.getLongitude())));
        // Store a data object with the polyline, used here to indicate an arbitrary type.
        polyline1.setTag("A");
        // Style the polyline.

// Getting URL to the Google Directions API
        /*String url =  getDirectionsUrl(new LatLng(loc.getLatitude(),loc.getLongitude()),new LatLng(latitude,longitude));
        DownloadTask downloadTask =new DownloadTask();
        downloadTask.execute(url);*/

    }

    public void clearmap() {
        map.clear();
        if(currentMarker!=null)
        map.addMarker(currentMarker);
    }

    /*private String getDirectionsUrl(LatLng origin, LatLng dest) {

// Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String parameters = str_origin + "&" + str_dest ;

        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters +"&api_key=AIzaSyDjF61on-ptfekCYFNWv4ZF3oe15-S9BzY";

        return url;
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

// For storing data from web service
            String data = "";

            try{
// Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
// doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(map);

// Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

// Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

// Connecting to url
            urlConnection.connect();

// Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }*/

}
