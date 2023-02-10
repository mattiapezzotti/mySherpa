package it.unimib.camminatori.mysherpa.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SavedRecordDAO {
    @Query("SELECT * FROM savedRecord")
    List<SavedRecord> getAll();

    @Query("SELECT * FROM savedRecord WHERE recordID LIKE :recordIDs")
    SavedRecord findById(int recordIDs);

    @Query("SELECT * FROM savedRecord WHERE displayName LIKE :name LIMIT 1")
    SavedRecord findByName(String name);

    @Query("SELECT * FROM savedRecord WHERE displayName LIKE :name")
    List<SavedRecord> findMultipleByName(String name);

    @Insert
    void insertAll(SavedRecord... savedRecord);

    @Delete
    void delete(SavedRecord savedRecord);

    @Query("DELETE FROM savedRecord")
    void nukeTable();
}
