package com.example.udacity.karthikeyan.mypopularmovies.Utilities;




import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.udacity.karthikeyan.mypopularmovies.BuildConfig;
import com.example.udacity.karthikeyan.mypopularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.toString();


    /**
     * Creates and returns an URL.
     *
     * @param sort_by_param Parameters to be used in the API call
     * @return URL formatted with parameters for the API
     * @throws MalformedURLException
     */
    public static URL getApiUrl(Context context, String sort_by_param) {

     final String TMDB_BASE_URL = context.getString(R.string.TMDB_BASE_URL);
     final String SORT_BY_PARAM = context.getString(R.string.SORT_BY_PARAM);
     final String API_KEY_PARAM = context.getString(R.string.API_KEY_PARAM);
     final String LANGUAGE_PARAM = context.getString(R.string.LANGUAGE_KEY_PARAM);
     final String API_KEY = BuildConfig.MY_MOVIE_DB_API_KEY;



        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendQueryParameter(SORT_BY_PARAM, sort_by_param)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, String.valueOf(Locale.getDefault()))
                .build();

        try {
            URL movieURL = new URL(builtUri.toString());
            Log.d(TAG,"URL: " + movieURL );
            return movieURL;

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            Log.d(TAG, "JSON String: "+ response);
            return response;
            
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    //Helper to check the network connection
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


}
