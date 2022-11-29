package it.unimib.camminatori.mysherpa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapFragment extends Fragment {
    private MapManager mapManager;

    public MapFragment() {
        super(R.layout.fragment_map);
    }

    public MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        MapView map = rootView.findViewById(R.id.map);
        this.mapManager = new MapManager(
                map, new MyLocationNewOverlay(new GpsMyLocationProvider(requireActivity()), map)
        );
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mapManager.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mapManager.pause();
    }
}