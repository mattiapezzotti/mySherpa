package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.util.GeoPoint;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.map.ExploreMap;
import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.viewmodel.Location_ViewModel;

// Per vedere la logica dietro la Mappa, vedere la classe ExploreMap
public class Explore_Map_Fragment extends Fragment{
    private ExploreMap exploreMap;
    private Location_ViewModel location_viewModel;
    private FloatingActionButton myLocationFAB; // Tasto per centrare sulla posizione attuale

    public Explore_Map_Fragment() {
        super(R.layout.fragment_explore_map);
    }

    public Explore_Map_Fragment newInstance() {
        return new Explore_Map_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location_viewModel = new ViewModelProvider(requireParentFragment()).get(Location_ViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observer che centra la mappa sulla posizione cercata o inserita tramite long press
        final Observer<Location> updateLocation = l -> {
            if(l.getLon() != null && l.getLat() != null) {
                exploreMap.setCenter(
                        new GeoPoint(
                                Double.parseDouble(l.getLat()), Double.parseDouble(l.getLon())
                        )
                );
            }
        };

        location_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocation);
        exploreMap.resetCenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_explore_map, container, false);
        exploreMap = new ExploreMap(rootView.findViewById(R.id.mappa_esplora));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.exploreMap.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.exploreMap.pause();
    }

    public void resetCenter() {
        this.exploreMap.resetCenter();
    }
}