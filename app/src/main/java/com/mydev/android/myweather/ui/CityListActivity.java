package com.mydev.android.myweather.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.cityUA.City;
import com.mydev.android.myweather.cityUA.CityList;
import com.mydev.android.myweather.data.CityForecastList;
import com.mydev.android.myweather.data.model.weather.Forecast;
import com.mydev.android.myweather.data.network.CityPreference;
import com.mydev.android.myweather.data.network.ForecastTack;
import com.mydev.android.myweather.data.network.GPSLocation;
import com.mydev.android.myweather.data.network.GPSLocation.MyLocation;
import com.mydev.android.myweather.ui.adapter.CityAdapter;
import com.mydev.android.myweather.ui.adapter.FindCityAdapter;
import com.mydev.android.myweather.ui.adapter.FindCityAdapter.ResultFind;

import java.util.List;
import java.util.concurrent.ExecutionException;

import Utills.GetInfoWeather;
import Utills.Utills;
import butterknife.BindView;
import butterknife.ButterKnife;


public class CityListActivity extends AppCompatActivity implements ResultFind, MyLocation {
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;
    private static final int SERVICE_LOCATION_DISABLE = -1;

    private List<Forecast> list;
    private List<City> cities;
    CityList cityList;
    private Context context;
    GPSLocation gpsLocation;

    @BindView(R.id.location_city)
    ConstraintLayout locationLayout;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.city_list_view)
    RecyclerView cityListView;

    @BindView(R.id.add_city)
    FloatingActionButton fab;

    @BindView(R.id.back)
    ImageButton back;

    @BindView(R.id.icon_location)
    ImageView locationIcon;

    @BindView(R.id.location)
    Switch aSwitch;

    @BindView(R.id.main_location)
    TextView locationMain;

    @BindView(R.id.description_location)
    TextView locationDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        context = this;
        if (CityPreference.getLastLocation(this) != null) {
            setDataLocation(CityForecastList.get(this).getForecast(CityPreference.getLastLocation(this)));
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    onLocation();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                findViewById(R.id.text_name_list_activity).setVisibility(View.GONE);
                locationLayout.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
                searchView.setIconified(false);
                cityList = new CityList(context);
                cities = cityList.getCityList();
                updateUI(cities);
                fab.setVisibility(View.GONE);
            }
        });

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
                onBackPressed();
            }
        });


        cityListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list = CityForecastList.get(this).getForecasts();
        cityListView.setAdapter(new CityAdapter(list, context));
    }

    @Override
    protected void onPause() {
        super.onPause();

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
        gpsLocation = new GPSLocation(context, this, this);
        gpsLocation.addClient();
    }

    void updateUI(List list) {
        cityListView.setAdapter(new FindCityAdapter(list, context, this));
    }


    void findForecast(String cityName) {
        if (Utills.isOnline(context)) {
            ForecastTack m = new ForecastTack(context, cityName);
            m.execute();

            try {
                Forecast forecast = m.get();
                if (forecast != null) {
                    finish();

                } else
                    Toast.makeText(context, "NOT FOUND", Toast.LENGTH_LONG).show();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(context, "Нету подключения, проверте соидинение", Toast.LENGTH_LONG).show();

    }


    @Override
    public void backPress() {
        finish();
    }

    @Override
    public void returnLocation(Location location) {

        ForecastTack m = new ForecastTack(context,
                String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        m.execute();

        try {
            Forecast forecast = m.get();
            if (forecast != null) {
                setDataLocation(forecast);

            } else
                Toast.makeText(context, "NOT FOUND", Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void setDataLocation(Forecast forecast) {
        GetInfoWeather getInfoWeather = new GetInfoWeather(forecast);
        String fm = getInfoWeather.getTemp(0) + ", "
                + forecast.getList().get(0).getWeatherDescription().get(0).getDescription();
        locationDescription.setText(fm);
        locationMain.setText(forecast.getCity().getName());
        locationIcon.setImageResource(getInfoWeather.getIcon(0));
        CityPreference.setLastLocation(context, forecast.getCity().getName());
    }

    @Override
    public void permissions(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions,
                requestCode);
    }
}


