package it.unimib.camminatori.mysherpa.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.SavedLocation;
import it.unimib.camminatori.mysherpa.ui.recyclerview.FavLocationRecyclerViewAdapter;
import it.unimib.camminatori.mysherpa.viewmodel.FavLocation_ViewModel;


public class SavedLocation_Fragment extends Fragment {
    final private static String TAG = "SavedLocationFragment";
    final private static String FAV_LOCATION_FILENAME = "FavLocation.json";
    protected RecyclerView favLocalityView;
    private TextView noLocationTextView;

    FavLocation_ViewModel dataLocationViewModel;

    public SavedLocation_Fragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_bookmark, container, false);

        favLocalityView = v.findViewById(R.id.fav_location_recycler_view);
        noLocationTextView = v.findViewById(R.id.no_location_text_view);
        LinearLayoutManager favLinearLayout = new LinearLayoutManager(getContext());
        favLocalityView.setLayoutManager(favLinearLayout);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        dataLocationViewModel = getDataLocationViewModel();

        final FavLocationRecyclerViewAdapter favLocationRecyclerViewAdapter = new FavLocationRecyclerViewAdapter(this, dataLocationViewModel.getFavList());
        favLocalityView.setAdapter(favLocationRecyclerViewAdapter);

        favLocationRecyclerViewAdapter.setOnItemsChangedListener(size -> {
            if (size == 0)
                noLocationTextView.setVisibility(View.VISIBLE);
            else
                noLocationTextView.setVisibility(View.GONE);
        });

        EditText favSearch = view.findViewById(R.id.fav_text_search);
        favSearch.addTextChangedListener(new TextWatcher() {
            final private String TAG = "favSearch TextWatcher";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                favLocationRecyclerViewAdapter.filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     *
     * Il metodo {@link #addLocation(Context, String, double, double)} richiama il metodo {@link FavLocation_ViewModel#addFavLocationToList(String, double, double)},
     * successivamente richiama anche il metodo {@link #saveToJson(Context, ArrayList)} che salva l'ArrayList delle località
     * preferite nel fie JSON.
     *
     * @param context
     * @param localityName
     *
     * @return Mi restituisce l'ArrayList aggiornato.
     *
     */
    public static ArrayList<SavedLocation> addLocation(Context context, String localityName, double latitude, double longitude) {
        FavLocation_ViewModel viewModel = new FavLocation_ViewModel(loadFromJson(context));
        viewModel.addFavLocationToList(localityName, latitude, longitude);
        saveToJson(context, viewModel.getFavList());

        return viewModel.getFavList();
    }

    /**
     *
     * Il metodo {@link #removeLocationFromList(ArrayList, SavedLocation)} rimuove la località scelta dalla lista delle località preferite, poi tramite il
     * metodo {@link #saveToJson(Context, ArrayList)} salva la nuova lista nel file JSON.
     *
     * @param favList
     * @param locationToRemove
     *
     * @return Mi ritorna la l'ArrayList senza la località selezionata.
     *
     */
    public ArrayList<SavedLocation> removeLocationFromList(ArrayList<SavedLocation> favList, SavedLocation locationToRemove) {
        if (locationToRemove != null)
            favList.remove(locationToRemove);

        saveToJson(this.getContext(), favList);

        return favList;
    }

    /**
     *
     * Il metodo {@link #getLocation(int)} richiama il metodo
     * {@link FavLocation_ViewModel#getFavLocationFromList(int)}
     *
     * @param position
     * @return Ritorna la località contenuta nell'ArrayList alla posizione data da position.
     *
     */
    public SavedLocation getLocation(int position) {
        return getDataLocationViewModel().getFavLocationFromList(position);
    }

    /**
     * @param position
     */
    public void openLocation(int position) {
        SavedLocation favList = getDataLocationViewModel().getFavLocationFromList(position);
        transition(favList);
    }

    /**
     *
     * Il metodo  {@link #loadFromJson(Context)} richiama il metodo  {@link #readFromFile(Context)} per leggere il contenuto del File JSON,
     * successivamente viene creato un nuovo oggetto ArrayList se il contenuto del File JSON è NULL,
     * oppure viene creato un'oggetto ArrayList popolato dai dati gia contenuti nel File JSON.
     *
     * @param context
     *
     * @return Se il file JSON è null mi crea un nuovo ArrayList,altrimenti , se
     * il file JSON è popolato me lo converte in un oggetto di tipo ArrayList.
     *
     */
    private static ArrayList<SavedLocation> loadFromJson(Context context) {

        //Leggo il contenuto dal File
        String jsonString = readFromFile(context);

        if (jsonString == null)
            return new ArrayList<>();
        else
            // Converte la stringa formato JSON in ArrayList con il metodo fromJson() contenuto
            //  nella libreria Gson.
            return new Gson().fromJson(jsonString, new TypeToken<ArrayList<SavedLocation>>() {
            }.getType());
    }

    /**
     *
     * Il metodo {@link #saveToJson(Context, ArrayList)} converte tramite il metodo {@link Gson#toJson(Object)}
     * il nostro ArrayList in una stringa che sia familiare al JSON, poi richiama il metodo {@link #writeToFile(String, Context)}
     * che inserisce la stringa nel file JSON.
     *
     * @param context
     * @param favList
     */
    private static void saveToJson(Context context, ArrayList<SavedLocation> favList) {
        String jsonString = new Gson().toJson(favList);
        writeToFile(jsonString, context);
    }


    /**
     *Il metodo {@link #writeToFile(String, Context)} crea un'istanza della classe
     * {@link OutputStreamWriter}, che apre uno stream di scrittura verso il File specificato da
     * FAV_LOCATION_FILENAME, tramite il metodo {@link OutputStreamWriter#write(String)} scrive la
     * stringa all'interno del File, infine con il metodo {@link OutputStreamWriter#close()} chiudo
     * lo stream.
     *
     * @param data
     * @param context
     */
    private static void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FAV_LOCATION_FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.d(TAG, "Saved FavLocation file");
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     *
     * TODO: Scrivere documentazione metodo readFromFile
     *
     * @param context
     * @return
     */
    private static String readFromFile(Context context) {

        String ret = null;

        try {
            InputStream inputStream = context.openFileInput(FAV_LOCATION_FILENAME);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private FavLocation_ViewModel getDataLocationViewModel() {
        if (dataLocationViewModel == null)
            dataLocationViewModel = new FavLocation_ViewModel(loadFromJson(this.getContext()));

        return dataLocationViewModel;
    }

    private void transition(SavedLocation savedLocation) {
        Bundle bundle = new Bundle();
        bundle.putDouble("destLon", savedLocation.getLongitude());
        bundle.putDouble("destLat", savedLocation.getLatitude());
        bundle.putString("destText", savedLocation.getDisplayName());
        Navigation.findNavController(this.getActivity().findViewById(R.id.nav_host_fragment)).popBackStack();
        Navigation.findNavController(this.getActivity().findViewById(R.id.nav_host_fragment)).navigate(R.id.fragment_explore, bundle);
    }
}