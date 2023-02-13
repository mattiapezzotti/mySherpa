package it.unimib.camminatori.mysherpa.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.model.SavedLocation;

public class FavLocation_ViewModel extends ViewModel {
    final private String TAG = "Data_Location_ViewModel";

    private ArrayList<SavedLocation> favList;

    public FavLocation_ViewModel(ArrayList<SavedLocation> savedLocationList) {
        if (savedLocationList == null)
            savedLocationList = new ArrayList<>();

        favList = savedLocationList;
    }

    /**
     * Il metodo {@link #getFavList()} restituisce l'ArrayList che contiene al suo interno le
     * località aggiunte ai preferiti.
     */
    public ArrayList<SavedLocation> getFavList() {
        return favList;
    }

    /**
     * Il metodo {@link #addFavLocationToList(String, double, double)} aggiunge all'ArrayList
     * i seguenti elementi quando viene premuto il bottone SAVE nella DRAG HANDLERT,
     * presente nell'Explore_Card_Fragment.
     *
     * @param localityName
     * @param latitude
     * @param longitude
     **/
    public void addFavLocationToList(String localityName, double latitude, double longitude) {
        SavedLocation saveRecordInfo = new SavedLocation(localityName, latitude, longitude);
        favList.add(saveRecordInfo);
    }

    /**
     * Il metodo {@link #getFavLocationFromList(int)} restituisce l'elemento dell'ArrayList
     * che si trova nella posizione indicata dal parametro position.
     *
     * @param position
     **/
    public SavedLocation getFavLocationFromList(int position) {
        return favList.get(position);
    }

}
