package it.unimib.camminatori.mysherpa.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.unimib.camminatori.mysherpa.data.Prova;
import it.unimib.camminatori.mysherpa.repository.DataBaseRepository;

public class Saved_Location_ViewModel extends ViewModel {

    private DataBaseRepository repository;
    private LiveData<List<Prova>> allProva;

    public Saved_Location_ViewModel(@NonNull Application application) {

        repository = new DataBaseRepository(application);
        allProva = repository.getAllProva();
    }

    public void insert(Prova prova){
        repository.insert(prova);
    }

    public void update(Prova prova){
        repository.update(prova);
    }

    public void delete(Prova prova){
        repository.delete(prova);
    }

    public void deleteAllProva(){
        repository.deleteAllProva();
    }

    public LiveData<List<Prova>> getAllProva(){
        return allProva;
    }

}
