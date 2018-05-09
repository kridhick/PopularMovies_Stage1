package com.example.udacity.karthikeyan.mypopularmovies.Sync;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RESTApiClient {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit() {
        if (null == retrofit) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
