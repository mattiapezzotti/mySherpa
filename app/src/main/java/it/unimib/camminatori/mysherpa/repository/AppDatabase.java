package it.unimib.camminatori.mysherpa.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import it.unimib.camminatori.mysherpa.model.SavedRecord;
import it.unimib.camminatori.mysherpa.model.SavedRecordDAO;
import it.unimib.camminatori.mysherpa.utils.Converters;

@Database(entities = {SavedRecord.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract SavedRecordDAO savedRecordDAO();
}
