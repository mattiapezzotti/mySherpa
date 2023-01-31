package it.unimib.camminatori.mysherpa.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Dao_Interface {

    @Insert
    void insert(Prova dataBase);

    @Update
    void update(Prova dataBase);

    @Delete
    void delete(Prova dataBase);

    @Query("DELETE FROM database_prova")
    void deleteAllDatabase();

    @Query("SELECT * FROM database_prova")
    LiveData<List<Prova>> getAllDatabase();

}
