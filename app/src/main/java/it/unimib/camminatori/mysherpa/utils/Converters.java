package it.unimib.camminatori.mysherpa.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.model.SavedLocation;

public class Converters {
    @TypeConverter
    public static ArrayList<SavedLocation> fromString(String value) {
        Type listType = new TypeToken<ArrayList<SavedLocation>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<SavedLocation> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
