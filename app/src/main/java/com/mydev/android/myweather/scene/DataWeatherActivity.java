package com.mydev.android.myweather.scene;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.model.Forecast;

import java.util.ArrayList;
import java.util.List;

public class DataWeatherActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<String> cityName = new ArrayList<>();
    String city = "Moscow";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagers_data_weather);
        cityName.add("Kyiv");
        cityName.add("Moscow");
        cityName.add("Chernihiv");
        mViewPager = (ViewPager) findViewById(R.id.weather_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                String city = cityName.get(position);
                return DataWeatherFragment.newInstance(city);
            }

            @Override
            public int getCount() {
                return cityName.size();
            }
        });

        for (int i = 0; i < cityName.size(); i++) {
            if (cityName.get(i).equals(city)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
