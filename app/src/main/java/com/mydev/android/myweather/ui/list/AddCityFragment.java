package com.mydev.android.myweather.ui.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.cityUA.CityList;
import com.mydev.android.myweather.data.ForecastDAO;
import com.mydev.android.myweather.data.model.City;
import com.mydev.android.myweather.data.model.Forecast;
import com.mydev.android.myweather.data.network.CityPreference;
import com.mydev.android.myweather.data.network.GetForecast;
import com.mydev.android.myweather.ui.adapter.FindCityAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCityFragment extends Fragment implements GetForecast.CallbackLoadForecast, FindCityAdapter.findCityClick {

    private List<City> cities;
    private CityList cityList;
    private GetForecast getForecast = new GetForecast(this);
    private ForecastDAO forecastDAO = ForecastDAO.get(getContext());

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.addCityList)
    RecyclerView addCityList;

    @BindView(R.id.back)
    ImageView back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_add_city, container, false);
        ButterKnife.bind(this, view);

        setListener();

        addCityList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        cityList = new CityList(getContext());
        cities = cityList.getCityList();
        updateUI(cities);
        return view;
    }

    private void setListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                findForecast(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                updateUI(cityList.query(cities, s));
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }


    void updateUI(List list) {
        addCityList.setAdapter(new FindCityAdapter(list, getContext(), this));
    }


    void findForecast(String cityName) {
        getForecast.getWeatherByCity(cityName);
    }

    @Override
    public void onClickFindCity(City city) {
        getForecast.getWeatherByLocation(city.getLat(), city.getLon());
    }


    @Override
    public void onResponse(Forecast forecast) {
        forecastDAO.addOrUpdate(forecast);

        CityPreference.setCityVisible(getContext(), forecast.getCity().getName());
        getActivity().finish();
    }
}

