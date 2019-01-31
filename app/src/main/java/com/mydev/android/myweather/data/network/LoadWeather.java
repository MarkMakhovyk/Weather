package com.mydev.android.myweather.data.network;

import android.content.Context;

import com.mydev.android.myweather.data.ForecastDAO;
import com.mydev.android.myweather.data.model.Forecast;

import java.util.Date;
import java.util.List;

import Utills.Utills;

public class LoadWeather implements GetForecast.CallbackLoadForecast {
    Context context;

    public LoadWeather(Context context) {
        this.context = context;
    }

    public void updateWeatherData() {
        Date dateNow = new Date();
        List<Forecast> list = ForecastDAO.get(context).getForecasts();
        for (Forecast f : list) {
            Date forecast = new Date(f.getList().get(0).getDt() * 1000L);
            if (dateNow.after(forecast)) {
                if (Utills.isOnline(context)) {
                    loadForecast(f);
                } else outdatedForecast(f);
            }
        }
    }


    void loadForecast(Forecast forecast) {
        GetForecast getForecast = new GetForecast(this);
        getForecast.getWeatherByCity(forecast.getCity().getName());
        outdatedForecast(forecast);
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
        ForecastDAO.get(context).addOrUpdate(forecast);
        return false;
    }

    @Override
    public void onResponse(Forecast forecast) {
        ForecastDAO.get(context).updateForecast(forecast);
    }
}
