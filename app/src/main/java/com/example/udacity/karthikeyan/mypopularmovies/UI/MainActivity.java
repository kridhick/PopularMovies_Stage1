package com.example.udacity.karthikeyan.mypopularmovies.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.udacity.karthikeyan.mypopularmovies.Sync.FetchMoviesFromTMDB;
import com.example.udacity.karthikeyan.mypopularmovies.Adapters.MovieListAdapter;
import com.example.udacity.karthikeyan.mypopularmovies.R;
import com.example.udacity.karthikeyan.mypopularmovies.Utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private MovieListAdapter mAdapter;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private FetchMoviesFromTMDB aSyncTask;
    FetchMoviesFromTMDB.onGetMoviesCompleted aSyncTaskCompleted;
    private Menu mMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview);
        mProgressBar = findViewById(R.id.progressbar);

        showLoadingInfo();

        int GRID_SPAN_COUNT = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new MovieListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        aSyncTaskCompleted = movies -> {
            mAdapter.swapData(movies);
            showMovieListInfo();
        };

        aSyncTask = new FetchMoviesFromTMDB(this, aSyncTaskCompleted);


        getMoviesData();




    }

    private void showLoadingInfo()
    {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showMovieListInfo()
    {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        if (mAdapter.getItemCount() == 0){
            Toast.makeText(this, R.string.NO_DATA_FOUND, Toast.LENGTH_LONG).show();
        }
    }

    private  void getMoviesData()
    {

        if(NetworkUtils.isNetworkAvailable(this))
        {
            String API_KEY = getString(R.string.API_KEY);
            if (API_KEY.isEmpty()) {
                Toast.makeText(this, R.string.MISSING_API_KEY, Toast.LENGTH_LONG).show();
            } else {
                aSyncTask.execute(getAPIUrl());
                Log.d(TAG, "Adapter values are set");
            }


        }
        else
        {
            Toast.makeText(this, R.string.NO_NETWORK, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No Network been called");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, mMenu);

        // Make menu items accessible
        mMenu = menu;

        // Add menu items dynamically
        mMenu.add(Menu.NONE, R.string.PREF_SORT_BY_MOST_POPULAR, Menu.NONE, R.string.PREF_SORT_BY_MOST_POPULAR)
                .setVisible(false)
                .setIcon(R.drawable.ic_action_whatshot)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        // Add menu items dynamically
        mMenu.add(Menu.NONE, R.string.PREF_SORT_BY_TOP_RATED, Menu.NONE, R.string.PREF_SORT_BY_TOP_RATED)
                .setVisible(false)
                .setIcon(R.drawable.ic_action_poll)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        // Update menu to show relevant items
        updateMenu();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.string.PREF_SORT_BY_MOST_POPULAR:
                updateSharedPreference(getString(R.string.SORT_BY_MOST_POPULAR_KEY));
                updateMenu();
                new FetchMoviesFromTMDB(this, aSyncTaskCompleted).execute(getAPIUrl());
                return true;
            case R.string.PREF_SORT_BY_TOP_RATED:
                updateSharedPreference(getString(R.string.SORT_BY_TOP_RATED_KEY));
                updateMenu();
                new FetchMoviesFromTMDB(this, aSyncTaskCompleted).execute(getAPIUrl());
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateMenu() {
        String sortMethod = getSortMethod();

        if (sortMethod.equals(getString(R.string.SORT_BY_MOST_POPULAR_KEY))) {
            mMenu.findItem(R.string.PREF_SORT_BY_MOST_POPULAR).setVisible(false);
            mMenu.findItem(R.string.PREF_SORT_BY_TOP_RATED).setVisible(true);
        } else {
            mMenu.findItem(R.string.PREF_SORT_BY_TOP_RATED).setVisible(false);
            mMenu.findItem(R.string.PREF_SORT_BY_MOST_POPULAR).setVisible(true);
        }
    }


    private String getSortMethod() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        return prefs.getString(getString(R.string.PREF_SORT_METHOD_KEY),
                getString(R.string.SORT_BY_MOST_POPULAR_KEY));
    }

    private URL getAPIUrl()
    {
        return NetworkUtils.getApiUrl(getBaseContext(), getSortMethod());
    }


    private void updateSharedPreference(String sortMethod) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.PREF_SORT_METHOD_KEY), sortMethod);
        editor.apply();
    }

}
