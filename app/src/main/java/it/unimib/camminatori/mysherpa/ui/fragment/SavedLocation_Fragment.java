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
import it.unimib.camminatori.mysherpa.ui.recyclerview.FavRecordsRecyclerViewAdapter;
import it.unimib.camminatori.mysherpa.viewmodel.Data_Location_ViewModel;


public class SavedLocation_Fragment extends Fragment {
    final private static String TAG = "SavedLocationFragment";
    final private static String FAV_LOCATION_FILENAME = "FavLocation.json";
    protected RecyclerView favLocalityView;
    private TextView noLocationTextView;

    Data_Location_ViewModel dataLocationViewModel;

    public SavedLocation_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
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

    // Sovrascrivi la lista delle località
    public void setFavData(ArrayList<SavedLocation> savedLocationList)
    {
        getDataLocationViewModel().setFavList(savedLocationList);
    }

    // Aggiungi una località
    public static ArrayList<SavedLocation> addLocation(Context context, String localityName, double latitude, double longitude) {
        Data_Location_ViewModel viewModel = new Data_Location_ViewModel(loadFromJson(context));
        viewModel.addRecord(localityName, latitude, longitude);
        saveToJson(context, viewModel.getFavList());

        return viewModel.getFavList();
    }

    // Rimuovi una località tramite posizione
    public ArrayList<SavedLocation> removeLocation(int position) {
        ArrayList<SavedLocation> favList = getDataLocationViewModel().removeRecord(position);
        saveToJson(this.getContext(), favList);

        return favList;
    }

    // Rimuovi una località tramite oggetto
    public ArrayList<SavedLocation> removeLocationFromList(ArrayList<SavedLocation> favList, SavedLocation location) {
        if (location != null)
            favList.remove(location);

        saveToJson(this.getContext(), favList);

        return favList;
    }

    // Leggi una località
    public SavedLocation getLocation(int position) {
        return getDataLocationViewModel().getRecord(position);
    }

    // Apri una località sulla mappa
    public void openLocation(int position) {
        SavedLocation favList = getDataLocationViewModel().getRecord(position);
        transition(favList);
    }

    private static ArrayList<SavedLocation> loadFromJson(Context context) {
        // Leggi il contenuto da disco
        String jsonString = readFromFile(context);

        if (jsonString == null)
            return new ArrayList<>();
        else
            // Converti il stringa json in array list
            return new Gson().fromJson(jsonString, new TypeToken<ArrayList<SavedLocation>>() {
            }.getType());
    }

    private static void saveToJson(Context context, ArrayList<SavedLocation> favList) {
        String jsonString = new Gson().toJson(favList);
        writeToFile(jsonString, context);
    }

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

    private Data_Location_ViewModel getDataLocationViewModel() {
        if (dataLocationViewModel == null)
            dataLocationViewModel = new Data_Location_ViewModel(loadFromJson(this.getContext()));

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