package it.unimib.camminatori.mysherpa.repository;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

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
            Call<Location> location = GeocodingAPI.getInstance().getApi_interface().doReverseGeocoding(lat, lon);
            location.enqueue(new Callback<Location>() {
                @Override
                public void onResponse(Call<Location> call, Response<Location> response) {
                    if(response.isSuccessful()) {
                        int statusCode = response.code();
                        locationResponse.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Location> call, Throwable t) {
                    // Log error here since request failed
                }
            });
            return locationResponse;
        }

        public MutableLiveData<Location> searchGeocoding(String text){
            Call<Location> location = GeocodingAPI.getInstance().getApi_interface().doGeocodingSearch(text);
            location.enqueue(new Callback<Location>() {
                @Override
                public void onResponse(Call<Location> call, Response<Location> response) {
                    if(response.isSuccessful()) {
                        int statusCode = response.code();
                        locationResponse.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Location> call, Throwable t) {
                    // Log error here since request failed
                }
            });
            return locationResponse;
        }

        public void fowardGeocoding(String street){
            Call<Location> location = GeocodingAPI.getInstance().getApi_interface().doForwardGeocoding(street);
            location.enqueue(new Callback<Location>() {
                @Override
                public void onResponse(Call<Location> call, Response<Location> response) {
                    if(response.isSuccessful()) {
                        int statusCode = response.code();
                        locationResponse.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Location> call, Throwable t) {
                    // Log error here since request failed
                }
            });
        }
    /* ----------- OLD CONTENT -----------------

    public void reverseGeocoding(int lat, int lon){
        geocodingAPI.getApi_interface().doReverseGeocoding(lat, lon);
    }
    public Single<Location> searchGeocoding(String text){
        return geocodingAPI.getApi_interface().doGeocodingSearch(text);
    }

    public void fowardGeocoding(String street){
        geocodingAPI.getApi_interface().doForwardGeocoding(street);
    }
    */
}
