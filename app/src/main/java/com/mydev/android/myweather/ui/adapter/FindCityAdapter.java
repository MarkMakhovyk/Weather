package com.mydev.android.myweather.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.model.City;

import java.util.List;

public class FindCityAdapter extends RecyclerView.Adapter<ForecastHolder> {
    private findCityClick findCityClick;

    private List<City> cities;
    private Context context;


    public FindCityAdapter(List<City> cities, Context context, @NonNull findCityClick findCityClick) {
        this.cities = cities;
        this.context = context;
        this.findCityClick = findCityClick;
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
                findCityClick.onClickFindCity(city);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public interface findCityClick {
        void onClickFindCity(City city);
    }

}
