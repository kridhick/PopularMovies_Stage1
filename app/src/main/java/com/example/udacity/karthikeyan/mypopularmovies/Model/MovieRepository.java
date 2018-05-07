package com.example.udacity.karthikeyan.mypopularmovies.Model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class MovieRepository extends ContentProvider{

    private static final String TAG = MovieRepository.class.getSimpleName();
    private MovieDBHelper mMovieDBHelper;

    static final int MOVIES= 1;
    static final int MOVIE_WITH_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);
    }


    @Override
    public boolean onCreate() {
        mMovieDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDBHelper.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri))
        {
            case MOVIES:
                cursor = db.query(MovieContract.MovieEntry.TABLE_NAME, projection, selection,selectionArgs, null, null, sortOrder);
                break;
            case MOVIE_WITH_ID:
                String[] selectionArgument = new String[]{uri.getLastPathSegment()};
                cursor = db.query(MovieContract.MovieEntry.TABLE_NAME, projection, MovieContract.MovieEntry.COLUMN_MOVIE_ID +" = ? ", selectionArgument, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }



        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();

        switch(uriMatcher.match(uri))
        {
            case MOVIES:
                db.beginTransaction();
                long rowsInserted = 0;
                try {
                    rowsInserted = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }


                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return uri;
             default:
                 throw new UnsupportedOperationException("Unknown uri: " + uri);

        }


    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted = 0;
        String whereClause = "";
        String[] whereArgs = {};

        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case MOVIE_WITH_ID:

                String id = uri.getPathSegments().get(1);

                if (!TextUtils.isEmpty(selection))
                {
                    whereClause = "AND ( " + selection + ")";
                    whereArgs = selectionArgs;
                }

                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID +  " = " + id + whereClause,
                        whereArgs);


                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }




    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("We are not implementing this method in our popular movies app.");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing this method in our popular movies app.");
    }
}





