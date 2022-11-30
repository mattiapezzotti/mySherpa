package it.unimib.camminatori.mysherpa.repository;

import it.unimib.camminatori.mysherpa.network.geocoding.GeocodingAPI;

public class Repository {
    private static Repository instance = null;
    private final GeocodingAPI geocodingAPI = GeocodingAPI.getInstance();

    private Repository(){}

    public static Repository getInstance(){
        if(instance == null)
            instance = new Repository();
        return instance;
    }

    /**
     * Ritorna il luogo geografico date latitudine e longitudine
     * @param lat latitudine
     * @param lon longitune
     */
    public void reverseGeocoding(int lat, int lon){
        geocodingAPI.getApi_interface().doReverseGeocoding(lat, lon);
    }

    /**
     * Effettua una ricerca di un luogo geografico data una stringa di testo
     * @param text
     */
    public void searchGeocoding(String text){
        geocodingAPI.getApi_interface().doGeocodingSearch(text);
    }

    /**
     * Ritorna il luogo geografico dato l'indirizzo del posto
     * @param street indirizzo
     */
    public void fowardGeocoding(String street){
        geocodingAPI.getApi_interface().doForwardGeocoding(street);
    }
}
