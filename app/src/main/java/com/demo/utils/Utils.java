package com.demo.utils;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import com.demo.R;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    /**
     * Sets focused pin bg.
     *
     * @param view    the view
     * @param context the context
     */
    public static void setFocusedPinBg(EditText view, Context context) {
        ColorStateList colorStateList = ColorStateList.valueOf(context.getColor(R.color.color_E8505B));
        ViewCompat.setBackgroundTintList(view, colorStateList);
    }

    /**
     * Sets normal pin bg.
     *
     * @param view    the view
     * @param context the context
     */
    public static void setNormalPinBg(EditText view, Context context) {
        ColorStateList colorStateList = ColorStateList.valueOf(context.getColor(R.color.color_b9b9b9));
        ViewCompat.setBackgroundTintList(view, colorStateList);
    }

    /**
     * Hide soft keyboard.
     *
     * @param view the view
     */
    public static void hideSoftKeyboard(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide soft keyboard.
     *
     * @param activity the activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide soft keyboard.
     *
     * @param activity the activity
     */
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
    public static void showToastComingSoon(Context context) {
        Toast.makeText(context,"Coming Soon",Toast.LENGTH_LONG).show();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        final String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Matcher matcher;
        Pattern pattern = Pattern.compile(emailPattern);

        matcher = pattern.matcher(email);
        return matcher != null && matcher.matches();
    }
    public static String getDeviceUniqueID(Activity activity){
        String deviceName = android.os.Build.MODEL;
        String deviceMan = android.os.Build.MANUFACTURER;
        String deviceOs = Build.VERSION.SDK_INT+"";

        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceMan +"|"+deviceName+"|"+deviceOs+"|"+device_unique_id;
    }

    public static void showKeyboard(Context context)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    public static String getGreetingMessage(Context homeActivity) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String greeting = "null";

        if(hour>= 12 && hour < 17){
            greeting = "Good Afternoon";
        } else if(hour >= 17 && hour < 21){
            greeting = "Good Evening";
        } else if(hour >= 21 && hour < 24){
            greeting = "Good Night";
        } else {
            greeting = "Good Morning";
        }

        return greeting;

    }

    public static void cancelJob(Context context) {

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.cancelAll();
    }

    public static void scheduleJob(Context context, int minutes, int meetingWaitingMsgCount) {
        ComponentName serviceComponent = new ComponentName(context, statusJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putInt("count",minutes);
        bundle.putInt("waitCount",meetingWaitingMsgCount);
        builder.setExtras(bundle);
        builder.setMinimumLatency(Math.round((minutes*60 * 1000)/meetingWaitingMsgCount)); // wait at least
        builder.setOverrideDeadline(5 * 1000*60); // maximum delay
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

    public static void shareData(Context context,String title, String message){
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/html");
        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        sendIntent.putExtra(android.content.Intent.EXTRA_TITLE, title);
        ((Activity) context).startActivity(Intent.createChooser(sendIntent, "Share via"));
    }


    public static void openChromewithUrl(Context context,String url) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setPackage("com.android.chrome");
        try {
            context.startActivity(i);
        } catch (ActivityNotFoundException e) {
            i.setPackage(null);
           context.startActivity(i);
        }
    }
}
