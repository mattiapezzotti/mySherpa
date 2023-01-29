package it.unimib.camminatori.mysherpa.model.map;

import static org.osmdroid.views.overlay.IconOverlay.ANCHOR_CENTER;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.BuildConfig;
import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.utils.ImageUtils;
import it.unimib.camminatori.mysherpa.repository.LocationRepository;

public class ExploreMap extends Map {

    private final Marker marker;

    private Drawable markerIcon;

    public ExploreMap(MapView map){
        super(map);

        markerIcon = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_circle_24_marker_red);


        this.marker = new Marker(mapView);
        this.marker.setIcon(markerIcon);
        this.marker.setInfoWindow(null);
    }

    // Un single tap rimuove tutti i marker rossi
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        this.mapView.getOverlays().remove(marker);
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

    public void updateLabelLocation(GeoPoint position){
        if(position == null)
            position = myLocationOverlay.getMyLocation();
        LocationRepository.getInstance().reverseGeocoding(position.getLatitude(), position.getLongitude());
    }
}