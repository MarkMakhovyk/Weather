package com.mydev.android.myweather.data.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mydev.android.myweather.data.CityForecastList;
import com.mydev.android.myweather.data.model.weather.Forecast;

public class ForecastTack extends AsyncTask<Void, Void, Forecast> {
    private static final String TAG = "Forecast";

    String city = null;
    Forecast forecast;
    Context context;
    String lon = "";
    String lat = "";
    int count;

    public ForecastTack(Context context, String city) {
        this.context = context;
        this.city = city;
    }

    public ForecastTack(Context context, String lon, String lat) {
        this.context = context;
        this.lon = lon;
        this.lat = lat;
    }

    @Override
    protected Forecast doInBackground(Void... voids) {

        OpenWeatherMap op = new OpenWeatherMap();
        if (lon != "" || lat != "") {
            forecast = op.getWeatherForLocation(lon, lat);
        } else {
            Log.e(TAG, "doInBackground: ");
            forecast = op.getWeather(city);
        }
        return forecast;
    }

    @Override
    protected void onPostExecute(Forecast result) {
        super.onPostExecute(result);
        if (forecast != null) {
            CityForecastList cityForecastList = CityForecastList.get(context);
            cityForecastList.addOrUpdate(forecast);
        }
    }
}
