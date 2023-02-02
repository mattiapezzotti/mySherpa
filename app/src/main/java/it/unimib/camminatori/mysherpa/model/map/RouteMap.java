package it.unimib.camminatori.mysherpa.model.map;

import static java.lang.System.exit;

import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.R;
import mil.nga.geopackage.BuildConfig;

public class RouteMap extends Map {

    private ArrayList<GeoPoint> waypoints = new ArrayList<>();
    private Marker startMarker;
    private Marker endMarker;

    private GeoPoint startPoint;
    private GeoPoint endPoint;
    private boolean inverted;

    private String pathLenght;
    private String pathTime;

    public RouteMap(MapView map){
        super(map);
        inverted = false;
    }

    public void updateStartNavigationPath(GeoPoint startPoint, String startPointText){
        this.deletePath();

        startMarker= new Marker(mapView);

        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapController.animateTo(startPoint);

        startMarker.setIcon(blue_Marker);
        startMarker.setTitle("Partenza: " + startPointText);
        mapView.getOverlays().add(startMarker);

        waypoints.add(startPoint);

        startMarker.setSubDescription(Road.getLengthDurationText(mapView.getContext(), 0, 0));

        this.startPoint = startPoint;
        mapView.invalidate();

    }

    // Definizione punto di arrivo e creazione del tragitto(road) e della polyline sulla mappa
    public void updateDestinationNavigationPath(GeoPoint endPoint, String endPointText) throws Exception{
        endMarker = new Marker(mapView);

        endMarker.setPosition(endPoint);
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        endMarker.setIcon(red_Marker);
        endMarker.setTitle("Destinazione: " + endPointText);

        mapView.getOverlays().add(endMarker);
        mapView.invalidate();

        waypoints.add(endPoint);

        Road road = roadManager.getRoad(waypoints);

        if(road.mStatus == Road.STATUS_TECHNICAL_ISSUE)
            throw new Exception("Strada non trovata");

        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);

        mapView.getOverlays().add(roadOverlay);
        endMarker.setSubDescription(Road.getLengthDurationText(mapView.getContext(), road.mLength, road.mDuration));

        pathTime = ((int) Math.floor((road.mDuration)/3600) + "h " + (int) Math.floor((road.mDuration)%3600/60) + "min");
        pathLenght = (Math.round((road.mLength)*10.0) / 10.0 + "km");

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
            mapView.invalidate();
        }

        this.endPoint = endPoint;
        mapView.invalidate();
    }

    public String getPathLength() {
        return this.pathLenght;
    }

    public String getPathTime(){
        return this.pathTime;
    }

    public void invertPath(String newStartText, String newEndText) throws Exception {
        if(startMarker != null && endMarker != null && startPoint != null && endPoint != null)
            if(!inverted) {
                GeoPoint newStartPoint = endPoint;
                GeoPoint newEndPoint = startPoint;
                updateStartNavigationPath(newStartPoint, newStartText);
                updateDestinationNavigationPath(newEndPoint, newEndText);
                inverted = true;

            }
            else{
                GeoPoint newStartPoint = startPoint;
                GeoPoint newEndPoint = endPoint;
                updateStartNavigationPath(newStartPoint, newEndText);
                updateDestinationNavigationPath(newEndPoint, newStartText);
                inverted = false;
            }
        mapView.invalidate();
    }

    public void deletePath() {
        waypoints.clear();
        mapView.getOverlays().clear();
        mapView.getOverlays().add(myLocationOverlay);
    }
}