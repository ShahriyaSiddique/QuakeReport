package com.example.android.quakereport;

public class Earthquake {

    private double magnitue;
    private String place;
    private long date;
    private String url;

    public Earthquake(double magnitue, String place, long date,String url) {
        this.magnitue = magnitue;
        this.place = place;
        this.date = date;
        this.url = url;
    }

    public double getMagnitue() {
        return magnitue;
    }

    public String getPlace() {
        return place;
    }

    public long getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
