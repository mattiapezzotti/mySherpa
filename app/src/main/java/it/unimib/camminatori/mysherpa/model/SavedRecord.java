package it.unimib.camminatori.mysherpa.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class SavedRecord {

    @PrimaryKey
    @NonNull
    public String recordID;

    @ColumnInfo(name = "date")
    public String dateString;

    @ColumnInfo(name = "displayName")
    public String locationString;

    @ColumnInfo(name = "millisecondsTime")
    public long millisecondsTime;

    @ColumnInfo(name = "metersDistance")
    public long metersDistance;

    @ColumnInfo(name = "path")
    public ArrayList<SavedLocation> path;
}
