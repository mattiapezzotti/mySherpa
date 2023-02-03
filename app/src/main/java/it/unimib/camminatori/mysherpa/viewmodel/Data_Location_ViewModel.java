package it.unimib.camminatori.mysherpa.viewmodel;

import android.content.Context;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Data_Location_ViewModel extends ViewModel {
    final private String TAG = "Data_Location_ViewModel";

    private LocationManager locationManager;
    private static MutableLiveData<Data_Location_ViewModel.SavedLocationInfo> savedInfo;
    private static ArrayList<Data_Location_ViewModel.SavedLocationInfo> favList;

    public Data_Location_ViewModel(){
        // TODO: leggere da JSon o DB
        favList = new ArrayList<>();
        favList.add(new SavedLocationInfo("lodi", 40,36));
        favList.add(new SavedLocationInfo("milano", 100,120));

       // String json = readFromFile(context);
    }

    public void init(Context context)
    {
        LoadFromJson(context);
    }

    /*
    public MutableLiveData<Data_Location_ViewModel.SavedLocationInfo> getRecordInfo() {
        if (savedInfo == null)
            savedInfo = new MutableLiveData();

        return savedInfo;
    }
    */

    public ArrayList<Data_Location_ViewModel.SavedLocationInfo> getFavList() {
        return favList;
    }

    /**
     Aggiunge gli elementi all'arraylist quando viene premuto il bottone save nella drag handle
     **/
    public void addRecord(Context context, String localityName, double latitude, double longitude) {
        Data_Location_ViewModel.SavedLocationInfo saveRecordInfo = new Data_Location_ViewModel.SavedLocationInfo();
        saveRecordInfo.locationString = localityName;
        saveRecordInfo.lat = latitude;
        saveRecordInfo.longi = longitude;
        favList.add(saveRecordInfo);

        SaveToJson(context);
    }

    /**
     Rimuove il record quando viene premuto il bottone elimina dell'interfaccia
     **/
    public static ArrayList<Data_Location_ViewModel.SavedLocationInfo> removeRecord(Context context, int position) {
        if (position >= 0)
            favList.remove(position);

        SaveToJson(context);

        return favList;
    }

    public static Data_Location_ViewModel.SavedLocationInfo getRecord(int position) {
        return favList.get(position);
    }

    public static class SavedLocationInfo {

        public String locationString;
        public double lat;
        public double longi;

        //Costruttore Vuoto
        public SavedLocationInfo(){

        }

        //Costruttore dell'arraylist popolato
        public SavedLocationInfo(String locationString, double lat, double longi){
            this.locationString = locationString;
            this.lat = lat;
            this.longi = longi;
        }

    }

    private static void LoadFromJson(Context context)
    {
        String jsonString = readFromFile(context);
        //favList = jsonToArrayList(jsonString);
    }

    private static void  SaveToJson(Context context) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(favList);
        writeToFile(jsonString, context);
    }

    private static void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private static String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private static <T> String listToJson(ArrayList favList){

        Gson gson = new Gson();
        String jsonString = gson.toJson(favList);
        return jsonString;
    }

    /** TODO:
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }
    **/

}
