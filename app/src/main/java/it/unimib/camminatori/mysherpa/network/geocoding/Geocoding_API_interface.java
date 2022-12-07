package it.unimib.camminatori.mysherpa.network.geocoding;

import java.util.List;

import it.unimib.camminatori.mysherpa.model.Location;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Geocoding_API_interface {
    String apikey = "012fb08a52msh67d0ca5257aba78p1c95adjsnc55e706a40e3";
    String apiHost = "forward-reverse-geocoding.p.rapidapi.com";

    @Headers({"X-RapidAPI-Key: " + apikey, "X-RapidAPI-Host: "+ apiHost})
    @GET("v1/search")
    Call<List<Location>> doGeocodingSearch(@Query("q") String q);

    @Headers({"X-RapidAPI-Key: " + apikey, "X-RapidAPI-Host: "+ apiHost})
    @GET("/v1/foward")
    Call<List<Location>> doForwardGeocoding(@Query("street") String street);

    @Headers({"X-RapidAPI-Key: " + apikey, "X-RapidAPI-Host: "+ apiHost})
    @GET("/v1/reverse")
    Call<Location> doReverseGeocoding(@Query("lat") double lat, @Query("lon") double lon);
}
