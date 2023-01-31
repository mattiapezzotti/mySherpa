package it.unimib.camminatori.mysherpa.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Prova.class}, version = 1)
public abstract class Località_Database extends RoomDatabase {

    private static Località_Database instance;

    public abstract Dao_Interface Dao();

    public static synchronized Località_Database getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), Località_Database.class,
                    "database_prova").fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){

        /**
         * Viene eseguito ogni volta che il database viene creato
         */
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }

        /**
         * Viene eseguito ogni volta che il database viene aperto
         */
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private Dao_Interface dao_interface;

        private PopulateDbAsyncTask(Località_Database db){
            dao_interface =db.Dao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao_interface.insert(new Prova("lodi","alta", "molto", "poca"));
            dao_interface.insert(new Prova("milano","alta", "molto", "poca"));
            dao_interface.insert(new Prova("codogno","alta", "molto", "poca"));
            return null;
        }
    }

}
