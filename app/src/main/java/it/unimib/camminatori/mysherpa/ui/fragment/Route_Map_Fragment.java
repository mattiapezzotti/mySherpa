package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.util.GeoPoint;

import java.util.concurrent.atomic.AtomicReference;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.model.map.RouteMap;
import it.unimib.camminatori.mysherpa.viewmodel.Location_ViewModel;

// Per vedere la logica dietro la Mappa, vedere la classe MapWrapper
public class Route_Map_Fragment extends Fragment{
    private RouteMap routeMap;
    private Location_ViewModel location_viewModel;
    private boolean start = true;

    public Route_Map_Fragment() { super(R.layout.fragment_route_map); }

    public Route_Map_Fragment newInstance() {
        return new Route_Map_Fragment();
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
        Location_ViewModel location_viewModel = new Location_ViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_route_map, container, false);
        routeMap = new RouteMap(rootView.findViewById(R.id.mappa_naviga));

        final Observer<Location> updateLocation = l -> {
            if(l.getLon() != null && l.getLat() != null) {

                GeoPoint p = new GeoPoint(
                        Double.parseDouble(l.getLat()), Double.parseDouble(l.getLon())
                );

                if(start) {
                    routeMap.updateStartNavigationPath(p, l.getDisplayName());
                    start = false;
                }
                else {
                    routeMap.updateDestinationNavigationPath(p, l.getDisplayName());
                    start = true;
                }
            }
        };

        location_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocation);

        return rootView;
    }

    public void resetCenter(){
        routeMap.resetCenter();
    }

    public void findPathTextOnly(String startText, String endText){
        start = true;
        location_viewModel.geocodePlace(startText);
        location_viewModel.geocodePlace(endText);
    }

    public void findPathWithNode(GeoPoint endNode, String endText){
        routeMap.updateStartNavigationPath(routeMap.getMyLocationOverlay().getMyLocation(), "My Position");
        routeMap.updateDestinationNavigationPath(endNode, endText);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.routeMap.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.routeMap.pause();
    }
}