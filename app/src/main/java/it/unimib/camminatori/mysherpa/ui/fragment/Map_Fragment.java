package it.unimib.camminatori.mysherpa.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.GroundOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
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
public class Map_Fragment extends Fragment implements MapEventsReceiver {

    private MapView map;
    private MyLocationNewOverlay myLocationOverlay;
    private Explore_ViewModel explore_viewModel;
    private IMapController mapController;
    private RotationGestureOverlay rotationController;
    private MapEventsOverlay mapEventsOverlay;
    private Bitmap myPositionIcon;
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
                mapController.setCenter(
                        new GeoPoint(
                                Double.parseDouble(l.getLat()), Double.parseDouble(l.getLon())
                        )
                );
            }
        };

        explore_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocation);
        mapController.setCenter(myLocationOverlay.getMyLocation());
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

        myLocationOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(requireActivity()), map);
        myLocationOverlay.enableFollowLocation();

        //myLocationOverlay.setPersonIcon(myPositionIcon);
        //myLocationOverlay.setDirectionIcon(myPositionIcon);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.setDrawAccuracyEnabled(true);

        map.getOverlays().add(myLocationOverlay);

        mapController = this.map.getController();
        mapController.setZoom(17.0);
        map.setMinZoomLevel(6.5);

        getMyLocationFAB = rootView.findViewById(R.id.fab_getMyLocation);
        getMyLocationFAB.setOnClickListener(v -> {mapController.setCenter(myLocationOverlay.getMyLocation());});

        mapEventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(0, mapEventsOverlay);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.map.onResume();
        this.myLocationOverlay.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.map.onPause();
        this.myLocationOverlay.disableMyLocation();
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        InfoWindow.closeAllInfoWindowsOn(map);
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        GroundOverlay myGroundOverlay = new GroundOverlay();

        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_background, null);
        //myGroundOverlay.setImage(d.mutate());
        BoundingBox bb = new BoundingBox(p.getLatitude()+map.getLatitudeSpanDouble()/2,
                p.getLongitude()+map.getLongitudeSpanDouble()/2,
                p.getLatitude(),
                p.getLongitude());
        map.getOverlays().add(myGroundOverlay);

        Marker ne = new Marker(map);
        ne.setPosition(new GeoPoint(bb.getLatNorth(), bb.getLonEast()));
        ne.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(ne);

        Marker sw = new Marker(map);
        sw.setPosition(new GeoPoint(bb.getLatSouth(), bb.getLonWest()));
        sw.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(sw);
        return false;
    }
}