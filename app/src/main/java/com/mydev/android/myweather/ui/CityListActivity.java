package com.mydev.android.myweather.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.CityForecastList;
import com.mydev.android.myweather.data.model.Forecast;
import com.mydev.android.myweather.data.network.OpenWeatherMap;

import java.util.List;

import Utills.GetInfoWeather;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CityListActivity extends AppCompatActivity {
    SearchView searchView;
    Forecast forecast;
    List<Forecast> list;
    Context context;
    RecyclerView cityListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        searchView = (SearchView) findViewById(R.id.search_view);
        context = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_city);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchView.setIconified(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                myTast m = new myTast(s);
                m.execute();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        cityListView = (RecyclerView) findViewById(R.id.city_list_view);
        cityListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list = CityForecastList.get(this).getForecasts();
        cityListView.setAdapter(new CityListAdapter(list));
    }

    class CityListAdapter extends RecyclerView.Adapter<CityListHolder> {
        List<Forecast> list;

        public CityListAdapter(List<Forecast> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public CityListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new CityListHolder(LayoutInflater.from(CityListActivity.this)
                    .inflate(R.layout.item_city, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CityListHolder viewHolder, int i) {
            viewHolder.bind(list.get(i));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class CityListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.city_name)
        TextView cityName;

        @BindView(R.id.forecast)
        TextView forecastMain;

        @BindView(R.id.icon)
        ImageView icon;


        public CityListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(Forecast forecast) {
            GetInfoWeather getInfoWeather = new GetInfoWeather(forecast);
            String fm = getInfoWeather.getTemp(0) + ", "
                    + forecast.getList().get(0).getWeatherDescription().get(0).getDescription();
            forecastMain.setText(fm);
            cityName.setText(forecast.getCity().getName());
            icon.setImageResource(getInfoWeather.getIcon(0));

        }
    }

    class myTast extends AsyncTask {
        String s;

        public myTast(String s) {
            this.s = s;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            OpenWeatherMap owm = new OpenWeatherMap();
            forecast = owm.getWeather(s, 1);
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (forecast != null) {
                list.add(forecast);
                CityListAdapter adapter = new CityListAdapter(list);
                cityListView.setAdapter(adapter);
            } else
                Toast.makeText(context, "NOT FOUND", Toast.LENGTH_LONG).show();


        }
    }

}
