package com.mydev.android.myweather.data.network;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mydev.android.myweather.data.model.weather.Forecast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
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

    public Forecast getWeatherForLocation(String lon, String lat) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon()
                .appendQueryParameter("lat", "" + lat)
                .appendQueryParameter("lon", "" + lon);

        return downloadGalleryItems(uriBuilder.toString());
    }

    public Forecast getWeather(String city) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon().appendQueryParameter("q", city);
        return downloadGalleryItems(uriBuilder.toString());
    }

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(1000);
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
        } catch (SocketTimeoutException e) {
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


    private Forecast downloadGalleryItems(String url) {
        Forecast forecast = null;
        try {
            Log.e(TAG, "url: " + url);

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
