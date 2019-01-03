
package com.mydev.android.myweather.cityUA;


public class City {


    private String lat;
    private String lon;
    private String name;
    private String regions;

    public City(String name, String regions, String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.regions = regions;
    }

    public String getRegions() {
        return regions;
    }

    public void setRegions(String regions) {
        this.regions = regions;
    }


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
