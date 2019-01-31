package Utills;

import android.content.Context;
import android.util.Log;

import com.mydev.android.myweather.R;
import com.mydev.android.myweather.data.ForecastDAO;
import com.mydev.android.myweather.data.model.Forecast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetInfoWeather {
    private static final String TAG = "Forecast";

    Forecast forecast;
    public GetInfoWeather(Forecast w) {
        forecast = w;
    }

    public String getDateToString(int date) {
        return DateFormat.getDateInstance(DateFormat.SHORT, new Locale("ru")).format(new Date(date * 1000L))
                + DateFormat.getTimeInstance(DateFormat.SHORT, new Locale("ru")).format(new Date(date * 1000L))
                ;
    }

    public String getDay(int index) {
        return new SimpleDateFormat("EEE", new Locale("ru"))
                .format(new Date(forecast.getList().get(index).getDt() * 1000L));

    }
    public String getTime(int index) {
        return new SimpleDateFormat("HH", new Locale("ru"))
                .format(new Date(forecast.getList().get(index).getDt() * 1000L));

    }

    public int getIcon(int index) {
        String icon = forecast.getList().get(index).getWeatherDescription().get(0).getIcon();
        switch (icon) {
            case "01d" :return R.drawable.clear_sky_d;
            case "01n" :return R.drawable.clear_sky_n;
            case "02d" :return R.drawable.few_clouds_d;
            case "02n" :return R.drawable.few_clouds_n;
            case "03d":
                return R.drawable.clouds;
            case "03n":
                return R.drawable.clouds;
            case "04d":
                return R.drawable.clouds;
            case "04n":
                return R.drawable.clouds;
            case "09d":
                return R.drawable.shower_rain;
            case "09n":
                return R.drawable.shower_rain;
            case "10d":
                return R.drawable.rain;
            case "10n":
                return R.drawable.rain;
            case "11d":
                return R.drawable.thunderstorm;
            case "11n":
                return R.drawable.thunderstorm;
            case "13d":
                return R.drawable.snows;
            case "13n":
                return R.drawable.snows;
            case "50d" :return R.drawable.mist_d;
            case "50n" :return R.drawable.mist_n;
        }
        return -1;
    }

    public static int getBackgroundColor(int indexCity, Context context) {
        Forecast forecast = ForecastDAO.get(context).getForecasts().get(indexCity);
        String icon = forecast.getList().get(0).getWeatherDescription().get(0).getIcon();
        return getColor(icon, context);
    }

    public static int getColor(String icon, Context context) {
        int[] background = context.getResources().getIntArray(R.array.backgroung_color);
        switch (icon) {
            case "01d":
                return background[0];
            case "01n":
                return background[1];
            case "02d":
                return background[2];
            case "02n":
                return background[2];
            case "03d":
                return background[4];
            case "03n":
                return background[4];
            case "04d":
                return background[4];
            case "04n":
                return background[4];
            case "09d":
                return background[5];
            case "09n":
                return background[5];
            case "10d":
                return background[5];
            case "10n":
                return background[5];
            case "11d":
                return background[6];
            case "11n":
                return background[6];
            case "13d":
                return background[7];
            case "13n":
                return background[7];
            case "50d":
                return background[8];
            case "50n":
                return background[8];
        }
        return -1;
    }

    public String getDescription(int index) {
        return forecast.getList().get(index).getWeatherDescription().get(0).getDescription();
    }
    public String getTemp(int index) {
        return (String.valueOf((int) (forecast.getList().get(index).getMain().getTemp()-273)) + "\u00B0");
    }

    public String getWeatherHumidity(int index) {
        Log.e(TAG, "getWeatherHumidity: "  + String.valueOf(forecast.getList().get(index).getMain().getHumidity())+"%");
        return String.valueOf(forecast.getList().get(index).getMain().getHumidity())+"%";
    }

    public String getWeatherPressure(int index) {
        double pressure = forecast.getList().get(index).getMain().getPressure();
        pressure *= 0.750062;

        return String.valueOf((int) pressure)+"мм";
    }
    public String getWeatherWind(int index) {
        double speed = forecast.getList().get(index).getWind().getSpeed();
        return String.valueOf((int) speed)+"м/с";
    }
    public String getWeatherClouds(int index) {
        int clouds = forecast.getList().get(index).getClouds().getAll();
        return String.valueOf(clouds)+"%";
    }



}
