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
     * Il metodo restituisce l'ArrayList che contiene al suo interno le località aggiunte ai
     * preferiti.
     */
    public ArrayList<SavedLocation> getFavList() {
        return favList;
    }

    /**
     * Il metodo aggiunge all'ArrayList favList i seguenti elementi quando viene premuto il bottone
     * SAVE nella DRAG HANDLERT, presente nell'Explore_Card_Fragment.
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
     * Il metodo restituisce l'elemento dell'ArrayList (favList) che si trova nella posizione
     * indicata dal parametro  (int) position.
     *
     * @param position
     **/
    public SavedLocation getFavLocationFromList(int position) {
        return favList.get(position);
    }

}
