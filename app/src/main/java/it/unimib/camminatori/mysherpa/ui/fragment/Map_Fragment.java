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

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.pojo.Location;
import it.unimib.camminatori.mysherpa.viewmodel.Explore_ViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Map_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Map_Fragment extends Fragment {

    private MapView map;
    private MyLocationNewOverlay myLocation;
    private Explore_ViewModel explore_viewModel;
    private IMapController mapController;
    private RotationGestureOverlay rotationController;

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
                mapController.setCenter(
                        new GeoPoint(
                                Double.parseDouble(l.getLat()), Double.parseDouble(l.getLon())
                        )
                );
            }
        };

        explore_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        map = rootView.findViewById(R.id.mappa);
        map.setTileSource(TileSourceFactory.WIKIMEDIA);
        map.setMultiTouchControls(true);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        rotationController = new RotationGestureOverlay(map);
        rotationController.setEnabled(true);
        map.getOverlays().add(rotationController);

        myLocation = new MyLocationNewOverlay(new GpsMyLocationProvider(requireActivity()), map);
        myLocation.enableMyLocation();
        map.getOverlays().add(myLocation);

        mapController = this.map.getController();
        mapController.setZoom(17.0);
        map.setMinZoomLevel(6.5);
        //mapController.setCenter(location.getMyLocation());

        //GeoPoint provaglio = new GeoPoint(45.6374,10.0430);
        //mapController.setCenter(provaglio);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.map.onResume();
        this.myLocation.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.map.onPause();
        this.myLocation.disableMyLocation();
    }
}