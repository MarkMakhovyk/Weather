package com.mydev.android.myweather.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.mydev.android.myweather.data.database.WeatherBaseHelper;
import com.mydev.android.myweather.data.database.WeatherCursorWrapper;
import com.mydev.android.myweather.data.database.WeatherDbSchema.WeatherTable;
import com.mydev.android.myweather.data.model.weather.Forecast;

import java.util.ArrayList;
import java.util.List;

import static com.mydev.android.myweather.data.database.WeatherDbSchema.WeatherTable.Cols.CITY_NAME;
import static com.mydev.android.myweather.data.database.WeatherDbSchema.WeatherTable.Cols.JSON;

public class CityForecastList {
    private static CityForecastList cityForecastList;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CityForecastList get(Context context) {
        if (cityForecastList == null) {
            cityForecastList = new CityForecastList(context);
        }
        return cityForecastList;
    }

    private CityForecastList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new WeatherBaseHelper(mContext).getWritableDatabase();
    }


    private void addForecast(String cityName, String json) {
        ContentValues values = getContentValues(cityName, json);
        mDatabase.insert(WeatherTable.NAME, null, values);
    }

    private void updateForecast(String cityName, String json) {

        ContentValues values = getContentValues(cityName, json);
        mDatabase.update(WeatherTable.NAME, values,
                "city_name = ?",
                new String[]{cityName});
    }

    public List<Forecast> getForecasts() {
        List<Forecast> forecasts = new ArrayList<>();
        WeatherCursorWrapper cursor = queryForecast(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                forecasts.add(cursor.getWeather());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return forecasts;
    }

    public Forecast getForecast(String cityName) {

        WeatherCursorWrapper cursor = queryForecast(
                WeatherTable.Cols.CITY_NAME + " = ?",
                new String[]{cityName}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getWeather();
        } finally {
            cursor.close();
        }
    }


    public void addOrUpdate(Forecast forecast) {

        boolean isUpdate = false;

        if (forecast == null) {
            return;
        }
        List<Forecast> list = getForecasts();
        for (int i = 0; i < list.size(); i++) {
            if (forecast.getCity().getName().equals(list.get(i).getCity().getName())) {
                updateForecast(forecast.getCity().getName(), forecastToJson(forecast));
                isUpdate = true;
            }
        }
        if (!isUpdate) {
            addForecast(forecast.getCity().getName(),
                    forecastToJson(forecast));
        }
    }

    public void deleteForecast(String cityName) {
        mDatabase.delete(WeatherTable.NAME, CITY_NAME + " = ?", new String[]{cityName});
    }


    public String forecastToJson(Forecast forecast) {
        Gson gson = new Gson();
        String json = gson.toJson(forecast);
        return json;
    }

    private WeatherCursorWrapper queryForecast(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                WeatherTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new WeatherCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(String cityName, String json) {
        ContentValues values = new ContentValues();
        values.put(CITY_NAME, cityName);
        values.put(JSON, json);
        return values;
    }

}
