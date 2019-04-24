package com.example.assignment7;

import com.google.android.gms.maps.model.LatLng;

public class Marker {
    private String locName = "";
    private String locDes = "";
    private String lat ;
    private String lng ;
    public Marker(String locName, String locDes, String lat, String lng){
        this.locDes = locDes;
        this.locName = locName;
        this.lat = lat;
        this.lng = lng;
    }


    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public String getLocDes() {
        return locDes;
    }

    public void setLocDes(String locDes) {
        this.locDes = locDes;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }


}
