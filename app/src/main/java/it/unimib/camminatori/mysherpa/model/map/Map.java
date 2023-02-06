package it.unimib.camminatori.mysherpa.model.map;

import static org.osmdroid.views.overlay.IconOverlay.ANCHOR_CENTER;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.utils.ImageUtils;
import mil.nga.geopackage.BuildConfig;

public class Map implements MapEventsReceiver {

    protected final MapView mapView;
    protected final MyLocationNewOverlay myLocationOverlay;
    protected final IMapController mapController;
    protected final RotationGestureOverlay rotationController;
    protected final MapEventsOverlay mapEventsOverlay;

    protected Drawable userIcon;
    protected Drawable red_Marker;
    protected Drawable blue_Marker;
    protected Drawable black_Marker;
    protected Drawable upward_arrow;

    protected RoadManager roadManager;

    public Map(MapView map) {
        this.mapView = map;
        this.mapController = this.mapView.getController();
        this.rotationController = new RotationGestureOverlay(this.mapView);
        this.myLocationOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(requireActivity()), map);
        this.mapEventsOverlay = new MapEventsOverlay(this);

        this.mapView.setTileSource(TileSourceFactory.MAPNIK);
        this.mapView.setMultiTouchControls(true);
        this.mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        this.rotationController.setEnabled(true);
        this.mapView.getOverlays().add(rotationController);

        this.myLocationOverlay.enableMyLocation();
        this.myLocationOverlay.setDrawAccuracyEnabled(false);
        this.mapController.setZoom(17.0);
        this.mapView.setMinZoomLevel(6.5);

        this.myLocationOverlay.enableFollowLocation();

        userIcon = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_circle_24_userposition);
        red_Marker = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_circle_24_marker_red);
        blue_Marker = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_circle_24_marker_blue);
        black_Marker = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_circle_24_marker_black);
        upward_arrow = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_arrow_upward_24);

        this.roadManager = new OSRMRoadManager(mapView.getContext(), BuildConfig.APPLICATION_ID);
        ((OSRMRoadManager) roadManager).setMean(OSRMRoadManager.MEAN_BY_FOOT);

        this.myLocationOverlay.setDirectionIcon(ImageUtils.drawableToBitmap(userIcon));
        this.myLocationOverlay.setPersonAnchor(ANCHOR_CENTER, ANCHOR_CENTER);
        this.mapView.getOverlays().add(0, mapEventsOverlay);
        this.mapView.getOverlays().add(myLocationOverlay);

        this.mapView.invalidate();
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

    public void setCenter(GeoPoint point) {
        this.myLocationOverlay.disableFollowLocation();
        this.mapController.setCenter(point);
    }

    public void resume() {
        this.mapEventsOverlay.onResume();
        this.mapView.onResume();
        this.myLocationOverlay.enableMyLocation();
    }

    public void pause() {
        this.mapEventsOverlay.onPause();
        this.mapView.onPause();
        this.myLocationOverlay.disableMyLocation();
    }

    public void resetCenter() {
        mapController.setCenter(myLocationOverlay.getMyLocation());
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