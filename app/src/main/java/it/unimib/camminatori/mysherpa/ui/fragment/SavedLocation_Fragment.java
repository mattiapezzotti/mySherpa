package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.FavRecordsRecyclerViewAdapter;
import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.Weather;
import it.unimib.camminatori.mysherpa.viewmodel.Location_ViewModel;
import it.unimib.camminatori.mysherpa.viewmodel.Record_ViewModel;
import it.unimib.camminatori.mysherpa.viewmodel.Weather_ViewModel;

public class SavedLocation_Fragment extends Fragment {

    final private String TAG = "SavedLocationFragment";
    protected RecyclerView favLocationView;

    public SavedLocation_Fragment() {
        // Required empty public constructor
    }

    public static SavedRecords_Fragment newInstance(String param1, String param2, String param3 , String param4 , String param5) {
        return new SavedRecords_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saved_records, container, false);

        favLocationView = (RecyclerView) v.findViewById(R.id.fav_location_recycler_view);
        LinearLayoutManager favLinearLayout = new LinearLayoutManager(getContext());
        favLocationView.setLayoutManager(favLinearLayout);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Location_ViewModel location_viewModel = new ViewModelProvider(requireActivity()).get(Location_ViewModel.class);
        Weather_ViewModel weather_viewModel = new ViewModelProvider(requireActivity()).get(Weather_ViewModel.class);




    }


}
