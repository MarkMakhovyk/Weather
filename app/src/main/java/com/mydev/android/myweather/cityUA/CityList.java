package com.mydev.android.myweather.cityUA;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CityList {
    Context context;

    public CityList(Context context) {
        this.context = context;
    }

    public List<City> getCityList() {

        String jsonText = loadJSONFromAsset();
        List<City> cities = new ArrayList<>();
        try {
            JSONObject jsonOll = new JSONObject(jsonText);
            JSONArray listRegion = jsonOll.getJSONObject("regions").getJSONArray("region");
            for (int indexRegion = 0; indexRegion < listRegion.length(); indexRegion++) {
                JSONObject region = listRegion.getJSONObject(indexRegion);
                JSONArray cityByRegion = region.getJSONArray("city");
                String regionName = region.getString("_name") + ",UA";
                for (int indexCity = 0; indexCity < cityByRegion.length(); indexCity++) {
                    JSONObject city = cityByRegion.getJSONObject(indexCity);
                    cities.add(new City(city.getString("_name"),
                            regionName,
                            city.getString("_lat"),
                            city.getString("_lon")
                    ));
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cities;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("cityUA.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public List<City> query(List<City> cities, String query) {
        List<City> queryList = new ArrayList<>();
        for (City c : cities) {
            if (c.getName().toLowerCase().contains(query.toLowerCase())) {
                queryList.add(c);
            }
        }

        return queryList;
    }
}
