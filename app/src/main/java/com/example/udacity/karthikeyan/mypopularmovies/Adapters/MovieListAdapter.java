package com.example.udacity.karthikeyan.mypopularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udacity.karthikeyan.mypopularmovies.Model.Movie;
import com.example.udacity.karthikeyan.mypopularmovies.R;
import com.example.udacity.karthikeyan.mypopularmovies.UI.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder>{

    private final Context mContext;
    private List<Movie> mValues;

    public MovieListAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.content_movies_list, parent, false);
        view.setFocusable(true);
        return new MovieListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapterViewHolder holder, int position) {
        final Movie movie = mValues.get(position);
        //holder.movieTitle.setText(movie.getmOriginalTitle());

          Picasso.get().load(movie.getmPosterPath())
                .resize(mContext.getResources().getInteger(R.integer.tmdb_poster_w185_width),
                        mContext.getResources().getInteger(R.integer.tmdb_poster_w185_height))
                .placeholder(R.drawable.placeholder)
                .into(holder.moviePoster);

          holder.view.setOnClickListener(v->{

              Intent detailIntent = new Intent(mContext, DetailActivity.class);
              detailIntent.putExtra(mContext.getString(R.string.TAG_ORIGINAL_TITLE), movie.getmOriginalTitle());
              detailIntent.putExtra(mContext.getString(R.string.TAG_POSTER_PATH), movie.getmPosterPath());
              detailIntent.putExtra(mContext.getString(R.string.TAG_VOTE_AVERAGE), movie.getmVoteAverage().toString());
              detailIntent.putExtra(mContext.getString(R.string.TAG_OVERVIEW), movie.getmOverview());
              detailIntent.putExtra(mContext.getString(R.string.TAG_RELESE_DATE), movie.getmReleaseDate());
              mContext.startActivity(detailIntent);

          });


    }

    @Override
    public int getItemCount() {
        if(null == mValues) {
            return 0;
        }
         return mValues.size();
    }

    public void swapData(final List<Movie> newValues)
    {
        if (null == mValues) {
            this.mValues = newValues;
            notifyDataSetChanged();
        }
        else
        {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mValues.size();
                }

                @Override
                public int getNewListSize() {
                    return newValues.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mValues.get(oldItemPosition).getmOriginalTitle() == newValues.get(newItemPosition).getmOriginalTitle();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Movie newMovie = newValues.get(newItemPosition);
                    Movie oldMovie = mValues.get(oldItemPosition);

                    return newMovie.getmOriginalTitle() == oldMovie.getmOriginalTitle() && newMovie.getmReleaseDate() == (oldMovie.getmReleaseDate());
                }
            });
            mValues = newValues;
            result.dispatchUpdatesTo(this);
        }
    }

    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView moviePoster;
        TextView movieTitle;
        View view;
        public MovieListAdapterViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.moviePoster = itemView.findViewById(R.id.moviePoster);
            //this.movieTitle = itemView.findViewById(R.id.movieName);
        }
    }
}
