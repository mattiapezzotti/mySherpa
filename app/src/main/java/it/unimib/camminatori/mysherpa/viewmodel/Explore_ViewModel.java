package it.unimib.camminatori.mysherpa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import it.unimib.camminatori.mysherpa.model.MapWrapper;
import it.unimib.camminatori.mysherpa.pojo.Location;
import it.unimib.camminatori.mysherpa.repository.LocationRepository;

public class Explore_ViewModel extends ViewModel {
    private MutableLiveData<Location> geocodedLocation;

    public Explore_ViewModel(){
    }

    public LiveData<Location> getGeocodedLocation(){
        if (geocodedLocation == null)
            geocodedLocation = new MutableLiveData<>();
        geocodedLocation = LocationRepository.getInstance().getLocationResponse();
        return geocodedLocation;
    }

    public void geocodePlace(String text){
        LocationRepository.getInstance().searchGeocoding(text);
    }

    public void reverseGeocode(double lat, double lon){
        LocationRepository.getInstance().reverseGeocoding(lat, lon);
    }

}