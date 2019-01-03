package com.mydev.android.myweather.data.network;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

public class CityPreference {
    private static final String CITY_SHOWING = "city_visible";
    private static final String LAST_LOCATION = "location";
    private static final String TAG = "City";

    public static String getCityVisible(Context context) {
        Log.e(TAG, "getCityVisible: " + PreferenceManager.getDefaultSharedPreferences(context)
                .getString(CITY_SHOWING, null));
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(CITY_SHOWING, null);
    }

    public static void setCityVisible(Context context, String city) {
        Log.e(TAG, "setCityVisible: " + city);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(CITY_SHOWING, city)
                .apply();
    }

    public static String getLastLocation(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(LAST_LOCATION, null);
    }

    public static void setLastLocation(Context context, String city) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(LAST_LOCATION, city)
                .apply();
    }
}
