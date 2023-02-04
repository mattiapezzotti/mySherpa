package it.unimib.camminatori.mysherpa.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Data_Location_ViewModel extends ViewModel {
    final private String TAG = "Data_Location_ViewModel";

    private ArrayList<Data_Location_ViewModel.SavedLocationInfo> favList;

    public Data_Location_ViewModel(ArrayList<Data_Location_ViewModel.SavedLocationInfo> savedLocationList){
        if (savedLocationList == null)
            savedLocationList = new ArrayList<Data_Location_ViewModel.SavedLocationInfo>();

        favList = savedLocationList;
    }

    public ArrayList<Data_Location_ViewModel.SavedLocationInfo> getFavList() {
        return favList;
    }

    /**
     Aggiunge gli elementi all'arraylist quando viene premuto il bottone save nella drag handle
     **/
    public ArrayList<Data_Location_ViewModel.SavedLocationInfo> addRecord(String localityName, double latitude, double longitude) {
        Data_Location_ViewModel.SavedLocationInfo saveRecordInfo = new Data_Location_ViewModel.SavedLocationInfo();
        saveRecordInfo.locationString = localityName;
        saveRecordInfo.lat = latitude;
        saveRecordInfo.lon = longitude;
        favList.add(saveRecordInfo);

        return favList;
    }

    /**
     Rimuove il record quando viene premuto il bottone elimina dell'interfaccia
     **/
    public ArrayList<Data_Location_ViewModel.SavedLocationInfo> removeRecord(int position) {
        if (position >= 0)
            favList.remove(position);

        return favList;
    }

    public Data_Location_ViewModel.SavedLocationInfo getRecord(int position) {
        return favList.get(position);
    }

    /**
     * Costruttore di savedLocation
     */
    public static class SavedLocationInfo {
        @Expose
        public String locationString;
        @Expose
        public double lat;
        @Expose
        public double lon;

        public SavedLocationInfo(){

        }


        public SavedLocationInfo(String locationString, double lat, double lon){
            this.locationString = locationString;
            this.lat = lat;
            this.lon = lon;
        }

    }
}
