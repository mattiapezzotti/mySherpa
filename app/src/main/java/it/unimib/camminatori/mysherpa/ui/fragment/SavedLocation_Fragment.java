package it.unimib.camminatori.mysherpa.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.ui.recyclerview.FavLocationRecyclerViewAdapter;
import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.viewmodel.Data_Location_ViewModel;
import it.unimib.camminatori.mysherpa.viewmodel.Record_ViewModel;


public class SavedLocation_Fragment extends Fragment {
    final private static String TAG = "SavedLocationFragment";
    final private static String FAV_LOCATION_FILENAME = "FavLocation.json";
    protected RecyclerView favLocalityView;

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

        favLocalityView = (RecyclerView) v.findViewById(R.id.fav_location_recycler_view);
        LinearLayoutManager favLinearLayout = new LinearLayoutManager(getContext());
        favLocalityView.setLayoutManager(favLinearLayout);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        dataLocationViewModel = GetDataLocationViewModel();

        final
        FavLocationRecyclerViewAdapter favLocationRecyclerViewAdapter = new FavLocationRecyclerViewAdapter(this, dataLocationViewModel.getFavList());

       /* favLocationRecyclerViewAdapter.setOnItemsChangedListener(size -> {

            TextView noLocationsTextView = view.findViewById(R.id.no_location_text_view);

            if (size == 0)
                noLocationsTextView.setVisibility(View.VISIBLE);
            else
                noLocationsTextView.setVisibility(View.GONE);

        }); */

        favLocalityView.setAdapter(favLocationRecyclerViewAdapter);

        EditText favSearch = (EditText) view.findViewById(R.id.fav_text_search);
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

    // Aggiungi una località
    public static ArrayList<Data_Location_ViewModel.SavedLocationInfo> AddLocation(Context context, String localityName, double latitude, double longitude)
    {
        Data_Location_ViewModel viewModel = new Data_Location_ViewModel(LoadFromJson(context));
        viewModel.addRecord(localityName, latitude, longitude);
        SaveToJson(context, viewModel.getFavList());

        return viewModel.getFavList();
    }

    // Rimuovi una località
    public ArrayList<Data_Location_ViewModel.SavedLocationInfo> RemoveLocation(int position)
    {
        ArrayList<Data_Location_ViewModel.SavedLocationInfo> favList = GetDataLocationViewModel().removeRecord(position);
        SaveToJson(this.getContext(), favList);

        return favList;
    }

    // Apri una località sulla mappa
    public void OpenLocation(int position)
    {
        Data_Location_ViewModel.SavedLocationInfo favList = GetDataLocationViewModel().getRecord(position);
        transaction(favList);
    }

    private static ArrayList<Data_Location_ViewModel.SavedLocationInfo> LoadFromJson(Context context)
    {
        // Leggi il contenuto da disco
        String jsonString = readFromFile(context);

        if (jsonString == null)
            return new ArrayList<>();
        else
            // Converti il stringa json in array list
            return new Gson().fromJson(jsonString, new TypeToken<ArrayList<Data_Location_ViewModel.SavedLocationInfo>>() {
            }.getType());
    }

    private static void SaveToJson(Context context, ArrayList<Data_Location_ViewModel.SavedLocationInfo> favList) {
        String jsonString = new Gson().toJson(favList);
        writeToFile(jsonString, context);
    }

    private static void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FAV_LOCATION_FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.d(TAG, "Saved FavLocation file");
        }
        catch (IOException e) {
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
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private Data_Location_ViewModel GetDataLocationViewModel()
    {
        if(dataLocationViewModel == null)
            dataLocationViewModel = new Data_Location_ViewModel(LoadFromJson(this.getContext()));

        return dataLocationViewModel;
    }

    private void transaction(Data_Location_ViewModel.SavedLocationInfo savedLocationInfo){
        Bundle bundle = new Bundle();
        bundle.putDouble("destLon", savedLocationInfo.lon);
        bundle.putDouble("destLat", savedLocationInfo.lat);
        bundle.putString("destText", savedLocationInfo.locationString);
        Navigation.findNavController(this.getActivity().findViewById(R.id.nav_host_fragment)).popBackStack();
        Navigation.findNavController(this.getActivity().findViewById(R.id.nav_host_fragment)).navigate(R.id.fragment_explore, bundle);
    }
}