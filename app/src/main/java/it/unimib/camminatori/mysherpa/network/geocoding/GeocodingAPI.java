package it.unimib.camminatori.mysherpa.network.geocoding;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Istanza retrofit e i vari adapter di retrofit
 */
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
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
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
}