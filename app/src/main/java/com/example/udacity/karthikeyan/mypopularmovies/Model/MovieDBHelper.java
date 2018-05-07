package com.example.udacity.karthikeyan.mypopularmovies.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.udacity.karthikeyan.mypopularmovies.Model.MovieContract.MovieEntry;

public class MovieDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieEntry.COLUMN_TITLE      + " TEXT NOT NULL, "                 +
                        MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL,"                 +
                        MovieEntry.COLUMN_OVERVIEW   + " TEXT NOT NULL, "                 +
                        MovieEntry.COLUMN_VOTE_AVG   + " INTEGER NOT NULL, "              +
                        MovieEntry.COLUMN_REL_DATE   + " INTEGER NOT NULL, "              +
                        MovieEntry.COLUMN_MOVIE_ID   + " INTEGER NOT NULL, "              +
                        " UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
