package com.example.udacity.karthikeyan.mypopularmovies.UI;

import android.annotation.SuppressLint;
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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udacity.karthikeyan.mypopularmovies.BuildConfig;
import com.example.udacity.karthikeyan.mypopularmovies.Model.Movie;
import com.example.udacity.karthikeyan.mypopularmovies.Model.MovieContract;
import com.example.udacity.karthikeyan.mypopularmovies.Model.ResultVideo;
import com.example.udacity.karthikeyan.mypopularmovies.Model.Video;
import com.example.udacity.karthikeyan.mypopularmovies.Sync.RESTApiClient;
import com.example.udacity.karthikeyan.mypopularmovies.Sync.RESTApiInterface;
import com.example.udacity.karthikeyan.mypopularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final int MOVIE_DEFAULT_VALUE = -1;
    private static final int MOVIE_DETAIL_LOADER_ID = 101;
    private static final int REQ_CODE = 1;

    private ContentValues mContentValues;
    private int mMovieID;
    private Uri mUri;
    private Movie mMovie;
    private ContentResolver mContentResolver;
    private RESTApiInterface restApiInterface;
    private String mVideoKey;


    String API_KEY;

    @BindString(R.string.YouTube)
    String cYoutube;

    @BindString(R.string.Trailer)
    String cTrailer;

    @BindView(R.id.textview_original_title)
    TextView TitleTextView;

    @BindView(R.id.imageview_poster)
    ImageView PosterImageView;

    @BindView(R.id.textview_overview)
    TextView OverviewTextView;

    @BindView(R.id.textview_vote_average)
    TextView VoteAverageTextView;

    @BindView(R.id.textview_release_date)
    TextView ReleaseDateTextView;

    @BindView(R.id.btn_review)
    ImageButton reviewButton;

    @BindView(R.id.btn_favorite)
    CheckBox favouriteCheckBox;

    @BindView(R.id.btn_trailer)
    ImageButton trailerButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.overview_layout)
    LinearLayout overviewLayout;

    public static final String[] FAV_MOVIE_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        API_KEY = BuildConfig.MY_MOVIE_DB_API_KEY;

        restApiInterface = RESTApiClient.getRetrofit().create(RESTApiInterface.class);


        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.PARCEL_KEY))) {
            mMovie = intent.getParcelableExtra(getString(R.string.PARCEL_KEY));
            showDetailData();
            loadVideos();

        }

        if(overviewLayout.getVisibility() == View.GONE)
        {
            overviewLayout.setVisibility(View.VISIBLE);
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




            reviewButton.setOnClickListener(v -> showReviewPage());

            trailerButton.setOnClickListener(v -> showTrailer());





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

    @SuppressLint("SetTextI18n")
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


    private void loadVideos()
    {
        if (mMovieID !=0 && mMovieID != MOVIE_DEFAULT_VALUE)
        {
            Call<Video> videoCall = restApiInterface.getMovieVideos(mMovieID, API_KEY);
            videoCall.enqueue(new Callback<Video>() {
                @Override
                public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {

                    if(response.isSuccessful())
                    {
                        assert response.body() != null;
                        List<ResultVideo> resultVideosList = response.body().getResults();
                        if(resultVideosList.size() == 0)
                        {
                            Toast.makeText(getApplicationContext(), R.string.NO_DATA_FOUND, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            for(ResultVideo value: resultVideosList)
                            {
                                // To ensure to hold the one key even though there are multiple official trailers for a movie
                                // To ensure we are considering the Trailer type only not the clips or other promotional videos for Youtube
                                if(value.getSite().equalsIgnoreCase(cYoutube) && value.getType().equalsIgnoreCase(cTrailer)){
                                  mVideoKey = value.getKey();
                                }
                            }
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<Video> call, @NonNull Throwable t) {
                    Log.e(TAG, t.toString());
                    Toast.makeText(getApplicationContext(), R.string.NO_NETWORK, Toast.LENGTH_LONG).show();

                }
            });
        }

    }

    private void showTrailer()
    {
        if (mVideoKey != null) {
            overviewLayout.setVisibility(View.GONE);
            Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+mVideoKey));
            startActivity(videoIntent);
        }

    }

}
