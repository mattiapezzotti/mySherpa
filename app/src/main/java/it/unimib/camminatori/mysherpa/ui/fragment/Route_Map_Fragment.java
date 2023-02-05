package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import org.osmdroid.util.GeoPoint;

import java.util.Objects;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.model.map.RouteMap;
import it.unimib.camminatori.mysherpa.viewmodel.Location_ViewModel;

// Per vedere la logica dietro la Mappa, vedere la classe MapWrapper
public class Route_Map_Fragment extends Fragment {
    private RouteMap routeMap;
    private Location_ViewModel location_viewModel;
    private boolean start = true;
    private boolean pathStart = false;
    private final String startOnMyPostionString = "myPosition";

    public Route_Map_Fragment() {
        super(R.layout.fragment_route_map);
    }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_route_map, container, false);
        routeMap = new RouteMap(rootView.findViewById(R.id.mappa_naviga));

        final Observer<Location> updateLocation = l -> {
            if (l != null) {

                GeoPoint p = new GeoPoint(
                        Double.parseDouble(l.getLat()), Double.parseDouble(l.getLon())
                );

                if (pathStart)
                    if (start) {
                        routeMap.updateStartNavigationPath(p, l.getDisplayName());
                        start = false;
                    } else {
                        try {
                            routeMap.updateDestinationNavigationPath(p, l.getDisplayName());
                        } catch (Exception e) {
                            this.printError(e.getMessage());
                        }
                        start = true;
                        pathStart = false;
                    }
            } else {
                Snackbar.make(this.getView(), "Qualcosa è andato storto. Riprova.", Snackbar.LENGTH_SHORT)
                        .show();
            }
        };
        location_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocation);
        return rootView;
    }

    public void resetCenter() {
        routeMap.resetCenter();
    }

    public void findPathTextOnly(String startText, String endText) {
        start = true;
        pathStart = true;

        if (!Objects.equals(startText, startOnMyPostionString))
            location_viewModel.geocodePlace(startText);
        else {
            routeMap.updateStartNavigationPath(routeMap.getMyLocationOverlay().getMyLocation(), "My Position");
            start = false;
        }

        if (!Objects.equals(endText, startOnMyPostionString))
            if (!Objects.equals(startText, startOnMyPostionString))
                (new Handler()).postDelayed(()
                        -> location_viewModel.geocodePlace(endText), 500
                );
            else
                location_viewModel.geocodePlace(endText);
        else if (!Objects.equals(startText, startOnMyPostionString))
            (new Handler()).postDelayed(()
                            -> {
                        try {
                            routeMap.updateDestinationNavigationPath(routeMap.getMyLocationOverlay().getMyLocation(), "My Position");
                        } catch (Exception e) {
                            this.printError(e.getMessage());
                        }
                    }, 500
            );
        else {
            try {
                routeMap.updateDestinationNavigationPath(routeMap.getMyLocationOverlay().getMyLocation(), "My Position");
            } catch (Exception e) {
                this.printError(e.getMessage());
            }
        }

    }

    public void findPathWithNode(GeoPoint endNode, String endText) {
        routeMap.updateStartNavigationPath(routeMap.getMyLocationOverlay().getMyLocation(), "My Position");
        try {
            routeMap.updateDestinationNavigationPath(endNode, endText);
        } catch (Exception e) {
            this.printError(e.getMessage());
        }
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

    public String getPathLength() {
        return routeMap.getPathLength();
    }

    public String getPathTime() {
        return routeMap.getPathTime();
    }

    public void invertPath(String startText, String endText) {
        try {
            this.routeMap.invertPath(startText, endText);
        } catch (Exception e) {
            this.printError(e.getMessage());
        }
    }

    public void deletePath() {
        this.routeMap.deletePath();
    }

    public void printError(String errorMessage) {
        Snackbar.make(this.getView(), errorMessage, Snackbar.LENGTH_SHORT).show();
    }
}