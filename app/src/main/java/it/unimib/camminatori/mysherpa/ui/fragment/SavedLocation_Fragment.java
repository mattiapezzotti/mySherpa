package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.ui.recyclerview.FavLocationRecyclerViewAdapter;
import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.viewmodel.Data_Location_ViewModel;


public class SavedLocation_Fragment extends Fragment {
    final private String TAG = "SavedLocationFragment";
    protected RecyclerView favLocalityView;

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
        Data_Location_ViewModel dataLocationViewModel = new ViewModelProvider(requireActivity()).get(Data_Location_ViewModel.class);

        ArrayList<Data_Location_ViewModel.SavedLocationInfo> favLocations = dataLocationViewModel.getFavList();

        final FavLocationRecyclerViewAdapter favLocationRecyclerViewAdapter = new FavLocationRecyclerViewAdapter(this, favLocations);

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

    public void onItemClick(Data_Location_ViewModel.SavedLocationInfo savedLocationInfo)
    {
        transaction(savedLocationInfo);
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