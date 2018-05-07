package com.example.udacity.karthikeyan.mypopularmovies.UI;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udacity.karthikeyan.mypopularmovies.BuildConfig;
import com.example.udacity.karthikeyan.mypopularmovies.Model.Movie;
import com.example.udacity.karthikeyan.mypopularmovies.Model.MovieContract;
import com.example.udacity.karthikeyan.mypopularmovies.Sync.FetchMoviesFromTMDB;
import com.example.udacity.karthikeyan.mypopularmovies.Adapters.MovieListAdapter;
import com.example.udacity.karthikeyan.mypopularmovies.R;
import com.example.udacity.karthikeyan.mypopularmovies.Utilities.ConnectivityReceiver;

import com.example.udacity.karthikeyan.mypopularmovies.Utilities.GeneralUtils;
import com.example.udacity.karthikeyan.mypopularmovies.Utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.udacity.karthikeyan.mypopularmovies.BuildConfig.MY_MOVIE_DB_API_KEY;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, ConnectivityReceiver.ConnectivityReceiverListener {

    private final String TAG = MainActivity.class.getSimpleName();
    private MovieListAdapter mAdapter;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private FetchMoviesFromTMDB aSyncTask;
    FetchMoviesFromTMDB.onGetMoviesCompleted aSyncTaskCompleted;
    private List<Movie> moviesList = new ArrayList<>();
    public String API_KEY;

    public static final String[] FAV_MOVIE_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_VOTE_AVG,
            MovieContract.MovieEntry.COLUMN_REL_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
    };

    private static final int MOVIE_LOADER_ID = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview);
        mProgressBar = findViewById(R.id.progressbar);

        showLoadingInfo();

        int GRID_SPAN_COUNT = GeneralUtils.calculateNoOfColumns(getApplicationContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        API_KEY = MY_MOVIE_DB_API_KEY;
        mAdapter = new MovieListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        aSyncTaskCompleted = movies -> {
            mAdapter.swapData(movies);
            showMovieListInfo();
        };

        aSyncTask = new FetchMoviesFromTMDB(this, aSyncTaskCompleted);

        getMoviesDataFromTMDB();

        getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);

    }


    private void showLoadingInfo() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showMovieListInfo() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        if (mAdapter.getItemCount() == 0) {
            Toast.makeText(this, R.string.NO_DATA_FOUND, Toast.LENGTH_LONG).show();
        }
    }

    private void showFavoriteList() {

        showLoadingInfo();
        if (moviesList != null && moviesList.size() != 0) {
            mAdapter.swapData(moviesList);
            showMovieListInfo();
        }
        else
        {
            Toast.makeText(this, R.string.NO_FAV_FOUND, Toast.LENGTH_LONG).show();
        }

    }


    private void getMoviesDataFromTMDB() {

        if (NetworkUtils.isNetworkAvailable(this)) {

            if (API_KEY.isEmpty()) {
                Toast.makeText(this, R.string.MISSING_API_KEY, Toast.LENGTH_LONG).show();
            } else {
                aSyncTask.execute(getAPIUrl());
                Log.d(TAG, "Adapter values are set");
            }


        } else {
            Toast.makeText(this, R.string.NO_NETWORK, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No Network been called");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_most_popular:
                updateSharedPreference(getString(R.string.SORT_BY_MOST_POPULAR_KEY));
                new FetchMoviesFromTMDB(this, aSyncTaskCompleted).execute(getAPIUrl());
                return true;
            case R.id.menu_top_rated:
                updateSharedPreference(getString(R.string.SORT_BY_TOP_RATED_KEY));
                new FetchMoviesFromTMDB(this, aSyncTaskCompleted).execute(getAPIUrl());
                return true;
            case R.id.menu_favorite:
                showFavoriteList();
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }


    private String getSortMethod() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        return prefs.getString(getString(R.string.PREF_SORT_METHOD_KEY),
                getString(R.string.SORT_BY_MOST_POPULAR_KEY));
    }

    private URL getAPIUrl() {
        return NetworkUtils.getApiUrl(getBaseContext(), getSortMethod());
    }


    private void updateSharedPreference(String sortMethod) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.PREF_SORT_METHOD_KEY), sortMethod);
        editor.apply();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == MOVIE_LOADER_ID) {
            return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI, FAV_MOVIE_PROJECTION, null, null, null);
        }
        else {
            throw new RuntimeException("Loader Not Implemented: " + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        convertCursortoList(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesList = null;
    }

    private void convertCursortoList(Cursor cursor) {
        int originalTitleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int posterPathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        int overviewIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        int voteAverageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVG);
        int releaseDateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_REL_DATE);
        int movieIDIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);



        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            String Title = cursor.getString(originalTitleIndex);
            String PosterPath = cursor.getString(posterPathIndex);
            String Overview = cursor.getString(overviewIndex);
            Double VoteAvg = cursor.getDouble(voteAverageIndex);
            String ReleaseDate = cursor.getString(releaseDateIndex);
            int    MovieID     = cursor.getInt(movieIDIndex);

            moviesList.add(new Movie(
                    Title, PosterPath, Overview, VoteAvg, ReleaseDate, MovieID
            ));


        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
      showNetworkMessage(isConnected);
    }

    // Showing the status in Snackbar
    private void showNetworkMessage(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = getString(R.string.NETWORK_SHORT_MSG);
            color = Color.WHITE;
        } else {
            message = getString(R.string.NO_NETWORK_SHORT_MSG);
            color = Color.RED;
        }
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(mAdapter.getItemCount() > 0)
        {
            outState.putParcelableArrayList(getString(R.string.PARCEL_KEY), (ArrayList<? extends Parcelable>) moviesList);
        }
        super.onSaveInstanceState(outState);
    }
}
