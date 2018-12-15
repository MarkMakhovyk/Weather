package com.mydev.android.myweather.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.mydev.android.myweather.data.database.WeatherBaseHelper;
import com.mydev.android.myweather.data.database.WeatherCursorWrapper;
import com.mydev.android.myweather.data.database.WeatherDbSchema.WeatherTable;
import com.mydev.android.myweather.data.model.Forecast;

import java.util.ArrayList;
import java.util.List;

import static com.mydev.android.myweather.data.database.WeatherDbSchema.WeatherTable.Cols.CITY_NAME;
import static com.mydev.android.myweather.data.database.WeatherDbSchema.WeatherTable.Cols.JSON;

public class CityForecastList {
    private static final String TAG = "Forecast";

    private static CityForecastList cityForecastList;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CityForecastList get(Context context) {
        if (cityForecastList == null) {
            cityForecastList = new CityForecastList(context);
        }

        return cityForecastList;
    }

    public CityForecastList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new WeatherBaseHelper(mContext)
                .getWritableDatabase();
    }


    public void addForecast(String cityName, String json) {
        Log.e(TAG, "addForecast: ");
        ContentValues values = getContentValues(cityName, json);
        mDatabase.insert(WeatherTable.NAME, null, values);
    }

    public String forecastToJson(Forecast forecast) {
        Gson gson = new Gson();
        String json = gson.toJson(forecast);
        return json;
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

    public void updateForecast(String cityName, String json) {
        Log.e(TAG, "updateForecast: ");
        ContentValues values = getContentValues(cityName, json);
        mDatabase.update(WeatherTable.NAME, values,
                "city_name = ?",
                new String[]{cityName});
    }

    public void updateForecast(Forecast forecast) {
        updateForecast(forecast.getCity().getName(), forecastToJson(forecast));
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
