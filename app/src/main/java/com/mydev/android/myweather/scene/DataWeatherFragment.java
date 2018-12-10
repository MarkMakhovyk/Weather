package com.mydev.android.myweather.scene;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.model.Forecast;
import com.mydev.android.myweather.data.model.ItemListWeather;
import com.mydev.android.myweather.network.OpenWeatherMap;

import Utills.GetInfoWeather;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DataWeatherFragment extends Fragment implements WeatherHolder.OnItemClick {

   private static final String TAG = "Forecast";
    GetInfoWeather getInfoWeather = null;
    private static final String CITY_NAME = "cityName";


    Forecast forecast = null;

    @BindView(R.id.city)
    TextView cityTextView;

    @BindView(R.id.recycler_view_weather_day)
    RecyclerView dayRecyclerView;

    @BindView(R.id.recycler_view_weather_hourly)
    RecyclerView hourlyRecyclerView;

    @BindView(R.id.temp_now)
    TextView tempNow;

    @BindView(R.id.icon_weather_today)
    ImageView icon_weather_now;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.humidity)
    TextView humidity;

    @BindView(R.id.pressure)
    TextView pressure;

    @BindView(R.id.wind)
    TextView wind;

    @BindView(R.id.clouds)
    TextView clouds;


    WeatherAdapter dayAdapter;
    DayWeatherAdapter hourlyAdapter;
    MyTask m;
    String cityName;

    public static Fragment newInstance(String city) {
        Bundle args = new Bundle();
        args.putSerializable(CITY_NAME, city);

        DataWeatherFragment fragment = new DataWeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       cityName = (String) getArguments().getSerializable(CITY_NAME);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_weather, container, false);

        ButterKnife.bind(this, view);

        m = new MyTask();
        m.setNameCity(cityName);
        m.execute();

        dayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        hourlyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyTask m = new MyTask();
                m.execute();
                swipe.setRefreshing(false);
            }
        });

        return view;


    }



    class MyTask extends AsyncTask<Void, Void, Void> {
        String nameCity = null;


        public void setNameCity(String nameCity) {
            this.nameCity = nameCity;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            OpenWeatherMap op = new OpenWeatherMap();
            forecast = op.getWeather(nameCity);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (forecast != null) {
                Log.e(TAG, "onPostExecute: " );
                getInfoWeather = new GetInfoWeather(forecast);
                setDataRecycleView();
                setDataWeatherToday();
            }
        }
    }

    void setDataRecycleView() {
        dayAdapter = new WeatherAdapter(forecast,this);
        dayRecyclerView.setAdapter(dayAdapter);
        hourlyAdapter = new DayWeatherAdapter(forecast,forecast.getList().get(0));
        hourlyRecyclerView.setAdapter(hourlyAdapter);
    }

    void setDataWeatherToday() {
        tempNow.setText(getInfoWeather.getTemp(0) );
        description.setText(getInfoWeather.getDescription(0));
        icon_weather_now.setImageResource(getInfoWeather.getIcon(0));
        humidity.setText(getInfoWeather.getWeatherHumidity(0));
        pressure.setText(getInfoWeather.getWeatherPressure(0));
        wind.setText(getInfoWeather.getWeatherWind(0));
        clouds.setText(getInfoWeather.getWeatherClouds(0));
        cityTextView.setText(forecast.getCity().getName());
    }

    @Override
    public void onItemClick(@NonNull ItemListWeather itemListWeather) {
        if (itemListWeather != null) {
            hourlyAdapter = new DayWeatherAdapter(forecast,itemListWeather);
            hourlyRecyclerView.setAdapter(hourlyAdapter);
        }
    }

}
