package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.map.ExploreMap;
import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.viewmodel.Location_ViewModel;
import it.unimib.camminatori.mysherpa.viewmodel.Weather_ViewModel;

// Per vedere la logica dietro la Mappa, vedere la classe ExploreMap
public class Explore_Map_Fragment extends Fragment{
    private ExploreMap exploreMap;
    private Location_ViewModel location_viewModel;
    private Weather_ViewModel weather_viewModel;

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
        weather_viewModel = new ViewModelProvider(requireParentFragment()).get(Weather_ViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observer che centra la mappa sulla posizione cercata o inserita tramite long press
        final Observer<Location> updateLocation = l -> {
            if(l != null) {
                exploreMap.setMarkerPosition(new GeoPoint(
                        Double.parseDouble(l.getLat()), Double.parseDouble(l.getLon())
                        )
                );
            }
            else{
                Snackbar.make(this.getView().getRootView(),"Qualcosa è andato storto. Riprova.", Snackbar.LENGTH_SHORT)
                        .show();
            }
        };

        location_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocation);
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
        this.exploreMap.getMapController().setZoom(17.0);
    }

    public void drawRoad(ArrayList<GeoPoint> waypoints) throws Exception {
        exploreMap.drawRoad(waypoints);
    }
}