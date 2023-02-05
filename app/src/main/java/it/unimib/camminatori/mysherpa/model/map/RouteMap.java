package it.unimib.camminatori.mysherpa.model.map;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

/**
 * La classe crea e definisce le informazioni principali quali tempo e distanza, di un percorso/instradamento generato a partire da due località (Geopoints).
 */
public class RouteMap extends Map {

    private ArrayList<GeoPoint> waypoints = new ArrayList<>();
    private Marker startMarker;
    private Marker endMarker;

    private GeoPoint startPoint;
    private GeoPoint endPoint;
    private boolean inverted;

    private String pathLenght;
    private String pathTime;

    /**
     * Costruttore della classe RouteMap.
     * @param map
     */
    public RouteMap(MapView map){
        super(map);
        inverted = false;
    }
    /**
     * Il metodo crea e aggiunge alla mappa il marker riferito alla posizione di partenza (startPoint).
     * Definisce inoltre le informazioni del quali tempo, distanza e nome della località rispetto all'instradamento (Road) definito.
     * @param startPoint Rappresenta un Geopoint (della località di partenza), ossia un punto geolocalizzato sulla mappa caratterizzato da longitudine e latitudine.
     * @param startPointText
     */
    public void updateStartNavigationPath(GeoPoint startPoint, String startPointText){
        this.deletePath();

        startMarker = new Marker(mapView);

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

    /**
     * Il metodo crea e aggiunge alla mappa il marker riferito alla posizione di arrivo (endPoint) e specifica le informazioni quali tempo, distanza e nome della località rispetto all'instradamento (Road) definito.
     * Definisce inoltre a partire dalle località (Geopoints) il percorso/instradamento (Road) disegnando sulla mappa attraverso una Polyline il cammino precedentemente trovato.
     * In aggiunta precisa in relazione al percoso/instradamento, i punti intermedi aventi ruolo di indicazioni, specificando anche per essi tempo, distanza e nome di quest'ultimi.
     * @param endPoint Rappresenta un Geopoint (della località di arrivo), ossia un punto geolocalizzato sulla mappa caratterizzato da longitudine e latitudine.
     * @param endPointText
     * @throws Exception Eccezione generata se l'instradamento/percorso non viene trovato e dunque non generato.
     */
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

        if (road.mStatus == Road.STATUS_TECHNICAL_ISSUE)
            throw new Exception("Strada non trovata");

        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);

        mapView.getOverlays().add(roadOverlay);
        endMarker.setSubDescription(Road.getLengthDurationText(mapView.getContext(), road.mLength, road.mDuration));

        pathTime = ((int) Math.floor((road.mDuration) / 3600) + "h " + (int) Math.floor((road.mDuration) % 3600 / 60) + "min");
        pathLenght = (Math.round((road.mLength) * 10.0) / 10.0 + "km");

        for (int i = 0; i < road.mNodes.size(); i++) {
            RoadNode node = road.mNodes.get(i);
            Marker roadMarker = new Marker(mapView);
            roadMarker.setPosition(node.mLocation);
            roadMarker.setIcon(black_Marker);
            roadMarker.setTitle("Step " + i);
            mapView.getOverlays().add(roadMarker);
            roadMarker.setSnippet(node.mInstructions);
            roadMarker.setSubDescription(Road.getLengthDurationText(mapView.getContext(), node.mLength, node.mDuration));
            roadMarker.setImage(upward_arrow);
            mapView.invalidate();
        }

        this.endPoint = endPoint;
        mapView.invalidate();
    }

    /**
     * Il metodo permette di ottenere attraverso una stringa, la distanza tra le due località (in Km) del cammino/instradamento generato.
     * @return Una stringa contente la distanza tra le due località (in Km) del cammino/instrdamento generato.
     */
    public String getPathLength() {
        return this.pathLenght;
    }

    /**
     * Il metodo permette di ottenere attraverso una stringa, il tempo (in ore e minuti) del cammino/instradamento generato.
     * @return Una stringa contente il tempo (in ore e minuti) del cammino/instrdamento generato.
     */
    public String getPathTime(){
        return this.pathTime;
    }

    /**
     * Il metodo permette di definire il percorso/instradamento inverso rispetto a due località.
     * Il vecchio startPoint diverrà la nuova destinazione, mentre il vecchio endPoint diverrà il nuovo punto di partenza.
     * @param newStartText
     * @param newEndText
     * @throws Exception Eccezione richiamata se l'instradamento/percorso non viene trovato e dunque non generato.
     */
    public void invertPath(String newStartText, String newEndText) throws Exception {
        if (startMarker != null && endMarker != null && startPoint != null && endPoint != null)
            if (!inverted) {
                GeoPoint newStartPoint = endPoint;
                GeoPoint newEndPoint = startPoint;
                updateStartNavigationPath(newStartPoint, newStartText);
                updateDestinationNavigationPath(newEndPoint, newEndText);
                inverted = true;

            } else {
                GeoPoint newStartPoint = startPoint;
                GeoPoint newEndPoint = endPoint;
                updateStartNavigationPath(newStartPoint, newEndText);
                updateDestinationNavigationPath(newEndPoint, newStartText);
                inverted = false;
            }
        mapView.invalidate();
    }

    /**
     * Il metodo permette di resettare il cammino e le informazioni relative ad esso, riportando la mappa nella condizione di default.
     */
    public void deletePath() {
        waypoints.clear();
        mapView.getOverlays().clear();
        mapView.getOverlays().add(myLocationOverlay);
    }
}