package com.demo.utils;

import android.util.Log;


/**
 * The type Print log.
 */
public class PrintLog {

    private static final boolean isDebug =true;

    /**
     * Is debug boolean.
     *
     * @return the boolean
     */
    public static boolean isDebug() {
        return isDebug;
    }

    /**
     * E.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void e(String tag, String message) {
        if (isDebug) Log.e("message",""+message);
    }



    /**
     * I.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void i(String tag, String message) {
        if (isDebug) Log.i("message",""+message);
    }

    /**
     * V.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void v(String tag, String message)
    {
        if (isDebug) Log.v(""+tag,""+message);
    }

    /**
     * D.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void d(String tag, String message) {
        if (isDebug) Log.d("message",""+message);
    }

    public static void v(String message) {
        Log.v("message",""+message);
    }
}