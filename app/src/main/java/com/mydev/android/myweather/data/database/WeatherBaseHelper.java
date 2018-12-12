package com.mydev.android.myweather.data.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.mydev.android.myweather.data.database.WeatherDbSchema.WeatherTable.Cols;
import static com.mydev.android.myweather.data.database.WeatherDbSchema.WeatherTable.NAME;

public class WeatherBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "weatherBase.db";

    public WeatherBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NAME + "(" +
                " _id integer primary key autoincrement, " +
                Cols.CITY_NAME + ", " +
                Cols.JSON + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}