package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Map_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Map_Fragment extends Fragment {

    private MapView map;
    private MyLocationNewOverlay myLocation;

    public Map_Fragment() {
        super(R.layout.fragment_map);
    }

    public Map_Fragment newInstance() {
        return new Map_Fragment();
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

        map = rootView.findViewById(R.id.mappa);
        map.setTileSource(TileSourceFactory.WIKIMEDIA);
        map.setMultiTouchControls(true);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        RotationGestureOverlay rotationController = new RotationGestureOverlay(map);
        rotationController.setEnabled(true);
        map.getOverlays().add(rotationController);

        myLocation = new MyLocationNewOverlay(new GpsMyLocationProvider(requireActivity()), map);
        myLocation.enableMyLocation();
        map.getOverlays().add(myLocation);

        IMapController mapController = this.map.getController();
        mapController.setZoom(17.0);
        map.setMinZoomLevel(6.5);
        //mapController.setCenter(location.getMyLocation());

        GeoPoint provaglio = new GeoPoint(45.6374,10.0430);
        mapController.setCenter(provaglio);

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