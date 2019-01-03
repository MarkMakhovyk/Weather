package com.mydev.android.myweather.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.model.weather.Forecast;
import com.mydev.android.myweather.data.model.weather.ItemListWeather;

import java.util.Date;
import java.util.List;

import Utills.GetInfoWeather;

public class DailyForecastAdapter extends RecyclerView.Adapter<ForecastHolder> {
    private OnItemClick mOnItemClick;
    private Forecast forecast;
    private static final int COUNT_FORECAST_PER_DAY = 8;

    public DailyForecastAdapter(Forecast forecast, @NonNull OnItemClick onItemClick) {
        this.forecast = forecast;
        mOnItemClick = onItemClick;
    }

    public int countOfWeatherDay() {
        int count = 0;
        int day = 0;
        List<ItemListWeather> listWeathers = forecast.getList();

        for (ItemListWeather i : listWeathers) {
            if ((new Date((listWeathers.get(day).getDt()) * 1000L)).getDay()
                    == (new Date((i.getDt()) * 1000L)).getDay()) {
                continue;
            }
            day = listWeathers.indexOf(i);
            count++;
        }
        return count;
    }

    @Override
    public ForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForecastHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastHolder holder, int position) {
        GetInfoWeather getInfoWeather = new GetInfoWeather(forecast);
        int index = position * COUNT_FORECAST_PER_DAY;
        holder.main.setText(getInfoWeather.getDay(index));
        holder.description.setText(getInfoWeather.getTemp(index));
        holder.icon.setImageResource(getInfoWeather.getIcon(index));
        holder.itemView.setOnClickListener(mInternalListener);
        holder.itemView.setTag(forecast.getList().get(index));

    }

    @Override
    public int getItemCount() {
        return countOfWeatherDay();
    }

    private final View.OnClickListener mInternalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ItemListWeather itemListWeather = (ItemListWeather) view.getTag();
            mOnItemClick.onItemClick(itemListWeather);
        }
    };

    public interface OnItemClick {
        void onItemClick(@NonNull ItemListWeather itemListWeather);
    }

}

