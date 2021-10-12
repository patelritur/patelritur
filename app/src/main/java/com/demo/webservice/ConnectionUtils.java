package com.demo.webservice;

import android.Manifest.permission;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresPermission;


/**
 * This class is used for checking if app is connected to internet or not
 * Mainly call before calling api
 */

public class ConnectionUtils {

    private ConnectionUtils() {
        throw new UnsupportedOperationException(
                "Should not create getInstance of Util class. Please use as static..");
    }

    /**
     * Get the network info
     *
     * @param context the context
     * @return network info
     */
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Checks the type of data connection that is currently available on the device.
     *
     * @param ctx the ctx
     * @return <code>ConnectivityManager.TYPE_*</code> as a type of internet connection on the device. Returns -1 in case of error or none of <code>ConnectivityManager.TYPE_*</code> is found.
     */

    /**
     * Check if there is any connectivity
     *
     * @param context the context
     * @return boolean boolean
     */
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }




    public static boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


}