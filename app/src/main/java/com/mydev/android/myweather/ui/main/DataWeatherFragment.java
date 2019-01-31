package com.mydev.android.myweather.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.ForecastDAO;
import com.mydev.android.myweather.data.model.Forecast;
import com.mydev.android.myweather.data.model.ItemListWeather;
import com.mydev.android.myweather.data.network.GetForecast;
import com.mydev.android.myweather.ui.adapter.DailyForecastAdapter;
import com.mydev.android.myweather.ui.adapter.HourlyWeatherAdapter;
import com.mydev.android.myweather.ui.list.CityListActivity;

import Utills.GetInfoWeather;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DataWeatherFragment extends Fragment implements DailyForecastAdapter.OnItemClick, GetForecast.CallbackLoadForecast {
    private static final String CITY_NAME = "cityName";

    GetInfoWeather getInfoWeather = null;
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

    @BindView(R.id.city_list)
    ImageButton settings;


    DailyForecastAdapter dayAdapter;
    HourlyWeatherAdapter hourlyAdapter;
    GetForecast getForecast;
    String cityName;
    ForecastDAO forecastDAO;


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
        forecastDAO = ForecastDAO.get(getContext());
        getForecast = new GetForecast(this);
        forecast = forecastDAO.getForecast(cityName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.data_weather, container, false);

        ButterKnife.bind(this, view);
        updateForecast();

        dayRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        hourlyRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getForecast.getWeatherByCity(cityName);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CityListActivity.class));
            }
        });

        return view;
    }


    private void updateForecast() {
        setDataForecast();
    }

    private void setDataForecast() {
        setDataRecycleView();
        setDataWeatherToday();
    }

    private void setDataRecycleView() {
        dayAdapter = new DailyForecastAdapter(forecast, this);
        dayRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                dayAdapter.countOfWeatherDay()));
        dayRecyclerView.setAdapter(dayAdapter);
        hourlyAdapter = new HourlyWeatherAdapter(forecast, forecast.getList().get(0));
        hourlyRecyclerView.setAdapter(hourlyAdapter);
    }

    private void setDataWeatherToday() {
        getInfoWeather = new GetInfoWeather(forecast);
        tempNow.setText(getInfoWeather.getTemp(0));
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
            hourlyAdapter = new HourlyWeatherAdapter(forecast, itemListWeather);
            hourlyRecyclerView.setAdapter(hourlyAdapter);
        }
    }

    @Override
    public void onResponse(Forecast forecast) {
        if (forecast != null)
            forecastDAO.updateForecast(forecast);

        this.forecast = forecast;
        swipe.setRefreshing(false);
        updateForecast();
    }
}