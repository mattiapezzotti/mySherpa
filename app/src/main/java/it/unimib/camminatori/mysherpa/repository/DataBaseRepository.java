package it.unimib.camminatori.mysherpa.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import it.unimib.camminatori.mysherpa.data.Dao_Interface;
import it.unimib.camminatori.mysherpa.data.Località_Database;
import it.unimib.camminatori.mysherpa.data.Prova;

public class DataBaseRepository {

    private Dao_Interface dao_interface;
    private LiveData<List<Prova>> allProva;

    public DataBaseRepository(Application application){
        Località_Database database = Località_Database.getInstance(application);
        dao_interface = database.Dao();
        allProva = dao_interface.getAllDatabase();
    }

    public void insert(Prova prova){
        new InsertProvaAsyncTask(dao_interface).execute(prova);
    }

    public void update(Prova prova){
        new UpdateProvaAsyncTask(dao_interface).execute(prova);
    }

    public void delete(Prova prova){
        new DeleteProvaAsyncTask(dao_interface).execute(prova);
    }

    public void deleteAllProva(){
        new DeleteAllProvaAsyncTask(dao_interface).execute();
    }

    public LiveData<List<Prova>> getAllProva(){
        return allProva;
    }

    private static class InsertProvaAsyncTask extends AsyncTask<Prova, Void, Void>{

        private Dao_Interface dao_interface;

        //Costruttore
        private InsertProvaAsyncTask(Dao_Interface dao_interface){
            this.dao_interface =dao_interface;
        }

        @Override
        protected Void doInBackground(Prova... prova) {
            dao_interface.insert(prova[0]);
        return null;
        }
    }

    private static class UpdateProvaAsyncTask extends AsyncTask<Prova, Void, Void>{

        private Dao_Interface dao_interface;

        //Costruttore
        private UpdateProvaAsyncTask(Dao_Interface dao_interface){
            this.dao_interface =dao_interface;
        }

        @Override
        protected Void doInBackground(Prova... prova) {
            dao_interface.update(prova[0]);
            return null;
        }
    }

    private static class DeleteProvaAsyncTask extends AsyncTask<Prova, Void, Void>{

        private Dao_Interface dao_interface;

        //Costruttore
        private DeleteProvaAsyncTask(Dao_Interface dao_interface){
            this.dao_interface =dao_interface;
        }

        @Override
        protected Void doInBackground(Prova... prova) {
            dao_interface.delete(prova[0]);
            return null;
        }
    }

    private static class DeleteAllProvaAsyncTask extends AsyncTask<Void, Void, Void>{

        private Dao_Interface dao_interface;

        public DeleteAllProvaAsyncTask(Dao_Interface dao_interface) {
            this.dao_interface =dao_interface;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            dao_interface.deleteAllDatabase();
            return null;
        }
    }

}
