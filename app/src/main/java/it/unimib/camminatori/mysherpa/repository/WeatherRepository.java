package it.unimib.camminatori.mysherpa.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.model.Weather;
import it.unimib.camminatori.mysherpa.network.geocoding.GeocodingAPI;
import it.unimib.camminatori.mysherpa.network.weather.WeatherAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository per interfacciarsi con le API esterne definite in GeocodingAPI
 */
public class WeatherRepository {
    private static WeatherRepository instance = null;

    public MutableLiveData<Weather> getWeatherResponse() {
        return weatherResponse;
    }

    /**
     * Dato mutevole aggiornato ogni volta che viene richiamato un metodo della classe
     */
    private final MutableLiveData<Weather> weatherResponse;

    private WeatherRepository(){
        weatherResponse = new MutableLiveData<>();
    }

    public static WeatherRepository getInstance(){
        if(instance == null)
            instance = new WeatherRepository();
        return instance;
    }

    /**
     * Trova il meteo di un luogo data la sua posizione
     * @param lat latitude del luogo
     * @param lon longitudie del luogo
     */
    public void getWeatherFromCoordinates(double lat, double lon){
        Call<Weather> weather = WeatherAPI.getInstance().getApi_interface().doCoordinatesWeather(lat,lon);
        weather.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if(response.isSuccessful()) {
                    weatherResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.e("ERROR", t.toString());
            }
        });
    }

    /**
     * Trova il meteo di un luogo dato il nome generico del luogo (non preciso)
     * @param text nome del luogo
     */
    public void getWeatherFromText(String text){
        Call<Weather> weather = WeatherAPI.getInstance().getApi_interface().doCityWeather(text);
        weather.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if(response.isSuccessful()) {
                    weatherResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.e("ERROR", t.toString());
            }
        });
    }
}
