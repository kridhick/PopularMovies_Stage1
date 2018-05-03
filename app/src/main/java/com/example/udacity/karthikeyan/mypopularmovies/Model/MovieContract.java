package com.example.udacity.karthikeyan.mypopularmovies.Model;

import android.provider.BaseColumns;

public class MovieContract {

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_ID = "tmb_movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVG = "vote_average";
        public static final String COLUMN_REL_DATE = "release_date";
        public static final String COLUMN_FAVORITES = "favorites";

    }
}
