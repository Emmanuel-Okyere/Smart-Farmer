package com.example.smartpro.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://api.thingspeak.com/";
    @GET("channels/1407757/feeds.json")
    Call<Results> getAllData();

    @GET("/channels/1407757/feeds.json")
    Call<Results> getResultData(@Query("results")String results);

}
