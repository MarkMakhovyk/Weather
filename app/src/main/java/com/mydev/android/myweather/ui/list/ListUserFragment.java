package com.mydev.android.myweather.ui.list;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.ForecastDAO;
import com.mydev.android.myweather.data.model.Forecast;
import com.mydev.android.myweather.data.network.CityPreference;
import com.mydev.android.myweather.data.network.GPSLocation;
import com.mydev.android.myweather.data.network.GetForecast;
import com.mydev.android.myweather.ui.adapter.CityAdapter;

import java.util.List;

import Utills.GetInfoWeather;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListUserFragment extends Fragment implements GPSLocation.MyLocation, GetForecast.CallbackLoadForecast {

    private static final int REQUEST_LOCATION_PERMISSIONS = 0;

    private List<Forecast> forecastList;
    private GetForecast getForecast = new GetForecast(this);
    GPSLocation gpsLocation;
    private ForecastDAO forecastDAO = ForecastDAO.get(getContext());

    @BindView(R.id.userCityList)
    RecyclerView userCityList;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.icon_location)
    ImageView locationIcon;

    @BindView(R.id.switch_location)
    Switch aSwitchLocation;

    @BindView(R.id.main_location)
    TextView locationMain;

    @BindView(R.id.description_location)
    TextView locationDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_show_list, container, false);
        ButterKnife.bind(this, view);

        if (CityPreference.getLastLocation(getContext()) != null) {
            setDataLocation(forecastDAO.getForecast(CityPreference.getLastLocation(getContext())));
        }

        setListener();

        userCityList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        forecastList = forecastDAO.getForecasts();
        userCityList.setAdapter(new CityAdapter(forecastList, getContext()));
        return view;
    }

    private void setListener() {
        aSwitchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    onLocation();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                if (gpsLocation.hasLocationPermission()) {
                    gpsLocation.getGPS();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    void onLocation() {
        gpsLocation = new GPSLocation(getContext(), this);
        gpsLocation.addClient();
    }


    @Override
    public void returnLocation(Location location) {
        getForecast.getWeatherByLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
    }

    void setDataLocation(Forecast forecast) {
        GetInfoWeather getInfoWeather = new GetInfoWeather(forecast);
        String fm = getInfoWeather.getTemp(0) + ", "
                + forecast.getList().get(0).getWeatherDescription().get(0).getDescription();
        locationDescription.setText(fm);
        locationMain.setText(forecast.getCity().getName());
        locationIcon.setImageResource(getInfoWeather.getIcon(0));
        CityPreference.setLastLocation(getContext(), forecast.getCity().getName());
    }

    @Override
    public void permissions(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(getActivity(), permissions,
                requestCode);
    }

    @Override
    public void onResponse(Forecast forecast) {
        forecastDAO.addOrUpdate(forecast);
        setDataLocation(forecast);
    }
}
