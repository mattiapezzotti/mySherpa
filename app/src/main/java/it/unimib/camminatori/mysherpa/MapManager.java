package it.unimib.camminatori.mysherpa;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapManager {
    private final MapView mapView;
    private final MyLocationNewOverlay myLocation;
    private final IMapController mapController;
    private final RotationGestureOverlay rotationController;

    public MapManager(MapView map, MyLocationNewOverlay myLocation){
        this.mapView = map;
        this.myLocation = myLocation;
        this.mapController = this.mapView.getController();
        this.rotationController = new RotationGestureOverlay(this.mapView);
        init();
    }

    private void init(){
        this.mapView.setTileSource(TileSourceFactory.WIKIMEDIA);
        this.mapView.setMultiTouchControls(true);
        this.mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);


        this.rotationController.setEnabled(true);
        this.mapView.getOverlays().add(rotationController);

        this.myLocation.enableMyLocation();
        this.mapView.getOverlays().add(myLocation);

        this.mapController.setZoom(17.0);
        this.mapView.setMinZoomLevel(6.5);
        //mapController.setCenter(location.getMyLocation());

        GeoPoint provaglio = new GeoPoint(45.6374,10.0430);
        mapController.setCenter(provaglio);
    }

    public void setCenter(double lat, double lon){
        mapController.setCenter(new GeoPoint(lat,lon));
    }

    public void resume(){
        this.mapView.onResume();
        this.myLocation.enableMyLocation();
    }

    public void pause(){
        this.mapView.onPause();
        this.myLocation.disableMyLocation();
    }
}
