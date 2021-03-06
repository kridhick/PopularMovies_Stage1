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
import com.example.udacity.karthikeyan.mypopularmovies.Model.Movie;
import com.example.udacity.karthikeyan.mypopularmovies.R;
import com.example.udacity.karthikeyan.mypopularmovies.UI.DetailActivity;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

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


          Picasso.get().load(movie.getmPosterPath())
                .resize(mContext.getResources().getInteger(R.integer.tmdb_poster_w185_width),
                        mContext.getResources().getInteger(R.integer.tmdb_poster_w185_height))
                .placeholder(R.drawable.placeholder)
                  .error(R.drawable.placeholder)
                .into(holder.moviePoster);

          holder.view.setOnClickListener(v->{

              Intent detailIntent = new Intent(mContext, DetailActivity.class);
              detailIntent.putExtra(mContext.getString(R.string.PARCEL_KEY), movie);
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
                    return mValues.get(oldItemPosition).getmOriginalTitle().equals(newValues.get(newItemPosition).getmOriginalTitle());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Movie newMovie = newValues.get(newItemPosition);
                    Movie oldMovie = mValues.get(oldItemPosition);

                    return newMovie.getmOriginalTitle().equals(oldMovie.getmOriginalTitle()) && newMovie.getmReleaseDate().equals(oldMovie.getmReleaseDate());
                }
            });
            mValues = newValues;
            result.dispatchUpdatesTo(this);
        }
    }

    class MovieListAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.moviePoster)
        ImageView moviePoster;

        View view;
        MovieListAdapterViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
