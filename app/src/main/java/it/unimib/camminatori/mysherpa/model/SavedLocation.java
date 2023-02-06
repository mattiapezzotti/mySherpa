package it.unimib.camminatori.mysherpa.model;

import com.google.gson.annotations.Expose;

public class SavedLocation {
    @Expose
    public String locationString;
    @Expose
    public double lat;
    @Expose
    public double lon;

    public SavedLocation() {
    }

    public SavedLocation(String locationString, double lat, double lon) {
        this.locationString = locationString;
        this.lat = lat;
        this.lon = lon;
    }

}
