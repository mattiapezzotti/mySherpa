package it.unimib.camminatori.mysherpa.utils;

import com.google.gson.annotations.Expose;

public class SaveLocation {
    @Expose
    private double latitude;
    @Expose
    private double longitude;
    @Expose
    private double altitude;

    public SaveLocation(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
