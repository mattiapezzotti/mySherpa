package it.unimib.camminatori.mysherpa.viewmodel;

import android.content.Context;
import android.location.LocationManager;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class Data_Location_ViewModel extends ViewModel {
    final private String TAG = "Data_Location_ViewModel";

    private Context context;
    private LocationManager locationManager;
    private static MutableLiveData<Data_Location_ViewModel.SavedLocationInfo> savedInfo;
    private static ArrayList<Data_Location_ViewModel.SavedLocationInfo> favList;

    public Data_Location_ViewModel(){
        // TODO: leggere da JSon o DB
        favList = new ArrayList<>();
        favList.add(new SavedLocationInfo("lodi", 40,36));
        favList.add(new SavedLocationInfo("milano", 100,120));
    }

    public MutableLiveData<Data_Location_ViewModel.SavedLocationInfo> getRecordInfo(Context context) {
        if (savedInfo == null)
            savedInfo = new MutableLiveData();

        if (this.context == null)
            this.context = context;

        return savedInfo;
    }

    public ArrayList<Data_Location_ViewModel.SavedLocationInfo> getFavList() {
        return favList;
    }

    /**
     Aggiunge gli elementi all'arraylist quando viene premuto il bottone save nella drag handle
     **/
    public void addRecord(String localityName, double latitude, double longitude) {
        Data_Location_ViewModel.SavedLocationInfo saveRecordInfo = new Data_Location_ViewModel.SavedLocationInfo();
        saveRecordInfo.locationString = localityName;
        saveRecordInfo.lat = latitude;
        saveRecordInfo.longi = longitude;
        favList.add(saveRecordInfo);
    }

    /**
     Rimuove il record quando viene premuto il bottone elimina dell'interfaccia
     **/
    public static ArrayList<Data_Location_ViewModel.SavedLocationInfo> removeRecord(int position) {
        if (position >= 0)
            favList.remove(position);

        return favList;
    }

    public static Data_Location_ViewModel.SavedLocationInfo getRecord(int position) {
        return favList.get(position);
    }

    public static class SavedLocationInfo {

        //Costruttore Vuoto
        public SavedLocationInfo(){

        }

        //Costruttore dell'arraylist popolato
        public SavedLocationInfo(String locationString, double lat, double longi){
            this.locationString = locationString;
            this.lat = lat;
            this.longi = longi;
        }

        public String locationString;
        public double lat;
        public double longi;
    }
}
