package com.mydev.android.myweather.scene;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.CityForecastList;
import com.mydev.android.myweather.data.model.Forecast;
import com.mydev.android.myweather.data.model.ItemListWeather;
import com.mydev.android.myweather.network.OpenWeatherMap;
import com.mydev.android.myweather.scene.dailyAdapter.DailyForecastAdapter;
import com.mydev.android.myweather.scene.dailyAdapter.DailyForecastHolder;
import com.mydev.android.myweather.scene.hourlyAdapter.HourlyWeatherAdapter;

import java.util.Date;

import Utills.GetInfoWeather;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DataWeatherFragment extends Fragment implements DailyForecastHolder.OnItemClick {

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
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    DailyForecastAdapter dayAdapter;
    HourlyWeatherAdapter hourlyAdapter;
    MyTask m;
    String cityName;
    CityForecastList cityForecastList;
    Window window;

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
        cityForecastList = CityForecastList.get(getContext());
        forecast = cityForecastList.getForecast(cityName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_weather, container, false);
        window = getActivity().getWindow();
        ButterKnife.bind(this, view);

        m = new MyTask();
        if (getForecast()) {
            m.setCity(cityName);
            m.execute();
        }

        dayRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));

        hourlyRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyTask m = new MyTask();
                m.setCity(cityName);
                m.execute();
                swipe.setRefreshing(false);
            }
        });

        return view;


    }



    class MyTask extends AsyncTask<Void, Void, Void> {

        String city;

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            OpenWeatherMap op = new OpenWeatherMap();
            forecast = op.getWeather(cityName);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (forecast != null) {
                setData();
                if (cityForecastList.getForecast(cityName) == null) {
                    cityForecastList.addForecast(forecast.getCity().getName(), cityForecastList.forecastToJson(forecast));
                } else
                    cityForecastList.updateForecast(forecast);
            }
        }
    }

    private boolean getForecast() {
        if (forecast != null) {
            setData();
        }
        if (forecast == null || tiredForecast(forecast)) {
            return true;
        }
        return false;
    }

    private void setData() {
        getInfoWeather = new GetInfoWeather(forecast);
        setColor();
        setDataRecycleView();
        setDataWeatherToday();
    }

    private void setColor() {
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorYellow));
        swipe.setBackgroundColor(getResources().getColor(R.color.colorYellow));
    }

    private boolean tiredForecast(Forecast forecast) {
        if (forecast == null) {
            return true;
        }
        boolean ok = true;
        while (ok) {
            ok = false;
            long date = forecast.getList().get(0).getDt();
            long dateNow = new Date().getTime();
            if (date + 10800000 > dateNow) {
                ok = true;
                forecast.getList().remove(0);
            }
        }
        cityForecastList.updateForecast(forecast);
        return false;

    }

    private void setDataRecycleView() {
        dayAdapter = new DailyForecastAdapter(forecast, this);
        dayRecyclerView.setAdapter(dayAdapter);
        hourlyAdapter = new HourlyWeatherAdapter(forecast, forecast.getList().get(0));
        hourlyRecyclerView.setAdapter(hourlyAdapter);
    }

    private void setDataWeatherToday() {
        try {
            tempNow.setText(getInfoWeather.getTemp(0));

            description.setText(getInfoWeather.getDescription(0));
            icon_weather_now.setImageResource(getInfoWeather.getIcon(0));
            humidity.setText(getInfoWeather.getWeatherHumidity(0));
            pressure.setText(getInfoWeather.getWeatherPressure(0));
            wind.setText(getInfoWeather.getWeatherWind(0));
            clouds.setText(getInfoWeather.getWeatherClouds(0));
            cityTextView.setText(forecast.getCity().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(@NonNull ItemListWeather itemListWeather) {
        if (itemListWeather != null) {
            hourlyAdapter = new HourlyWeatherAdapter(forecast, itemListWeather);
            hourlyRecyclerView.setAdapter(hourlyAdapter);
        }
    }

}
