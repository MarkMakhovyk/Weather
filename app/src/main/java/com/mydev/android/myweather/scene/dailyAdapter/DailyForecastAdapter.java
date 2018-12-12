package com.mydev.android.myweather.scene.dailyAdapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.model.Forecast;
import com.mydev.android.myweather.data.model.ItemListWeather;

import java.util.Date;
import java.util.List;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastHolder> {
    private DailyForecastHolder.OnItemClick mOnItemClick;
    private Forecast forecast;
    private static final int COUNT_FORECAST_PER_DAY = 8;
    ItemListWeather item;


    public DailyForecastAdapter(Forecast forecast, @NonNull DailyForecastHolder.OnItemClick onItemClick) {
        this.forecast = forecast;
        mOnItemClick = onItemClick;
    }
    private int countOfWeatherDay() {
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
    public DailyForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyForecastHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day, parent, false), forecast, mOnItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyForecastHolder holder, int position) {
        holder.bind(forecast.getList().get(position*COUNT_FORECAST_PER_DAY));



    }

    @Override
    public int getItemCount() {
        return countOfWeatherDay() ;
    }
}

