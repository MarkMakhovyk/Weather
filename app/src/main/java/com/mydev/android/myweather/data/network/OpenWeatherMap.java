package com.mydev.android.myweather.data.network;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mydev.android.myweather.data.model.Forecast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenWeatherMap {
    private static final String TAG = "Forecast";
    private static final String API_KEY = "2842e4fe031b0d2c1fa0294b481ff31a";


    Uri ENDPOINT = Uri
            .parse("http://api.openweathermap.org/data/2.5/forecast?")
            .buildUpon()
            .appendQueryParameter("lang", "ru")
            .appendQueryParameter("appid", API_KEY)
            .build();

    public String buildUrl(String city, int count) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon().appendQueryParameter("q", city);
        if (count > 0) {
            uriBuilder = uriBuilder.appendQueryParameter("cnt", String.valueOf(count));
        }
        return uriBuilder.toString();

    }
    public void buildUrl(Location location) {
        Log.e(TAG, "buildUrl: " + location.getLatitude() + " " + location.getLongitude() );
        ENDPOINT = Uri.parse("http://api.openweathermap.org/data/2.5/forecast?")
                .buildUpon()
                .appendQueryParameter("lang", "ru")
                .appendQueryParameter("appid", API_KEY)
                .appendQueryParameter("lat", "" + location.getLatitude())
                .appendQueryParameter("lon", "" + location.getLongitude())
                .build();
    }


    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            Log.e(TAG, "getUrlBytes: disconnect");
            connection.disconnect();
        }
    }
    public String getUrlString(String urlSpec) throws IOException {
        byte[] b = getUrlBytes(urlSpec);
        if (b == null) {
            return "";
        }

        return new String(b);
    }

    public Forecast getWeather(String cityName, int count) {

        return downloadGalleryItems(buildUrl(cityName, count));
    }

    public Forecast getWeather(String cityName) {

        return downloadGalleryItems(buildUrl(cityName, 0));
    }

    private Forecast downloadGalleryItems(String url) {
        Forecast forecast = null;
        try {
            Log.i(TAG, "url: " + url);

            String jsonString = getUrlString(url);
            if (jsonString == "") {
                return null;
            }
            forecast = jsonToWeather(jsonString);
            Log.i(TAG, "Received JSON: " + jsonString);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return forecast;

    }
    Forecast jsonToWeather(String json) {
        Gson gson = new GsonBuilder().create();
        Forecast forecast = null;
        try {

            forecast = gson.fromJson(json, Forecast.class);
        } catch (Exception ioe) {
            Log.e(TAG, "jsonToWeather: ",ioe);
            ioe.printStackTrace();
        }
        return forecast;
    }

}
