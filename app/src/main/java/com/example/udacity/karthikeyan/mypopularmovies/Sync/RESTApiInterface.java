package com.example.udacity.karthikeyan.mypopularmovies.Sync;

import com.example.udacity.karthikeyan.mypopularmovies.Model.Review;
import com.example.udacity.karthikeyan.mypopularmovies.Model.Video;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RESTApiInterface {

    @GET("movie/{id}/reviews")
    Call<Review> getMovieReviews(@Path("id") int id,
                                 @Query("api_key") String apiKey,
                                 @Query("page") int pageNo);

    @GET("movie/{id}/videos")
    Call<Video> getMovieVideos(@Path("id") int id,
                               @Query("api_key") String apiKey);

}
