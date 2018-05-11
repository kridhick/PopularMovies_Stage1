package com.example.udacity.karthikeyan.mypopularmovies.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.udacity.karthikeyan.mypopularmovies.Adapters.ReviewAdapter;
import com.example.udacity.karthikeyan.mypopularmovies.BuildConfig;
import com.example.udacity.karthikeyan.mypopularmovies.Model.Movie;
import com.example.udacity.karthikeyan.mypopularmovies.Model.ResultReview;
import com.example.udacity.karthikeyan.mypopularmovies.Model.Review;
import com.example.udacity.karthikeyan.mypopularmovies.R;
import com.example.udacity.karthikeyan.mypopularmovies.Sync.RESTApiClient;
import com.example.udacity.karthikeyan.mypopularmovies.Sync.RESTApiInterface;
import com.example.udacity.karthikeyan.mypopularmovies.Utilities.PaginationScrollListener;

import java.util.List;
import java.util.Objects;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindInt(R.integer.MAX_PAGE_CHECK_SIZE)
    int TOTAL_PAGES;

    ReviewAdapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    RESTApiInterface restApiInterface;

    private static String API_KEY;
    private int mMovieId;
    private Movie mMovie;
    private static final String TAG = ReviewActivity.class.getSimpleName();
    private static final int MOVIE_DEFAULT_VALUE = -1;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        API_KEY = BuildConfig.MY_MOVIE_DB_API_KEY;

        initialize();

        restApiInterface = RESTApiClient.getRetrofit().create(RESTApiInterface.class);

        loadFirstPage();

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                loadNextPage();

            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


    }

    private void initialize()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // Handled the device back button pressed stage.
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);


        Intent intent = getIntent();
        mMovieId = intent.getIntExtra(getString(R.string.TAG_MOVIE_ID), MOVIE_DEFAULT_VALUE);
        mMovie = intent.getParcelableExtra(getString(R.string.PARCEL_KEY));

        reviewAdapter = new ReviewAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(reviewAdapter);


    }

    private void loadFirstPage()
    {
        if (mMovieId != 0 && mMovieId != MOVIE_DEFAULT_VALUE) {

            Call<Review> reviewCall = restApiInterface.getMovieReviews(mMovieId, API_KEY, currentPage);
            reviewCall.enqueue(new Callback<Review>() {
                @Override
                public void onResponse(@NonNull Call<Review> call, @NonNull Response<Review> response) {

                    if(response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        List<ResultReview> resultReviewsList = Objects.requireNonNull(response.body()).getResults();

                        if (resultReviewsList.size() == 0) {
                            Toast.makeText(getApplicationContext(), R.string.NO_DATA_FOUND, Toast.LENGTH_LONG).show();

                        }
                        else {
                            Log.d(TAG, "The API had returned:" + resultReviewsList.size());
                            reviewAdapter.addAll(resultReviewsList);

                            Review review = response.body();
                            TOTAL_PAGES = Objects.requireNonNull(review).getTotalPages();

                            if (currentPage < TOTAL_PAGES) reviewAdapter.addLoadingFooter();
                            else isLastPage = true;
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Review> call, @NonNull Throwable t) {
                    Log.e(TAG, t.toString());
                    Toast.makeText(getApplicationContext(), R.string.NO_NETWORK, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void loadNextPage()
    {
        if (mMovieId != 0 && mMovieId != MOVIE_DEFAULT_VALUE) {

            Call<Review> reviewCall = restApiInterface.getMovieReviews(mMovieId, API_KEY, currentPage);
            reviewCall.enqueue(new Callback<Review>() {
                @Override
                public void onResponse(@NonNull Call<Review> call, @NonNull Response<Review> response) {

                    reviewAdapter.removeLoadingFooter();
                    isLoading = false;

                    if(response.isSuccessful()) {

                        progressBar.setVisibility(View.GONE);
                        List<ResultReview> resultReviewsList = Objects.requireNonNull(response.body()).getResults();
                        Log.d(TAG, "The API had returned:" + resultReviewsList.size());
                        reviewAdapter.addAll(resultReviewsList);

                        if (resultReviewsList.size() != 0 &&  currentPage != TOTAL_PAGES) reviewAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Review> call, @NonNull Throwable t) {
                    Log.e(TAG, t.toString());
                    Toast.makeText(getApplicationContext(), R.string.NO_NETWORK, Toast.LENGTH_LONG).show();
                }
            });
        }

    }



    @Override
    public void onBackPressed() {
        Intent detailActivity = new Intent();
        detailActivity.putExtra(getString(R.string.PARCEL_KEY), mMovie);
        setResult(RESULT_OK, detailActivity);
        finish();
    }
}
