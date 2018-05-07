package com.example.udacity.karthikeyan.mypopularmovies.Model;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.udacity.karthikeyan.mypopularmovies";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_MOVIES = "movies";


    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVG = "vote_average";
        public static final String COLUMN_REL_DATE = "release_date";
        public static final String COLUMN_MOVIE_ID = "tmdb_movie_id";


        public static Uri buildUriWithMovieID(int movieID) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(movieID))
                    .build();
        }


    }
}
