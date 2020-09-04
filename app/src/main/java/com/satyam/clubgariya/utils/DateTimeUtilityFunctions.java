package com.satyam.clubgariya.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtilityFunctions {
    private static final String TAG = "DateTimeUtilityFunction";
    private static DateTimeUtilityFunctions instance;
    private  Calendar calendar;
    private  TimeZone timeZone;
    private SimpleDateFormat formatter;
    private String finalValue;
    private SimpleDateFormat sdf;

    public static DateTimeUtilityFunctions getInstance() {
        if (instance == null)
            instance = new DateTimeUtilityFunctions();
        return instance;
    }

    public String timeStampToDateTime(String dateTimeFormat,long timeStamp){
        // Format i:e  "dd/MM/yyyy HH:mm:ss"
        calendar = Calendar.getInstance();
//        timeZone = calendar.getTimeZone();
        /* debug: is it local time? */
//        Log.d("Time zone: ", timeZone.getDisplayName());

        /* date formatter in local timezone */
        sdf = new SimpleDateFormat(dateTimeFormat);
//        sdf.setTimeZone(timeZone);

        /* print your timestamp and double check it's the date you expect */
//        finalValue = sdf.format(new Date(timeStamp * 1000)); // I assume your timestamp is in seconds and you're converting to milliseconds?
        finalValue = sdf.format(new Date(timeStamp)); // I assume your timestamp is in seconds and you're converting to milliseconds?
        Log.d("Time: ", finalValue);
        return finalValue;
    }

    public long getCurrentTime(){
        return System.currentTimeMillis();
    }

    public String getDateTime(String dateTimeFormat){
        // Format i:e  "dd/MM/yyyy HH:mm:ss"
        String value=null;
        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        Date date = new Date();
        value=formatter.format(date);
        return value;
    }
}
