package it.unimib.camminatori.mysherpa.network.weather;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Istanza retrofit e i vari adapter di retrofit
 */
public class WeatherAPI {
    private static WeatherAPI instance = null;
    private static final String BASE_URL = "https://weather-by-api-ninjas.p.rapidapi.com/";
    private final Weather_API_interface api_interface;

    private WeatherAPI() {
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

        this.api_interface = retrofit.create(Weather_API_interface.class);
    }

    public static WeatherAPI getInstance(){
        if(instance == null)
            instance = new WeatherAPI();
        return instance;
    }

    public Weather_API_interface getApi_interface() {
        return api_interface;
    }
}