package com.example.udacity.karthikeyan.mypopularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{

    private String mOriginalTitle;
    private String mPosterPath;
    private String mOverview;
    private Double mVoteAverage;
    private String mReleaseDate;
    private int    mMovieID;


    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public void setmOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public Double getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(Double mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public int getmMovieID() { return mMovieID; }

    public void setmMovieID(int mMovieID) { this.mMovieID = mMovieID; }

    /**
     * Constructor for a movie object
     * @param source
     */
    public Movie(Parcel source) {
        mOriginalTitle = source.readString();
        mPosterPath = source.readString();
        mOverview = source.readString();
        mVoteAverage = (Double) source.readValue(Double.class.getClassLoader());
        mReleaseDate = source.readString();
        mMovieID = (Integer) source.readValue(Integer.class.getClassLoader());
    }


    public Movie (String mOriginalTitle, String mPosterPath, String mOverview, Double mVoteAverage, String mReleaseDate, int mMovieID)
    {
        this.mOriginalTitle = mOriginalTitle;
        this.mPosterPath = mPosterPath;
        this.mOverview = mOverview;
        this.mVoteAverage = mVoteAverage;
        this.mReleaseDate = mReleaseDate;
        this.mMovieID = mMovieID;
    }


    /* Parcelable */


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeValue(mVoteAverage);
        dest.writeString(mReleaseDate);
        dest.writeValue(mMovieID);
    }


    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
