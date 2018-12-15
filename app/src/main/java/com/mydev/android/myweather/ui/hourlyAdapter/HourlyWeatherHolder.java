package com.mydev.android.myweather.ui.hourlyAdapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.model.Forecast;
import com.mydev.android.myweather.data.model.ItemListWeather;

import Utills.GetInfoWeather;

public class HourlyWeatherHolder extends RecyclerView.ViewHolder {
    ImageView iconImageView;
    TextView dateTextView;
    TextView temp;
Forecast forecast;

    public HourlyWeatherHolder(@NonNull View itemView, Forecast forecast) {
        super(itemView);
        temp = (TextView) itemView.findViewById(R.id.forecast);
        dateTextView = (TextView) itemView.findViewById(R.id.date);
        iconImageView = (ImageView) itemView.findViewById(R.id.icon_weather_hourly);
        this.forecast = forecast;
    }

    public void bind(ItemListWeather itemListWeather) {

        GetInfoWeather getInfoWeather = new GetInfoWeather(forecast);
        int index = forecast.getList().indexOf(itemListWeather);
        dateTextView.setText(getInfoWeather.getTime(index));
        temp.setText(getInfoWeather.getTemp(index));
        iconImageView.setImageResource(getInfoWeather.getIcon(index));
    }
}
