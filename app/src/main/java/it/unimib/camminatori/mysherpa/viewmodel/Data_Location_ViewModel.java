package it.unimib.camminatori.mysherpa.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.model.SavedLocation;

public class Data_Location_ViewModel extends ViewModel {
    final private String TAG = "Data_Location_ViewModel";

    private ArrayList<SavedLocation> favList;

    public Data_Location_ViewModel(ArrayList<SavedLocation> savedLocationList) {
        if (savedLocationList == null)
            savedLocationList = new ArrayList<>();

        favList = savedLocationList;
    }

    public ArrayList<SavedLocation> getFavList() {
        return favList;
    }

    /**
     * Aggiunge gli elementi all'arraylist quando viene premuto il bottone save nella drag handle
     **/
    public void addRecord(String localityName, double latitude, double longitude) {
        SavedLocation saveRecordInfo = new SavedLocation(localityName, latitude, longitude);
        favList.add(saveRecordInfo);
    }

    /**
     * Rimuove il record quando viene premuto il bottone elimina dell'interfaccia
     **/
    public ArrayList<SavedLocation> removeRecord(int position) {
        if (position >= 0)
            favList.remove(position);

        return favList;
    }

    public SavedLocation getRecord(int position) {
        return favList.get(position);
    }

    /**
     * Costruttore di savedLocation
     */

}
