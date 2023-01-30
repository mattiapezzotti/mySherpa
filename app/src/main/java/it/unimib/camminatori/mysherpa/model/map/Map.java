package it.unimib.camminatori.mysherpa.model.map;

import static org.osmdroid.views.overlay.IconOverlay.ANCHOR_CENTER;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import it.unimib.camminatori.mysherpa.BuildConfig;
import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.repository.LocationRepository;
import it.unimib.camminatori.mysherpa.utils.ImageUtils;

public class Map implements MapEventsReceiver {

    protected final MapView mapView;
    protected final MyLocationNewOverlay myLocationOverlay;
    protected final IMapController mapController;
    protected final RotationGestureOverlay rotationController;
    protected final MapEventsOverlay mapEventsOverlay;
    protected Drawable userIcon;

    public Map(MapView map){
        this.mapView = map;
        this.mapController = this.mapView.getController();
        this.rotationController = new RotationGestureOverlay(this.mapView);
        this.myLocationOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(requireActivity()), map);
        this.mapEventsOverlay = new MapEventsOverlay(this);

        this.mapView.setTileSource(TileSourceFactory.WIKIMEDIA);
        this.mapView.setMultiTouchControls(true);
        this.mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        this.rotationController.setEnabled(true);
        this.mapView.getOverlays().add(rotationController);

        this.myLocationOverlay.enableMyLocation();
        this.myLocationOverlay.setDrawAccuracyEnabled(true);
        this.mapController.setZoom(17.0);
        this.mapView.setMinZoomLevel(6.5);

        this.myLocationOverlay.enableFollowLocation();
        this.myLocationOverlay.setPersonAnchor(ANCHOR_CENTER, ANCHOR_CENTER);

        userIcon = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_circle_24_userposition);

        this.myLocationOverlay.setDirectionIcon(ImageUtils.drawableToBitmap(userIcon));
        this.mapView.getOverlays().add(0, mapEventsOverlay);
        this.mapView.getOverlays().add(myLocationOverlay);
    }

    protected Context requireActivity() {
        return getMapView().getContext();
    }

    public MyLocationNewOverlay getMyLocationOverlay() {
        return myLocationOverlay;
    }

    public MapView getMapView() {
        return mapView;
    }

    public IMapController getMapController() {
        return mapController;
    }

    public RotationGestureOverlay getRotationController() {
        return rotationController;
    }

    public void setCenter(GeoPoint point){
        mapController.setCenter(point);
    }

    public void resume(){
        this.mapView.onResume();
        this.myLocationOverlay.enableMyLocation();
        this.mapEventsOverlay.onResume();
    }

    public void pause(){
        this.mapView.onPause();
        this.myLocationOverlay.disableMyLocation();
        this.mapEventsOverlay.onPause();
    }

    public void resetCenter(){
        GeoPoint myLocation = myLocationOverlay.getMyLocation();
        mapController.setCenter(myLocation);
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }
}