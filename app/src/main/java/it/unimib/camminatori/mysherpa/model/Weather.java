package it.unimib.camminatori.mysherpa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("wind_speed")
    @Expose
    private double windSpeed;
    @SerializedName("wind_degrees")
    @Expose
    private int windDegrees;
    @SerializedName("temp")
    @Expose
    private int temp;
    @SerializedName("humidity")
    @Expose
    private int humidity;
    @SerializedName("sunset")
    @Expose
    private int sunset;
    @SerializedName("min_temp")
    @Expose
    private int minTemp;
    @SerializedName("cloud_pct")
    @Expose
    private int cloudPct;
    @SerializedName("feels_like")
    @Expose
    private int feelsLike;
    @SerializedName("sunrise")
    @Expose
    private int sunrise;
    @SerializedName("max_temp")
    @Expose
    private int maxTemp;

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindDegrees() {
        return windDegrees;
    }

    public void setWindDegrees(int windDegrees) {
        this.windDegrees = windDegrees;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getCloudPct() {
        return cloudPct;
    }

    public void setCloudPct(int cloudPct) {
        this.cloudPct = cloudPct;
    }

    public int getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(int feelsLike) {
        this.feelsLike = feelsLike;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }
}
