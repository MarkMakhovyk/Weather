package com.mydev.android.myweather.data.network;

import android.content.Context;
import android.os.AsyncTask;

import com.mydev.android.myweather.data.CityForecastList;
import com.mydev.android.myweather.data.model.Forecast;

public class ForecastTack extends AsyncTask<Void, Void, Void> {

    String city;
    Forecast forecast;
    Context context;

    public ForecastTack(Context context, String city) {
        this.context = context;
        this.city = city;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        OpenWeatherMap op = new OpenWeatherMap();
        forecast = op.getWeather(city);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (forecast != null) {
            CityForecastList cityForecastList = new CityForecastList(context);
            if (cityForecastList.getForecast(city) == null) {
                cityForecastList.addForecast(forecast.getCity().getName(), cityForecastList.forecastToJson(forecast));
            } else
                cityForecastList.updateForecast(forecast);
        }
    }

}
