package it.unimib.camminatori.mysherpa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.osmdroid.util.GeoPoint;

import it.unimib.camminatori.mysherpa.pojo.Location;
import it.unimib.camminatori.mysherpa.repository.LocationRepository;

public class Explore_ViewModel extends ViewModel {
    private LiveData<Location> geocodedLocation;

    public Explore_ViewModel(){
        geocodedLocation = LocationRepository.getInstance().searchGeocoding("Milano");
    }

    public LiveData<Location> getGeocodedLocation(){
        return geocodedLocation;
    }

    public void geocodePlace(String text){
        geocodedLocation = LocationRepository.getInstance().searchGeocoding(text);
    }

}