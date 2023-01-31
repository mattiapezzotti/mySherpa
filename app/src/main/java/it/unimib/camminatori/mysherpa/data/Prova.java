package it.unimib.camminatori.mysherpa.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "database_prova")
public class Prova {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "location")
    public String location;

    @ColumnInfo(name = "temperature")
    public String temperature;

    @ColumnInfo(name = "temperature")
    public String wind;

    @ColumnInfo(name = "temperature")
    public String umidity;

    public Prova(String location, String temperature, String wind, String umidity) {
        this.location = location;
        this.temperature = temperature;
        this.wind = wind;
        this.umidity = umidity;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getUmidity() {
        return umidity;
    }

    public void setUmidity(String umidity) {
        this.umidity = umidity;
    }
}
