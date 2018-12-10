package com.mydev.android.myweather.scene;

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

public class DayWeatherAdapter  extends RecyclerView.Adapter<DayWeatherHolder>{
    Forecast forecast;
    int startPosition;
    int count;
    private static final String TAG = "Forecast";
    private List<ItemListWeather> listWeathers;
    private static final int COUNT_FORECAST_PER_DAY = 8;

    public DayWeatherAdapter(Forecast forecast, ItemListWeather item) {
        this.forecast = forecast;
        listWeathers = forecast.getList();
        startPosition = forecast.getList().indexOf(item);
        Log.e(TAG, "DayWeatherAdapter: " + item.getDt() );
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
        count = finish-start +1;


    }

    @Override
    public DayWeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DayWeatherHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item,parent,false), forecast);

    }

    @Override
    public void onBindViewHolder(@NonNull DayWeatherHolder holder, int position) {
        holder.bind(listWeathers.get(startPosition + position));
    }

    @Override
    public int getItemCount() {
        if ((startPosition) + COUNT_FORECAST_PER_DAY > listWeathers.size()) {
            return listWeathers.size()-startPosition;
        }
        return count;
    }

}
