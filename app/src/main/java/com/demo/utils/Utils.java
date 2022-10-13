package com.demo.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;

import com.cometchat.pro.helpers.Logger;
import com.demo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    /**
     * Sets focused pin bg.
     *
     * @param view    the view
     * @param context the context
     */
    static final String[] tensNames = { "", " ten", " twenty", " thirty", " forty",
            " fifty", " sixty", " seventy", " eighty", " ninety" };

    static final String[] numNames = { "", " one", " two", " three", " four", " five",
            " six", " seven", " eight", " nine", " ten", " eleven", " twelve", " thirteen",
            " fourteen", " fifteen", " sixteen", " seventeen", " eighteen", " nineteen" };
    public static void setFocusedPinBg(EditText view, Context context) {
        ColorStateList colorStateList = ColorStateList.valueOf(context.getColor(R.color.color_241e61));
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

    public static boolean isvalidDl(String dlNumber){
        if(dlNumber==null)
            return false;
        final String pattren = "^(([A-Z]{2}[0-9]{2})( )|([A-Z]{2}-[0-9]{2}))((19|20)[0-9][0-9])[0-9]{7}$";
        Matcher matcher;
        Pattern pattern = Pattern.compile(pattren);

        matcher = pattern.matcher(dlNumber);
        return matcher != null && matcher.matches();
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
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null
                && permissions != null) {
            for (String permission : permissions) {
                Logger.error("TAG", " hasPermissions() : Permission : " + permission
                        + "checkSelfPermission : " + ActivityCompat.checkSelfPermission(context, permission));
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String getDayOfWeek() {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
// 3 letter name form of the day
        return (new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
    }

    public static long getNextTime(int format) {
        Calendar date = Calendar.getInstance();
        System.out.println("Current Date and TIme : " + date.getTime());
        long timeInSecs = date.getTimeInMillis();
        Date afterAdding10Mins = new Date(timeInSecs + (format * 1000));

        return afterAdding10Mins.getTime();

    }
    public static String DateFormater (String date)
    {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("dd/MM/yyyy");  //format of the date which you send as parameter(if the date is like 08-Aug-2016 then use dd-MMM-yyyy)
        String s = "";
        try {
            Date dt = sdf.parse(date);
            sdf = new SimpleDateFormat("dd-MMM-YY");
            s = sdf.format(dt);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static boolean DateAfter(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate.after(new Date());
    }

    public static String EnglishNumberToWords (String number){

        {
            String twodigitword="";
            String word="";
            String[] HTLC = {"", "Hundred", "Thousand", "Lakh", "Crore"}; //H-hundread , T-Thousand, ..
            int split[]={0,2, 3, 5, 7,9};
            String[] temp=new String[split.length];
            boolean addzero=true;
            int len1=number.length();
            if (len1>split[split.length-1]) { System.out.println("Error. Maximum Allowed digits "+ split[split.length-1]);
                System.exit(0);
            }
            for (int l=1 ; l<split.length; l++ )
                if (number.length()==split[l] ) addzero=false;
            if (addzero==true) number="0"+number;
            int len=number.length();
            int j=0;
            //spliting & putting numbers in temp array.
            while (split[j]<len)
            {
                int beg=len-split[j+1];
                int end=beg+split[j+1]-split[j];
                temp[j]=number.substring(beg , end);
                j=j+1;
            }

            for (int k=0;k<j;k++)
            {
                twodigitword=ConvertOnesTwos(temp[k]);
                if (k>=1){
                    if (twodigitword.trim().length()!=0) word=twodigitword+" " +HTLC[k] +" "+word;
                }
                else word=twodigitword ;
            }
            return (word);
        }


    }



    private static String ConvertOnesTwos(String t)
    {
        final String[] ones ={"", "One", "Two", "Three", "Four", "Five","Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve","Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
        final String[] tens = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty","Ninety"};

        String word="";
        int num=Integer.parseInt(t);
        if (num%10==0) word=tens[num/10]+" "+word ;
        else if (num<20) word=ones[num]+" "+word ;
        else
        {
            word=tens[(num-(num%10))/10]+word ;
            word=word+" "+ones[num%10] ;
        }
        return word;
    }


    public static void showDilaog(Dialog recordDialog) {
        recordDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = recordDialog.getWindow().getAttributes();
        recordDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        lp.dimAmount = 0.8f;
        recordDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        recordDialog.show();
    }
}
