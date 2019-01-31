package com.mydev.android.myweather.data.network;

import android.support.annotation.NonNull;

import com.mydev.android.myweather.data.model.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    String LAND = "ru";
    String API_KEY = "2842e4fe031b0d2c1fa0294b481ff31a";

    @GET("forecast")
    Call<Forecast> getData(@NonNull @Query("q") String query,
                           @Query("lang") String land,
                           @Query("appid") String APIKey);

    @GET("forecast")
    Call<Forecast> getForecastByLocation(@Query("lat") String lat,
                                         @Query("lon") String lon,
                                         @Query("lang") String land,
                                         @Query("appid") String APIKey);
}
