package it.unimib.camminatori.mysherpa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.osmdroid.util.GeoPoint;

import it.unimib.camminatori.mysherpa.network.geocoding.GeocodingAPI;
import it.unimib.camminatori.mysherpa.pojo.Location;

public class Navigation_ViewModel extends ViewModel {
    private GeocodingAPI geocodingAPI = GeocodingAPI.getInstance();
    private MutableLiveData<Location> myLocation = null;

    public LiveData<Location> getLocation(){
        if(myLocation == null) {
            myLocation = new MutableLiveData<>();
            fetchLocation();
        }
        return myLocation;
    }

    public void fetchLocation(){
        geocodingAPI.getApi_interface().doGeocodingSearch("Iseo");
    }



}