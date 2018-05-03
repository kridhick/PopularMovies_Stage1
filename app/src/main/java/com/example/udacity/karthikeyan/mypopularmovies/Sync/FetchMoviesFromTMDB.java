package com.example.udacity.karthikeyan.mypopularmovies.Sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.udacity.karthikeyan.mypopularmovies.Model.Movie;
import com.example.udacity.karthikeyan.mypopularmovies.Utilities.JSONUtils;
import com.example.udacity.karthikeyan.mypopularmovies.Utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

// AsyncTask Class will be executed from Main Activity.
public class FetchMoviesFromTMDB extends AsyncTask<URL, Void, List<Movie>> {

    private static final String TAG = FetchMoviesFromTMDB.class.getSimpleName();


    private final Context mContext;
    private final onGetMoviesCompleted mListener;

    public FetchMoviesFromTMDB(Context context, onGetMoviesCompleted listener)
    {

        super();
        this.mContext = context;
        this.mListener = listener;
    }

    //Interface used to notify the Main Activity when Background Fetch Completed.
    public interface onGetMoviesCompleted {
        void onFetchComplete(List<Movie> movies);
    }



    @Override
    protected List<Movie> doInBackground(URL... urls) {
        Log.d(TAG, "Inside the background thread");

        String mTMDB_JSON = NetworkUtils.getResponseFromHttpUrl(urls[0]);
        List<Movie> mMovieList = JSONUtils.parseMovieJson(mContext, mTMDB_JSON);

        return mMovieList;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        mListener.onFetchComplete(movies);
        Log.d(TAG, "Completed the do background method");
    }
}
