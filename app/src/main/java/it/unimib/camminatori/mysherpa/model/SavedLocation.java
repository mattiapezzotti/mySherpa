package it.unimib.camminatori.mysherpa.model;

import com.google.gson.annotations.Expose;

public class SavedLocation {
    @Expose
    private String displayName;
    @Expose
    private double latitude;
    @Expose
    private double longitude;
    @Expose
    private double altitude;

    public SavedLocation() {
    }

    public SavedLocation(String displayName, double latitude, double longitude) {
        this.displayName = displayName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public SavedLocation(double latitude, double longitude, double altitude) {
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return this.altitude;
    }
}
