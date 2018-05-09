package com.example.udacity.karthikeyan.mypopularmovies.Adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.udacity.karthikeyan.mypopularmovies.Model.ResultReview;
import com.example.udacity.karthikeyan.mypopularmovies.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    private List<ResultReview> mReviewResults = new ArrayList<>();
    private final Context mContext;

    public ReviewAdapter(Context context) {
        this.mContext = context;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType)
        {
            case ITEM:
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.content_review, parent, false);
                itemView.setFocusable(true);
                viewHolder = new ReviewItemViewHolder(itemView);
                break;
            case LOADING:
                View progressView = LayoutInflater.from(mContext).inflate(R.layout.content_review_progress, parent, false);
                progressView.setFocusable(true);
                viewHolder = new ReviewProgressViewHolder(progressView);
               break;

        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final ResultReview resultReview = mReviewResults.get(position);

       switch (getItemViewType(position))
       {
           case ITEM:
               final ReviewItemViewHolder reviewItemViewHolder = (ReviewItemViewHolder) holder;
               reviewItemViewHolder.reviewAuthor.setText(resultReview.getAuthor());
               reviewItemViewHolder.reviewComments.setText(resultReview.getContent() + resultReview.getUrl());
               break;
           case LOADING:
               final ReviewProgressViewHolder reviewProgressViewHolder = (ReviewProgressViewHolder) holder;
               if (position == 0)
               {
                   reviewProgressViewHolder.loadNextPageProgressBar.setVisibility(View.GONE);
               }
               break;
       }

    }

    @Override
    public int getItemCount() {
        return (null == mReviewResults)? 0 : mReviewResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mReviewResults.size()-1 && isLoadingAdded)? LOADING : ITEM;
    }

    //start region for View holders
    protected class ReviewItemViewHolder extends RecyclerView.ViewHolder {

        TextView reviewAuthor;
        TextView reviewComments;
        View view;

        public ReviewItemViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = itemView.findViewById(R.id.textview_author);
            reviewComments = itemView.findViewById(R.id.textview_comments);
            view = itemView;

        }
    }

    protected class ReviewProgressViewHolder extends RecyclerView.ViewHolder {

        ProgressBar loadNextPageProgressBar;

        public ReviewProgressViewHolder(View itemView) {
            super(itemView);

            loadNextPageProgressBar = itemView.findViewById(R.id.loadmore_progress);
        }
    }
    //end region for view holders


    //start region for Helper methods

    private void add(ResultReview reviewResult) {
        mReviewResults.add(reviewResult);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addAll(List<ResultReview> reviewResultList) {
            for (ResultReview value :  reviewResultList){
                add(value);
            }

        }

    public void remove(ResultReview resultReview) {
        int position = mReviewResults.indexOf(resultReview);
        if (position > -1) {
            mReviewResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ResultReview());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mReviewResults.size() - 1;
        ResultReview result = getItem(position);

        if (result != null) {
            mReviewResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ResultReview getItem(int position) {
        return mReviewResults.get(position);
    }


    //end region for Helper methods

}



