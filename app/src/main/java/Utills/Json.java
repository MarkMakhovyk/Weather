package Utills;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mydev.android.myweather.data.model.Forecast;

public class Json {
    public static Forecast jsonToWeather(String json) {
        Gson gson = new GsonBuilder().create();
        Forecast forecast = null;
        try {
            forecast = gson.fromJson(json, Forecast.class);
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        return forecast;
    }
}
