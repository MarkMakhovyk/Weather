package com.mydev.android.myweather.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.cityUA.City;
import com.mydev.android.myweather.data.network.CityPreference;
import com.mydev.android.myweather.data.network.ForecastTack;

import java.util.List;
import java.util.concurrent.ExecutionException;

import Utills.Utills;

public class FindCityAdapter extends RecyclerView.Adapter<ForecastHolder> {
    private ResultFind resultFind;

    private List<City> cities;
    private Context context;


    public FindCityAdapter(List<City> cities, Context context, @NonNull ResultFind resultFind) {
        this.cities = cities;
        this.context = context;
        this.resultFind = resultFind;
    }

    @NonNull
    @Override
    public ForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForecastHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_city, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastHolder holder, int position) {
        final City city = cities.get(position);
        holder.main.setText(city.getName());
        holder.description.setText(city.getRegions());
        holder.icon.setVisibility(View.GONE);
        holder.itemView.findViewById(R.id.delete).setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utills.isOnline(context)) {
                    ForecastTack forecastTack = new ForecastTack(context,
                            city.getLon(), city.getLat());
                    forecastTack.execute();
                    try {
                        if (forecastTack.get() != null) {
                            CityPreference.setCityVisible(context, forecastTack.get().getCity().getName());
                            resultFind.backPress();
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(context, "Нету подключения, проверте соидинение", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public interface ResultFind {
        void backPress();
    }

}
