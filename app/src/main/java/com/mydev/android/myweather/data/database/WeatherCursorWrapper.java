package com.mydev.android.myweather.data.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.mydev.android.myweather.data.database.WeatherDbSchema.WeatherTable;
import com.mydev.android.myweather.data.model.weather.Forecast;

import Utills.Json;

public class WeatherCursorWrapper extends CursorWrapper {

    public WeatherCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Forecast getWeather() {
        String json = getString(getColumnIndex(WeatherTable.Cols.JSON));
        Forecast forecast = Json.jsonToWeather(json);

        return forecast;
    }
}
