package it.unimib.camminatori.mysherpa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.camminatori.mysherpa.pojo.Location;
import it.unimib.camminatori.mysherpa.repository.LocationRepository;

public class Explore_ViewModel extends ViewModel {
    //private static Explore_ViewModel instance = null;
    private MutableLiveData<Location> geocodedLocation = new MutableLiveData<>();

    public Explore_ViewModel(){

    }

    /*
    public static Explore_ViewModel getInstance(){
        if(instance == null)
            instance = new Explore_ViewModel();
        return instance;
    }
     */

    public LiveData<Location> getGeocodedLocation(){
        return geocodedLocation;

    }

    public void geocodePlace(String text){
        geocodedLocation = LocationRepository.getInstance().searchGeocoding(text);
    }





}