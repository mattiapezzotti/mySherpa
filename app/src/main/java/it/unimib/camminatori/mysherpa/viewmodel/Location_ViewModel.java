package it.unimib.camminatori.mysherpa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.repository.LocationRepository;

public class Location_ViewModel extends ViewModel {

    public Location_ViewModel(){
    }

    public LiveData<Location> getGeocodedLocation(){
        return LocationRepository.getInstance().getLocationResponse();
    }

    public void geocodePlace(String text){
        LocationRepository.getInstance().searchGeocoding(text);
    }

    public void reverseGeocode(double lat, double lon){
        LocationRepository.getInstance().reverseGeocoding(lat, lon);
    }

}