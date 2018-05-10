package com.example.udacity.karthikeyan.mypopularmovies.UI;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udacity.karthikeyan.mypopularmovies.Model.Movie;
import com.example.udacity.karthikeyan.mypopularmovies.Model.MovieContract;
import com.example.udacity.karthikeyan.mypopularmovies.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final int MOVIE_DEFAULT_VALUE = -1;
    private static final int MOVIE_DETAIL_LOADER_ID = 101;
    private static final int REQ_CODE = 1;
    private ContentValues mContentValues;
    private int mMovieID;
    private CheckBox favouriteCheckBox;
    private Uri mUri;
    private Movie mMovie;
    private ContentResolver mContentResolver;
    private TextView TitleTextView;
    private ImageView PosterImageView;
    private TextView OverviewTextView;
    private TextView VoteAverageTextView;
    private TextView ReleaseDateTextView;
    private ImageButton reviewButton;
    public static final String[] FAV_MOVIE_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TitleTextView =  findViewById(R.id.textview_original_title);
        PosterImageView =  findViewById(R.id.imageview_poster);
        OverviewTextView = findViewById(R.id.textview_overview);
        VoteAverageTextView =  findViewById(R.id.textview_vote_average);
        ReleaseDateTextView =  findViewById(R.id.textview_release_date);
        favouriteCheckBox = findViewById(R.id.btn_favorite);
        reviewButton = findViewById(R.id.btn_review);


        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.PARCEL_KEY))) {
            mMovie = intent.getParcelableExtra(getString(R.string.PARCEL_KEY));
            showDetailData();

        }

        getLoaderManager().initLoader(MOVIE_DETAIL_LOADER_ID, null, this);

            favouriteCheckBox.setOnClickListener(v -> {

                // Checked
                if (favouriteCheckBox.isChecked() && mMovieID != MOVIE_DEFAULT_VALUE) {
                    mContentResolver.delete(mUri, null, null);

                    mContentResolver.insert(MovieContract.MovieEntry.CONTENT_URI, mContentValues);

                    Toast.makeText(this, R.string.FAV_ADD_MSG, Toast.LENGTH_LONG).show();
                }


                // Not Checked
                if (!favouriteCheckBox.isChecked() && mMovieID != MOVIE_DEFAULT_VALUE) {
                    mContentResolver.delete(mUri, null, null);
                    Toast.makeText(this, R.string.FAV_REMOVE_MSG, Toast.LENGTH_LONG).show();
                }


            });




        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewPage();
            }
        });



    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == MOVIE_DETAIL_LOADER_ID) {
            return new CursorLoader(this, mUri, FAV_MOVIE_PROJECTION, null, null, null);
        }
        else {
            throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int movieIDIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            int dbPersistedMovieID = data.getInt(movieIDIndex);
            if(mMovieID == dbPersistedMovieID)
            {
                favouriteCheckBox.setChecked(true);
            }
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void showReviewPage()
    {
        Intent reviewIntent = new Intent(this, ReviewActivity.class);
        reviewIntent.putExtra(getString(R.string.TAG_MOVIE_ID), mMovieID);
        reviewIntent.putExtra(getString(R.string.PARCEL_KEY), mMovie);
        startActivityForResult(reviewIntent, REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                mMovie = data.getParcelableExtra(getString(R.string.PARCEL_KEY));
                showDetailData();

            }
        }
    }

    private void showDetailData()
    {

        String mTitle = mMovie.getmOriginalTitle();
        String mPosterURL = mMovie.getmPosterPath();
        String mOverview = mMovie.getmOverview();
        String mVoteAverage = String.valueOf(mMovie.getmVoteAverage());
        String mReleaseDate = mMovie.getmReleaseDate();
        mMovieID = mMovie.getmMovieID();

        mUri = MovieContract.MovieEntry.buildUriWithMovieID(mMovieID);

        TitleTextView.setText(mTitle);

        Picasso.get()
                .load(mPosterURL)
                .resize(getResources().getInteger(R.integer.tmdb_poster_w185_width),
                        getResources().getInteger(R.integer.tmdb_poster_w185_height))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(PosterImageView);


        if (mOverview == null) {
            OverviewTextView.setTypeface(null, Typeface.ITALIC);
            mOverview = getResources().getString(R.string.NO_DATA_FOUND);
        }
        OverviewTextView.setText(mOverview);
        VoteAverageTextView.setText(mVoteAverage + "/10");


        if (mReleaseDate == null) {
            ReleaseDateTextView.setTypeface(null, Typeface.ITALIC);
            mReleaseDate = getResources().getString(R.string.NO_DATA_FOUND);
        }
        ReleaseDateTextView.setText(mReleaseDate);
        Log.d(TAG, "Detail activity successfully completed for" + mTitle);

        mContentResolver = this.getContentResolver();
        mContentValues = new ContentValues();
        mContentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, mTitle);
        mContentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, mPosterURL);
        mContentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mOverview);
        mContentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, Double.parseDouble(mVoteAverage));
        mContentValues.put(MovieContract.MovieEntry.COLUMN_REL_DATE, mReleaseDate);
        mContentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovieID);

    }
}
