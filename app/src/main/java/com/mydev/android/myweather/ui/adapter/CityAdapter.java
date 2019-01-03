package com.mydev.android.myweather.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.CityForecastList;
import com.mydev.android.myweather.data.model.weather.Forecast;

import java.util.List;

import Utills.GetInfoWeather;

public class CityAdapter extends RecyclerView.Adapter<ForecastHolder> {
    List<Forecast> list;
    Context context;

    public CityAdapter(List<Forecast> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ForecastHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ForecastHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_city, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastHolder holder, int i) {
        final Forecast forecast = list.get(i);
        GetInfoWeather getInfoWeather = new GetInfoWeather(forecast);
        String fm = getInfoWeather.getTemp(0) + ", "
                + forecast.getList().get(0).getWeatherDescription().get(0).getDescription();
        holder.description.setText(fm);
        holder.main.setText(forecast.getCity().getName());
        holder.icon.setImageResource(getInfoWeather.getIcon(0));
        holder.itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityForecastList.get(context).deleteForecast(forecast.getCity().getName());
                list = CityForecastList.get(context).getForecasts();
                CityAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

