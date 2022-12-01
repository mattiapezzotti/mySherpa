package it.unimib.camminatori.mysherpa.repository;

import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.camminatori.mysherpa.network.geocoding.GeocodingAPI;
import it.unimib.camminatori.mysherpa.pojo.Location;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationRepository {
    private static LocationRepository instance = null;
    private final MutableLiveData<Location> locationResponse;

    private LocationRepository(){
        locationResponse = new MutableLiveData<>();
    }

    public static LocationRepository getInstance(){
        if(instance == null)
            instance = new LocationRepository();
        return instance;
    }

        public MutableLiveData<Location> reverseGeocoding(double lat, double lon){
            Call<List<Location>> location = GeocodingAPI.getInstance().getApi_interface().doReverseGeocoding(lat, lon);
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
            return locationResponse;
        }

        public MutableLiveData<Location> searchGeocoding(String text){
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
            return locationResponse;
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
