package it.unimib.camminatori.mysherpa;

import static org.osmdroid.views.overlay.IconOverlay.ANCHOR_CENTER;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import org.osmdroid.api.IMapController;
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

import it.unimib.camminatori.mysherpa.utils.ImageUtils;
import it.unimib.camminatori.mysherpa.repository.LocationRepository;

public class MapWrapper implements MapEventsReceiver {
    private final MapView mapView;
    private final MyLocationNewOverlay myLocationOverlay;
    private final IMapController mapController;
    private final RotationGestureOverlay rotationController;
    private final MapEventsOverlay mapEventsOverlay;
    private final Marker marker;

    public MapWrapper(MapView map){
        this.mapView = map;
        this.mapController = this.mapView.getController();
        this.rotationController = new RotationGestureOverlay(this.mapView);
        this.myLocationOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(requireActivity()), map);
        this.mapEventsOverlay = new MapEventsOverlay(this);
        this.marker = new Marker(mapView);
        init();
    }

    private Context requireActivity() {
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


    private void init(){
        this.mapView.setTileSource(TileSourceFactory.WIKIMEDIA);
        this.mapView.setMultiTouchControls(true);
        this.mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        this.rotationController.setEnabled(true);
        this.mapView.getOverlays().add(rotationController);

        this.myLocationOverlay.enableMyLocation();
        this.myLocationOverlay.setDrawAccuracyEnabled(false);
        this.mapController.setZoom(17.0);
        this.mapView.setMinZoomLevel(6.5);

        this.myLocationOverlay.enableFollowLocation();
        this.marker.setInfoWindow(null);

        //TODO: fix size
        Drawable userIcon = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_circle_24_userposition);
        Drawable markerIcon = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_circle_24_marker_red);

        this.myLocationOverlay.setDirectionIcon(ImageUtils.drawableToBitmap(userIcon));
        this.myLocationOverlay.setPersonAnchor(ANCHOR_CENTER, ANCHOR_CENTER);
        this.marker.setIcon(markerIcon);

        this.mapView.getOverlays().add(0, mapEventsOverlay);
        this.mapView.getOverlays().add(myLocationOverlay);
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

    // Un single tap rimuove tutti i marker rossi
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        mapView.getOverlays().remove(marker);
        return true;
    }

    // Un long tap inserisce un marker rosso nel punto premuto, centra la mappa su quello e
    // aggiorna la location in focus in modo da comunicarlo alle altre classi
    @Override
    public boolean longPressHelper(GeoPoint p) {
        mapView.getOverlays().remove(marker);

        marker.setPosition(p);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle("Start point");

        mapView.getOverlays().add(marker);
        mapController.animateTo(marker.getPosition());
        updateLabelLocation(marker.getPosition());
        return true;
    }

    public void resetCenter(){
        GeoPoint myLocation = myLocationOverlay.getMyLocation();
        mapController.setCenter(myLocation);
    }

    public void updateLabelLocation(GeoPoint position){
        if(position == null)
            position = myLocationOverlay.getMyLocation();
        LocationRepository.getInstance().reverseGeocoding(position.getLatitude(), position.getLongitude());
    }
}