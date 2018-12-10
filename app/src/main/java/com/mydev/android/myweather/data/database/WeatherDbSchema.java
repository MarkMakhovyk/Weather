package com.mydev.android.myweather.data.database;

public class WeatherDbSchema {
    public static final class WeatherTable {
        public static final String NAME = "weather";

        public static final class Cols {
            public static final String CITY_NAME = "city_name";
            public static final String JSON = "json";
        }
    }
}