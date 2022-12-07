package it.unimib.camminatori.mysherpa.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.camminatori.mysherpa.network.geocoding.GeocodingAPI;
import it.unimib.camminatori.mysherpa.model.Location;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationRepository {
    private static LocationRepository instance = null;

    public MutableLiveData<Location> getLocationResponse() {
        return locationResponse;
    }

    private final MutableLiveData<Location> locationResponse;

    private LocationRepository(){
        locationResponse = new MutableLiveData<>();
    }

    public static LocationRepository getInstance(){
        if(instance == null)
            instance = new LocationRepository();
        return instance;
    }

        public void reverseGeocoding(double lat, double lon){
            Call<Location> location = GeocodingAPI.getInstance().getApi_interface().doReverseGeocoding(lat, lon);
            location.enqueue(new Callback<Location>() {
                @Override
                public void onResponse(Call<Location> call, Response<Location> response) {
                    if(response.isSuccessful()) {
                        locationResponse.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Location> call, Throwable t) {
                    Log.e("ERROR", t.toString());
                }
            });
        }

        public void searchGeocoding(String text){
            Call<List<Location>> location = GeocodingAPI.getInstance().getApi_interface().doGeocodingSearch(text);
            location.enqueue(new Callback<List<Location>>() {
                @Override
                public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                    if(response.isSuccessful()) {
                        locationResponse.postValue(response.body().get(0));
                    }
                    else
                        Log.e("response", response.errorBody().toString());
                }

                @Override
                public void onFailure(Call<List<Location>> call, Throwable t) {
                    Log.e("ERROR", t.toString());
                }
            });
        }

        public void fowardGeocoding(String street){
            Call<List<Location>> location = GeocodingAPI.getInstance().getApi_interface().doForwardGeocoding(street);
            location.enqueue(new Callback<List<Location>>() {
                @Override
                public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                    if(response.isSuccessful()) {
                        locationResponse.postValue(response.body().get(0));
                    }
                }

                @Override
                public void onFailure(Call<List<Location>> call, Throwable t) {
                    Log.e("ERROR", t.toString());
                }
            });
        }
}
