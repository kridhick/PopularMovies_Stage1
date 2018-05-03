package com.example.udacity.karthikeyan.mypopularmovies.UI;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udacity.karthikeyan.mypopularmovies.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView TitleTextView =  findViewById(R.id.textview_original_title);
        ImageView PosterImageView =  findViewById(R.id.imageview_poster);
        TextView OverviewTextView = findViewById(R.id.textview_overview);
        TextView VoteAverageTextView =  findViewById(R.id.textview_vote_average);
        TextView ReleaseDateTextView =  findViewById(R.id.textview_release_date);

        // TODO: Need to explore the possibility of sending the whole class/object as param.
        Intent intent = getIntent();
        String mTitle = intent.getStringExtra(getString(R.string.TAG_ORIGINAL_TITLE));
        String mPosterURL = intent.getStringExtra(getString(R.string.TAG_POSTER_PATH));
        String mOverview = intent.getStringExtra(getString(R.string.TAG_OVERVIEW));
        String mVoteAverage = intent.getStringExtra(getString(R.string.TAG_VOTE_AVERAGE));
        String mReleaseDate = intent.getStringExtra(getString(R.string.TAG_RELESE_DATE));

        TitleTextView.setText(mTitle);

        Picasso.get()
                .load(mPosterURL)
                .resize(getResources().getInteger(R.integer.tmdb_poster_w185_width),
                        getResources().getInteger(R.integer.tmdb_poster_w185_height))
                .placeholder(R.drawable.placeholder)
                .into(PosterImageView);


        if (mOverview == null) {
            OverviewTextView.setTypeface(null, Typeface.ITALIC);
            mOverview = getResources().getString(R.string.NO_DATA_FOUND);
        }
        OverviewTextView.setText(mOverview);
        VoteAverageTextView.setText(mVoteAverage + "/10");



        if(mReleaseDate == null)
        {
            ReleaseDateTextView.setTypeface(null, Typeface.ITALIC);
            mReleaseDate = getResources().getString(R.string.NO_DATA_FOUND);
        }
        ReleaseDateTextView.setText(mReleaseDate);
        Log.d(TAG, "Detail activity successfully completed for"+ mTitle);

    }

}
