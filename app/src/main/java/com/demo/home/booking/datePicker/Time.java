package com.demo.home.booking.datePicker;

import com.demo.utils.PrintLog;

import org.joda.time.DateTime;

import java.text.DateFormat;
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

    public ArrayList<String> getNextTime(DateTime dateSelected,String startTime, String endTime)
    {
       String nextTime = startTime;

        //timeSlotList.add(nextTime);
        try {
            do{


                SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                Date nexttime = parseFormat.parse(nextTime);
                System.out.println(parseFormat.format(nexttime) + " = " + displayFormat.format(nexttime));
                nexttime = displayFormat.parse(displayFormat.format(nexttime));

             //   DateFormat dateFormat = new SimpleDateFormat("HH:mm a");
             //   Date nexttime=dateFormat.parse(nextTime);
//                PrintLog.v("nexttiem","ner"+nexttime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateSelected.toDate());
                calendar.set(Calendar.HOUR_OF_DAY,nexttime.getHours());
                calendar.set(Calendar.MINUTE,nexttime.getMinutes());
                if(Calendar.getInstance().getTime().before(calendar.getTime())){
                    timeSlotList.add(nextTime);
                }

                nextTime = getNextTime(nextTime);
            }
            while(getTime(getNextTime(endTime)).getTime()>getTime(nextTime).getTime());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeSlotList;

    }
}
