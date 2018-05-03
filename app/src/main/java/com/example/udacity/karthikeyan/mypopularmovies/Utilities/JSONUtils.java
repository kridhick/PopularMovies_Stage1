package com.example.udacity.karthikeyan.mypopularmovies.Utilities;

import android.content.Context;
import android.util.Log;

import com.example.udacity.karthikeyan.mypopularmovies.Model.Movie;
import com.example.udacity.karthikeyan.mypopularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"finally", "ReturnInsideFinallyBlock"})
public class JSONUtils {

    private static final String TAG = JSONUtils.class.toString();


    @SuppressWarnings("finally")
    public static List<Movie> parseMovieJson(Context context, final String TMDB_JSON)
    {
        List<Movie> movieList = null;

        final String TAG_RESULTS = context.getString(R.string.TAG_RESULTS);
        final String TAG_ORIGINAL_TITLE = context.getString(R.string.TAG_ORIGINAL_TITLE);
        final String TAG_POSTER_PATH = context.getString(R.string.TAG_POSTER_PATH);
        final String TAG_OVERVIEW = context.getString(R.string.TAG_OVERVIEW);
        final String TAG_VOTE_AVERAGE = context.getString(R.string.TAG_VOTE_AVERAGE);
        final String TAG_RELEASE_DATE = context.getString(R.string.TAG_RELESE_DATE);
        //Used to Append to Poster Path
        final String TMDB_POSTER_BASE_URL = context.getString(R.string.TMDB_POSTER_BASE_URL);

        try {
            JSONObject tmdbJson = new JSONObject(TMDB_JSON);
            JSONArray resultsArray = tmdbJson.getJSONArray(TAG_RESULTS);
            movieList = new ArrayList<>(resultsArray.length());

            for (int i=0; i < resultsArray.length(); i++)
            {

                JSONObject movieInfo = resultsArray.getJSONObject(i);

                String movieTitle = movieInfo.getString(TAG_ORIGINAL_TITLE);
                String posterPath = movieInfo.optString(TAG_POSTER_PATH);
                String overview = movieInfo.optString(TAG_OVERVIEW);
                Double voteAverage = movieInfo.optDouble(TAG_VOTE_AVERAGE);
                String releaseDate = movieInfo.optString(TAG_RELEASE_DATE);

                Movie movieItem = new Movie(movieTitle, TMDB_POSTER_BASE_URL + posterPath, overview, voteAverage, releaseDate);

                movieList.add(movieItem);
            }



        }
        catch(JSONException e)
        {
            Log.d(TAG, "An error had occurred on JSON Parsing");
            e.printStackTrace();
            return null;
        }
        finally{
            return movieList;
        }



    }
}
