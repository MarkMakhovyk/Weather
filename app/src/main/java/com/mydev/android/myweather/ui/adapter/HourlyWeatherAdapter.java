package com.mydev.android.myweather.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.model.Forecast;
import com.mydev.android.myweather.data.model.ItemListWeather;

import java.util.Date;
import java.util.List;

import Utills.GetInfoWeather;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<ForecastHolder> {
    Forecast forecast;
    int startPosition;
    int count;
    private static final String TAG = "Forecast";
    private List<ItemListWeather> listWeathers;
    private static final int COUNT_FORECAST_PER_DAY = 7;

    public HourlyWeatherAdapter(Forecast forecast, ItemListWeather item) {
        this.forecast = forecast;
        listWeathers = forecast.getList();
        startPosition = forecast.getList().indexOf(item);
        Log.e(TAG, "HourlyWeatherAdapter: " + item.getDt());
        howCountForecastToday();

    }


    private void howCountForecastToday() {
        int start = 0;
        int finish = 0;
        boolean ok = true;
        for (int i = 0; ok ;i++) {
            ok = false;
            if ((startPosition - i) >= 0) {
                if ((new Date((listWeathers.get(startPosition - i).getDt())* 1000L)).getDay()
                        == (new Date((listWeathers.get(startPosition).getDt()) * 1000L)).getDay()) {
                    start = startPosition -i;
                    ok = true;
                }

            }
            if ((startPosition + i) < listWeathers.size()) {
                if ((new Date((listWeathers.get(startPosition + i).getDt()) * 1000L)).getDay()
                        == (new Date((listWeathers.get(startPosition).getDt()) * 1000L)).getDay()) {
                    finish = startPosition + i;
                    ok = true;
                }
            }
        }

        startPosition = start;
        count = finish - start;


    }

    @Override
    public ForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForecastHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ForecastHolder holder, int position) {
        GetInfoWeather getInfoWeather = new GetInfoWeather(forecast);
        int index = startPosition + position;
        holder.main.setText(getInfoWeather.getTime(index));
        holder.description.setText(getInfoWeather.getTemp(index));
        holder.icon.setImageResource(getInfoWeather.getIcon(index));
    }

    @Override
    public int getItemCount() {
        if ((startPosition) + COUNT_FORECAST_PER_DAY > listWeathers.size()) {
            return listWeathers.size()-startPosition;
        } else if (count < COUNT_FORECAST_PER_DAY && count + COUNT_FORECAST_PER_DAY < listWeathers.size()) {
            count = 7;
        }
        return count;
    }

}
