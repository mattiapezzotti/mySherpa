package it.unimib.camminatori.mysherpa.network.weather;

import java.util.List;

import it.unimib.camminatori.mysherpa.model.Location;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Definisce gli endpoint HTTP esterni
 */
public interface Weather_API_interface {
    String apikey = "012fb08a52msh67d0ca5257aba78p1c95adjsnc55e706a40e3";
    String apiHost = "weather-by-api-ninjas.p.rapidapi.com";

    @Headers({"X-RapidAPI-Key: " + apikey, "X-RapidAPI-Host: "+ apiHost})
    @GET("v1/weather")
    Call<List<Location>> getCityWeather(@Query("city") String city);

    @Headers({"X-RapidAPI-Key: " + apikey, "X-RapidAPI-Host: "+ apiHost})
    @GET("v1/weather")
    Call<List<Location>> getCoordinatesWeather(@Query("lat") double lat, @Query("lon") double lon);
}
