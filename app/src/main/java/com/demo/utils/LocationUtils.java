package com.demo.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.demo.R;
import com.demo.home.booking.model.BookingResponseModel;
import com.demo.home.booking.model.MapLocationResponseModel;
import com.demo.home.model.DirectionResults;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class LocationUtils implements OnMapReadyCallback {

    private final LocationManager locationManager;
    private final LocationListener locationListener;
    private GoogleMap map;
    private  Float DEFAULT_ZOOM = 15f;
    private Context context;
    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Marker currentMarker;
    private MarkerOptions markerOptions;
    private boolean isDealership;
    private LatLngBounds.Builder builder;
    private int count=0;

    public Location getLoc() {
        return loc.getValue();
    }

    public MutableLiveData<Location> getMutableLoc() {

        return loc;
    }

    private MutableLiveData<Location> loc = new MutableLiveData<>();


    public LocationUtils(Context context, SupportMapFragment mapFragment) {
        mapFragment.getMapAsync(this);
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //To get users permissions and updates about device movement
        locationListener = new LocationListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onLocationChanged(Location location) {
            /*   map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                       new LatLng(location.getLatitude(),
                               location.getLongitude()), DEFAULT_ZOOM));*/
                loc.setValue(location);
                Constants.LATITUDE = String.valueOf(location.getLatitude());
                Constants.LONGITUDE = String.valueOf(location.getLongitude());
                if (currentMarker == null) {
                    markerOptions = (new MarkerOptions().position(new LatLng(loc.getValue().getLatitude(), loc.getValue().getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.mipmap.customer)));
                    currentMarker = map.addMarker(markerOptions);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(),
                                    location.getLongitude()), DEFAULT_ZOOM));
                } else
                    currentMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                if(isDealership)
                updateCameraBearing(map, location.getBearing(),location);


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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    private void updateCameraBearing(GoogleMap map, float bearing, Location location) {
        if (map == null) return;
        CameraPosition camPos = CameraPosition
                .builder(map.getCameraPosition())// current Camera)
                .bearing(bearing)
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                //.zoom(18f)
                .build();
        PrintLog.v("bear==" + bearing);
      /*  map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(loc.getLatitude(),
                        loc.getLongitude()), DEFAULT_ZOOM));*/
        map.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setPadding(0,0,0,80);
        getLocationPermission();
        map.setOnCameraChangeListener(getCameraChangeListener());
    }

    private GoogleMap.OnCameraChangeListener getCameraChangeListener()
    {
        return new GoogleMap.OnCameraChangeListener()
        {
            @Override
            public void onCameraChange(CameraPosition position)
            {
                Log.d("Zoom", "Zoom: " + position.zoom);


                DEFAULT_ZOOM = position.zoom;
            }
        };
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
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
            if (isLocationEnabled(context))
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
                if (map == null)
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
                        loc.setValue(lastKnownLocationGPS);
                    } else {
                        Location loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        this.loc.setValue(loc);
                    }
                    if (loc.getValue() == null)
                        return;
                    Constants.LATITUDE = String.valueOf(loc.getValue().getLatitude());
                    Constants.LONGITUDE = String.valueOf(loc.getValue().getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(this.loc.getValue().getLatitude(),
                                    this.loc.getValue().getLongitude()), DEFAULT_ZOOM));
                    if (currentMarker == null) {
                        markerOptions = (new MarkerOptions().position(new LatLng(loc.getValue().getLatitude(), loc.getValue().getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.mipmap.customer)));
                        currentMarker = map.addMarker(markerOptions);
                    } else
                        currentMarker.setPosition(new LatLng(loc.getValue().getLatitude(), loc.getValue().getLongitude()));


                }
            }
        } catch (SecurityException e) {
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
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
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
        PrintLog.v("setLocationOnMap==="+mapLocationResponseModel.size());
        map.clear();
         LatLngBounds.Builder builder = new LatLngBounds.Builder();
       currentMarker= map.addMarker(markerOptions);
        builder.include(currentMarker.getPosition());
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
                            zoomMapTocontainsAll(builder);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

        }



    }

    private void zoomMapTocontainsAll(LatLngBounds.Builder builder) {

        builder.include(currentMarker.getPosition());
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
        map.animateCamera(cu);

    }

    public void setLocationOnMapForDealer(List<BookingResponseModel.Locationlist> maplocationlist) {
        map.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
       currentMarker= map.addMarker(markerOptions);
        builder.include(currentMarker.getPosition());
        PrintLog.v("setLocationOnMapForDealer"+maplocationlist.size());
        for(int i=0;i<maplocationlist.size();i++) {
            final BitmapDescriptor[] bitmapDescriptor = new BitmapDescriptor[1];
            PrintLog.v(maplocationlist.get(i).getLatitude());
            PrintLog.v(maplocationlist.get(i).getLongitude());
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
                            MarkerOptions marker = (new MarkerOptions().position(new LatLng(latitude,longitude)).title(maplocationlist.get(finalI).getShowroomID()).icon(bitmapDescriptor[0]));
                            map.addMarker(marker);
                            builder.include(marker.getPosition());
                            zoomMapTocontainsAll(builder);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });





        }

    }


    public void clearmap() {
        if(map!=null)
            map.clear();
        if(markerOptions!=null)
            map.addMarker(markerOptions);
    }




    public void removePolyLine(){
        isDealership=false;
        map.clear();
        currentMarker=  map.addMarker(markerOptions);
    }


    @SuppressLint("SuspiciousIndentation")
    public void addPolyLine(PolylineOptions rectLine, DirectionResults.Location endLocation, String demoType, String durationInMin) {
        PrintLog.v("add");
        if(builder==null)
            builder = new LatLngBounds.Builder();
        map.clear();
      currentMarker=  map.addMarker(markerOptions);
        map.addPolyline(rectLine);
       //
        MarkerOptions marker;
        if(demoType.equalsIgnoreCase("At Home")){
            isDealership=false;
             marker = (new MarkerOptions().position(new LatLng(endLocation.getLat(),endLocation.getLng())).icon(BitmapDescriptorFactory.fromResource(R.mipmap.car)));
            map.addMarker(marker);
            builder.include(marker.getPosition());
//            zoomMapTocontainsAll(builder);
            Location location=new Location("");
            location.setLatitude(endLocation.getLat());
            location.setLongitude(endLocation.getLng());
            updateCameraBearing(map, location.getBearing(), location);
            if(count==0) {
                PrintLog.v("zoomMapTocontainsAll","zoomMapTocontainsAll==1");
                zoomMapTocontainsAll(builder);
                count=1;
            }

        }
        else {
            isDealership=true;
            currentMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.car));
            marker = (new MarkerOptions().position(new LatLng(endLocation.getLat(), endLocation.getLng())).icon(BitmapDescriptorFactory.fromResource(R.mipmap.dealership)).title("Dealership").snippet(durationInMin));
            map.addMarker(marker);
            builder.include(currentMarker.getPosition());
            if(count==0) {
                PrintLog.v("zoomMapTocontainsAll","zoomMapTocontainsAll==1");
                zoomMapTocontainsAll(builder);
                count=1;
            }
        }
      /*  map.addMarker(marker);
        builder.include(marker.getPosition());
        zoomMapTocontainsAll(builder);*/


    }

    public void setZoom(int zoom, int padding) {
        if(map!=null && loc!=null) {
            map.animateCamera(CameraUpdateFactory.zoomTo(zoom));
            if(builder!=null) {
                zoomMapTocontainsAll(builder);
            }
            DEFAULT_ZOOM = Float.valueOf(zoom);
            map.setPadding(0, 0, 0, padding);
            if(loc!=null && loc.getValue()!=null)
            updateCameraBearing(map, loc.getValue().getBearing(), loc.getValue());
        }
    }
}
