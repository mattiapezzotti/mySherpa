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
import it.unimib.camminatori.mysherpa.model.MapWrapper;
import it.unimib.camminatori.mysherpa.pojo.Location;
import it.unimib.camminatori.mysherpa.viewmodel.Explore_ViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Map_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Map_Fragment extends Fragment{
    private MapWrapper mapWrapper;
    private Explore_ViewModel explore_viewModel;
    private FloatingActionButton getMyLocationFAB;

    public Map_Fragment() {
        super(R.layout.fragment_map);
    }

    public Map_Fragment newInstance() {
        return new Map_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        explore_viewModel = new ViewModelProvider(requireParentFragment()).get(Explore_ViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Observer<Location> updateLocation = l -> {
            if(l.getLon() != null && l.getLat() != null) {
                System.out.println(l);
                mapWrapper.getMapController().setCenter(
                        new GeoPoint(
                                Double.parseDouble(l.getLat()), Double.parseDouble(l.getLon())
                        )
                );
            }
        };

        explore_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocation);
        mapWrapper.resetCenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapWrapper = new MapWrapper(rootView.findViewById(R.id.mappa));

        getMyLocationFAB = rootView.findViewById(R.id.fab_getMyLocation);
        getMyLocationFAB.setOnClickListener(v -> {
            mapWrapper.resetCenter();
            mapWrapper.updateLabelLocation(null);
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mapWrapper.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mapWrapper.pause();
    }
}