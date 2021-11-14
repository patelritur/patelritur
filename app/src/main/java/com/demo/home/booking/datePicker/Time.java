package com.demo.home.booking.datePicker;

import com.demo.utils.PrintLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Time

{
    ArrayList<String> timeSlotList = new ArrayList<>();
    private String getNextTime(String startTime)
    {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");

        try {
            Calendar now = Calendar.getInstance();
            Date date =df.parse(startTime);
            date.setHours(date.getHours());
            date.setMinutes(date.getMinutes());
            now.setTime(date);
            now.add(Calendar.MINUTE, 30);
            return df.format(now.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }



    private Date getTime(String endTime)
    {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
        Date date = null;
        Calendar now = Calendar.getInstance();
        try {
            date = df.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date.setHours(date.getHours());
        date.setMinutes(date.getMinutes());
        now.setTime(date);
        return  date;
    }

    public ArrayList<String> getNextTime(String startTime, String endTime)
    {
       String nextTime = startTime;

        timeSlotList.add(nextTime);
        try {
            do{

                nextTime = getNextTime(nextTime);
                timeSlotList.add(nextTime);
            }
            while(getTime(endTime).getTime()>getTime(nextTime).getTime());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeSlotList;

    }
}
