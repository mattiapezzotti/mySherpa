package it.unimib.camminatori.mysherpa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.camminatori.mysherpa.model.Weather;
import it.unimib.camminatori.mysherpa.network.weather.repository.WeatherRepository;

public class Weather_ViewModel extends ViewModel {

    public Weather_ViewModel(){
    }

    public LiveData<Weather> getWeather(){
        return WeatherRepository.getInstance().getWeatherResponse();
    }

    public void getCityWeather(String text){
        WeatherRepository.getInstance().getWeatherFromText(text);
    }

    public void getCoordinatesWeather(double lat, double lon){
        WeatherRepository.getInstance().getWeatherFromCoordinates(lat, lon);
    }

}