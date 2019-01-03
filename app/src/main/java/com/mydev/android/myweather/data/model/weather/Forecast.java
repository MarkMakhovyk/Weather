
package com.mydev.android.myweather.data.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {

    @SerializedName("cod")
    @Expose
    private String cod;

    @SerializedName("cnt")
    @Expose
    private Integer cnt;
    @SerializedName("list")
    @Expose
    private List<ItemListWeather> list = null;
    @SerializedName("city")
    @Expose
    private City city;



    public List<ItemListWeather> getList() {
        return list;
    }

    public void setList(List<ItemListWeather> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

}
