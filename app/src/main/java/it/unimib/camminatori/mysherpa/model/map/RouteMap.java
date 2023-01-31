package it.unimib.camminatori.mysherpa.model.map;

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

    private Drawable red_Marker;
    private Drawable blue_Marker;
    private Drawable black_Marker;
    private Drawable upward_arrow;

    private ArrayList<GeoPoint> waypoints = new ArrayList<>();
    private RoadManager roadManager;
    private Marker startMarker;
    private Marker endMarker;

    private GeoPoint startPoint;
    private GeoPoint endPoint;
    private boolean inverted;

    private String pathLenght;
    private String pathTime;

    public RouteMap(MapView map){
        super(map);

        red_Marker = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_circle_24_marker_red);
        blue_Marker = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_circle_24_marker_blue);
        black_Marker = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_circle_24_marker_black);
        upward_arrow = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_arrow_upward_24);

        inverted = false;

        this.roadManager = new OSRMRoadManager(mapView.getContext(), BuildConfig.APPLICATION_ID);
        ((OSRMRoadManager)roadManager).setMean(OSRMRoadManager.MEAN_BY_FOOT);

    }

    public void updateStartNavigationPath(GeoPoint startPoint, String startPointText){
        waypoints.clear();
        mapView.getOverlays().clear();
        mapView.getOverlays().add(myLocationOverlay);


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
    public void updateDestinationNavigationPath(GeoPoint endPoint, String endPointText){
        endMarker = new Marker(mapView);

        endMarker.setPosition(endPoint);
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        endMarker.setIcon(red_Marker);
        endMarker.setTitle("Destinazione: " + endPointText);

        mapView.getOverlays().add(endMarker);
        mapView.invalidate();

        waypoints.add(endPoint);

        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);

        mapView.getOverlays().add(roadOverlay);
        endMarker.setSubDescription(Road.getLengthDurationText(mapView.getContext(), road.mLength, road.mDuration));

        pathTime = (Math.round((road.mDuration)/3600) + "h " + Math.round((road.mDuration)/60%60) + "min");
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

    public void invertPath() {
        if(startMarker != null && endMarker != null)
            if(!inverted) {
                startMarker.setPosition(endPoint);
                endMarker.setPosition(startPoint);
                inverted = true;

            }
            else{
                startMarker.setPosition(startPoint);
                endMarker.setPosition(endPoint);
                inverted = false;
            }
        mapView.invalidate();
    }
}