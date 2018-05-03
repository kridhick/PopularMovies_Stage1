package com.example.udacity.karthikeyan.mypopularmovies.Model;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.example.udacity.karthikeyan.mypopularmovies.R;
import com.example.udacity.karthikeyan.mypopularmovies.Utilities.JSONUtils;
import com.example.udacity.karthikeyan.mypopularmovies.Utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MovieRepositary {

    private static final String TAG = MovieRepositary.class.getSimpleName();
    private final Context mContext;



    //Constructor
    public MovieRepositary(Context context) {
        this.mContext = context;

    }

}





