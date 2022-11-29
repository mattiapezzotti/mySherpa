package it.unimib.camminatori.mysherpa;

import it.unimib.camminatori.mysherpa.pojo.Location;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Geocoding {

    public static final String BASE_URL = "https://forward-reverse-geocoding.p.rapidapi.com/";
    private API_interface api_interface = null;

    public Geocoding() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        this.api_interface = retrofit.create(API_interface.class);
    }


    public void reverseGeocoding(){

    }

    public void fowardGeocoding(String text){
        Call<Location> location = api_interface.doGeocodingSearch(text);
        location.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                int statusCode = response.code();
                Location location = response.body();

            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                // Log error here since request failed
            }
        });

    }

    public void geocodingSearch(String text){

    }
}