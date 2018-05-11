package com.example.udacity.karthikeyan.mypopularmovies.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneralUtils {

    private static Date getFormattedDate(String aDate, String aFormat)
    {
       try {
           @SuppressLint("SimpleDateFormat")
           SimpleDateFormat simpleDateFormat = new SimpleDateFormat(aFormat);
           return simpleDateFormat.parse(aDate);
       }
       catch(ParseException e)
       {
           e.printStackTrace();
           return null;
       }
    }

    public static String getLocalizedDate(Context aContext, String aDate, String aFormat) {
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(aContext);
            return dateFormat.format(getFormattedDate(aDate, aFormat));
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }
}
