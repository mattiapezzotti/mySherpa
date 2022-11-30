package it.unimib.camminatori.mysherpa.network.geocoding;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeocodingAPI {
    private static GeocodingAPI instance = null;
    private static final String BASE_URL = "https://forward-reverse-geocoding.p.rapidapi.com/";
    private final Geocoding_API_interface api_interface;

    private GeocodingAPI() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        this.api_interface = retrofit.create(Geocoding_API_interface.class);
    }

    public static GeocodingAPI getInstance(){
        if(instance == null)
            instance = new GeocodingAPI();
        return instance;
    }

    public Geocoding_API_interface getApi_interface() {
        return api_interface;
    }

    /* ----------- OLD CONTENT -----------------

        public void reverseGeocoding(int lat, int lon){
            Call<Location> location = api_interface.doReverseGeocoding(lat, lon);
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

        public void searchGeocoding(String text){
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

        public void fowardGeocoding(String street){
            Call<Location> location = api_interface.doForwardGeocoding(street);
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
    */
}