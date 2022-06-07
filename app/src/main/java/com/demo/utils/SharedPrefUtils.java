package com.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {

    private static final String PREF_APP = "pref_app";
    private final SharedPreferences.Editor mEditor;
    private final SharedPreferences mSettings;
    public SharedPrefUtils(Context ctx) {
        mSettings = ctx.getSharedPreferences(PREF_APP,
                Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    /**
     * Gets boolean data.
     *
     * @param key     the key
     * @return the boolean data
     */
     public boolean getBooleanData( String key) {

        return mSettings.getBoolean(key, false);
    }

    /**
     * Gets int data.
     *
     * @param context the context
     * @param key     the key
     * @return the int data
     */
     public int getIntData(Context context, String key) {
        return mSettings.getInt(key, 0);
    }

     public Long getLongData(Context context, String key) {
        return mSettings.getLong(key, 0);
    }

    /**
     * Gets string data.
     *
     * @param context the context
     * @param key     the key
     * @return the string data
     */
    // Get Data
     public String getStringData(Context context, String key) {
        return mSettings.getString(key, null);
    }

    public String getStringData( String key) {
        return mSettings.getString(key, null);
    }

    /**
     * Save data.
     *
     * @param key     the key
     * @param val     the val
     */
    // Save Data
    public void saveData( String key, String val) {
        mEditor.putString(key, val).apply();
    }

    public void saveData( String key, boolean val) {
        mEditor.putBoolean(key, val).apply();
    }
    /**
     * Save data.
     *
     * @param context the context
     * @param key     the key
     * @param val     the val
     */
     public void saveData(Context context, String key, int val) {
        mEditor.putInt(key, val).apply();
    }

     public void saveData(Context context, String key, long val) {
        mEditor.putLong(key, val).apply();
    }

    /**
     * Save data.
     *
     * @param context the context
     * @param key     the key
     * @param val     the val
     */
     public void saveData(Context context, String key, boolean val) {
        mEditor
                .putBoolean(key, val)
                .apply();
    }




     public void clearData(Context context){
        mEditor.clear();
        mEditor.commit();
        mEditor.apply();

    }
}
