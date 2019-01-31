package com.mydev.android.myweather.data.network;

import com.mydev.android.myweather.data.model.Forecast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetForecast implements Callback<Forecast> {
    private CallbackLoadForecast callback;

    public GetForecast(CallbackLoadForecast callbackLoadForecast) {
        callback = callbackLoadForecast;
    }

    public void getWeatherByCity(String cityName) {
        App.getApi().getData(cityName,
                WeatherService.LAND, WeatherService.API_KEY).enqueue(this);
    }

    public void getWeatherByLocation(String lat, String lon) {
        App.getApi().getForecastByLocation(lat, lon,
                WeatherService.LAND, WeatherService.API_KEY).enqueue(this);
    }

    @Override
    public void onResponse(Call<Forecast> call, Response<Forecast> response) {
        if (response != null)
            callback.onResponse(response.body());
    }

    @Override
    public void onFailure(Call<Forecast> call, Throwable t) {

    }

    public interface CallbackLoadForecast {
        void onResponse(Forecast forecast);
    }
}
