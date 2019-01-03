package com.mydev.android.myweather.data.network;

import android.content.Context;
import android.util.Log;

import com.mydev.android.myweather.data.CityForecastList;
import com.mydev.android.myweather.data.model.weather.Forecast;

import java.util.Date;
import java.util.List;

import Utills.Utills;

public class LoadWeather {
    Context context;
    private static final String TAG = "Load";

    public LoadWeather(Context context) {
        this.context = context;
    }

    public void updateWeatherData() {
        Date dateNow = new Date();
        List<Forecast> list = CityForecastList.get(context).getForecasts();
        for (Forecast f : list) {
            Date forecast = new Date(f.getList().get(0).getDt() * 1000L);
            if (dateNow.after(forecast)) {
                if (Utills.isOnline(context)) {
                    loadForecast(f);
                } else outdatedForecast(f);
            }
        }
    }


    void loadForecast(Forecast f) {
        Log.e(TAG, "loadForecast: ");
        ForecastTack ft = new ForecastTack(context, f.getCity().getName());
        ft.execute();
        outdatedForecast(f);


    }


    private boolean outdatedForecast(Forecast forecast) {
        if (forecast == null) {
            return true;
        }
        Date dateNow = new Date();
        boolean ok = true;
        while (ok) {
            ok = false;
            long date = forecast.getList().get(0).getDt();
            if (dateNow.after(new Date(date * 1000L))) {
                ok = true;
                forecast.getList().remove(0);
            }
        }
        CityForecastList.get(context).addOrUpdate(forecast);
        return false;
    }
}
