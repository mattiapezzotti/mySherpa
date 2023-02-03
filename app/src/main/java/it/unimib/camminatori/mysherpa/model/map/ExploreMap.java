package it.unimib.camminatori.mysherpa.model.map;



import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.repository.WeatherRepository;
import it.unimib.camminatori.mysherpa.viewmodel.Location_ViewModel;
import it.unimib.camminatori.mysherpa.viewmodel.Weather_ViewModel;

public class ExploreMap extends Map {

    private final Marker marker;
    private final Location_ViewModel location_viewModel;
    private final Weather_ViewModel weather_viewModel;

    public ExploreMap(MapView map){
        super(map);

        location_viewModel = new Location_ViewModel();
        weather_viewModel = new Weather_ViewModel();

        this.marker = new Marker(mapView);
        this.marker.setIcon(red_Marker);
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
        setMarkerPosition(p);
        updateLabelLocation(p);
        return true;
    }

    public void setMarkerPosition(GeoPoint p){
        mapView.getOverlays().remove(marker);

        marker.setPosition(p);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        mapView.getOverlays().add(marker);
        mapController.animateTo(marker.getPosition());
    }

    public void updateLabelLocation(GeoPoint position){
        if(position == null)
            position = myLocationOverlay.getMyLocation();
        location_viewModel.reverseGeocode(position.getLatitude(), position.getLongitude());
        weather_viewModel.getCoordinatesWeather(position.getLatitude(), position.getLongitude());
    }

    public void drawRoad(ArrayList<GeoPoint> waypoints) throws Exception {
        Road road = roadManager.getRoad(waypoints);

        if(road.mStatus == Road.STATUS_TECHNICAL_ISSUE)
            throw new Exception("Strada non trovata");

        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);

        mapView.getOverlays().add(roadOverlay);

        for (int i=0; i<road.mNodes.size(); i++){
            RoadNode node = road.mNodes.get(i);
            Marker roadMarker= new Marker(mapView);
            roadMarker.setPosition(node.mLocation);
            roadMarker.setIcon(black_Marker);
            roadMarker.setTitle("Step "+ i);
            mapView.getOverlays().add(roadMarker);
            roadMarker.setSnippet(node.mInstructions);
            roadMarker.setSubDescription(Road.getLengthDurationText(mapView.getContext(), node.mLength, node.mDuration));
            roadMarker.setImage(upward_arrow);
        }
        mapView.invalidate();
    }
}