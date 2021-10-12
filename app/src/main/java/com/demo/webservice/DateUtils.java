package com.demo.webservice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Provides helper methods for date utilities.
 */
public class DateUtils {

    /***
     * Converts ISO date string to UTC timezone equivalent.
     *
     * @return the utc time
     */
    public static String getUtcTFormat() {
        return "yyyy-MM-dd'T'HH:mm:ssZ";
    }

    /****
     * Parses date string and return mapView {@link Date} object
     *
     * @param date the date
     * @return The ISO formatted date object
     */
    private static Date parseDate(String date) {

        if (date == null) {
            return null;
        }

        StringBuffer sbDate = new StringBuffer();
        sbDate.append(date);
        String newDate = null;
        Date dateDT = null;

        try {
            newDate = sbDate.substring(0, 19);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String rDate = newDate.replace("T", " ");
        String nDate = rDate.replaceAll("-", "/");

        try {
            dateDT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).parse(nDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateDT;
    }


    /**
     * Gets elapsed time.
     *
     * @param time ISO formatted time when the event occurred in local time zone.
     * @return the elapsed time
     * @deprecated Totally bloated code. Calculates the elapsed time after the given parameter date.
     */
    public static String getElapsedTime(String time) {
        TimeZone defaultTimeZone = TimeZone.getDefault();

        // TODO: its advisable not to use this method as it changes the
        // timezone.
        // Change it at some time in future.
        //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        Date eventTime = DateUtils.parseDate(time);

        Date currentDate = new Date();

        long diffInSeconds = (currentDate.getTime() - eventTime.getTime()) / 1000;
        String elapsed;
        long mins = diffInSeconds / 60;
        long hours = diffInSeconds / (60 * 60);
        long days = diffInSeconds / 86400;
        long weeks = diffInSeconds / 604800;
        long months = diffInSeconds / 2592000;


        if (diffInSeconds < 120) {
            elapsed = "mapView min ago";
        } else if (mins < 60) {
            elapsed = mins + " mins ago";
        } else if (hours < 24) {
            elapsed = hours + " " + (hours > 1 ? "hrs" : "hr") + " ago";
        } else if (hours < 48) {
            elapsed = "mapView day ago";
        } else if (days < 7) {
            elapsed = days + " days ago";
        } else if (weeks < 5) {
            elapsed = weeks + " " + (weeks > 1 ? "weeks" : "week") + " ago";
        } else if (months < 12) {
            elapsed = months + " " + (months > 1 ? "months" : "months") + " ago";
        } else {
            elapsed = "more than mapView year ago";
        }

        TimeZone.setDefault(defaultTimeZone);

        return elapsed;
    }


    private static boolean isYesterday(long date) {
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(date);
        now.add(Calendar.DATE, -1);
        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);


    }


}