package com.mydev.android.myweather.ui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.CityForecastList;
import com.mydev.android.myweather.data.model.weather.Forecast;
import com.mydev.android.myweather.data.network.CityPreference;
import com.mydev.android.myweather.data.network.LoadWeather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Utills.GetInfoWeather;

public class DataWeatherActivity extends AppCompatActivity {

    private static final String TAG = "weather_activity";
    private ViewPager cityForecastPagers;
    private List<String> cityNameList = new ArrayList<>();
    private int lastBackgroundColor = 0;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagers_data_weather);
        cityForecastPagers = (ViewPager) findViewById(R.id.weather_view_pager);
        this.context = this;

        LoadWeather lw = new LoadWeather(context);
        lw.updateWeatherData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    void updateData() {
        getListCity();

        if (cityNameList.size() == 0) {
            startActivity(new Intent(this, CityListActivity.class));
        }

        setDataCityPagers();
    }

    private void getListCity() {
        CityForecastList cfl = CityForecastList.get(this);
        List<Forecast> forecasts = cfl.getForecasts();
        cityNameList.clear();

        for (Forecast f : forecasts) {
            cityNameList.add(f.getCity().getName());
        }

        Collections.sort(cityNameList);
    }

    void setDataCityPagers() {
        String city = CityPreference.getCityVisible(context);
        FragmentManager fragmentManager = getSupportFragmentManager();

        cityForecastPagers.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                String city = cityNameList.get(position);
                return DataWeatherFragment.newInstance(city);
            }

            @Override
            public int getCount() {
                return cityNameList.size();
            }
        });

        //отображение страници которая была отображена ранее
        for (int i = 0; i < cityNameList.size(); i++) {
            if (cityNameList.get(i).equals(city)) {
                cityForecastPagers.setCurrentItem(i);
                setColor(i);
                break;
            }
        }

        cityForecastPagers.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                CityPreference.setCityVisible(context, cityNameList.get(i));
                setColor(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }


    void setColor(int indexCity) {
        int newColor = GetInfoWeather.getBackgroundColor(indexCity, this);

        if (lastBackgroundColor == 0) {
            cityForecastPagers.setBackgroundColor(newColor);
        } else {
            ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                    .ofInt(cityForecastPagers, "backgroundColor", lastBackgroundColor, newColor)
                    .setDuration(1000);
            sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());
            sunsetSkyAnimator.start();
        }

        lastBackgroundColor = newColor;
    }
}
