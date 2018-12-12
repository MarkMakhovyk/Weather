package com.mydev.android.myweather.scene.dailyAdapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.model.Forecast;
import com.mydev.android.myweather.data.model.ItemListWeather;

import Utills.GetInfoWeather;

public class DailyForecastHolder extends RecyclerView.ViewHolder {
    private OnItemClick mOnItemClick;
    private Forecast forecast;
    ImageView iconImageView;
    TextView dateTextView;
    TextView temp;
    ItemListWeather itemListWeather= null;

    public DailyForecastHolder(@NonNull View itemView, Forecast forecast, @NonNull OnItemClick onItemClick) {
        super(itemView);
        temp= (TextView) itemView.findViewById(R.id.temp_now);
        dateTextView = (TextView) itemView.findViewById(R.id.date);
        iconImageView = (ImageView) itemView.findViewById(R.id.icon_weather_day);
        this.forecast = forecast;

        itemView.setOnClickListener(mInternalListener);
        this.mOnItemClick = onItemClick;
    }


    public void bind(ItemListWeather itemList) {
        this.itemListWeather = itemList;

        GetInfoWeather getInfoWeather = new GetInfoWeather(forecast);
        int index = forecast.getList().indexOf(itemListWeather);
        dateTextView.setText(getInfoWeather.getDay(index));
        temp.setText(getInfoWeather.getTemp(index));
        iconImageView.setImageResource(getInfoWeather.getIcon(index));


    }
    private final View.OnClickListener mInternalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (itemListWeather != null) {
                mOnItemClick.onItemClick(itemListWeather);
            }

        }


    };

    public interface OnItemClick {
        void onItemClick(@NonNull ItemListWeather itemListWeather);
    }


}
