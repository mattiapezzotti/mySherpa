package it.unimib.camminatori.mysherpa.model.map;



import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.viewmodel.Location_ViewModel;
import it.unimib.camminatori.mysherpa.viewmodel.Weather_ViewModel;

/**
 * La classe permette di specificare, settare e aggiornare i marker sulla mappa, attraverso una pressione prolungata o, un singolo tap su schermo.
 * Inoltre crea e definisce le informazioni principali quali tempo e distanza, di un percorso/instradamento generato a partire da due località (Geopoints).
 * {@link it.unimib.camminatori.mysherpa.ui.fragment.Explore_Map_Fragment}
 */
public class ExploreMap extends Map {

    private Marker marker;
    private final Location_ViewModel location_viewModel;
    private final Weather_ViewModel weather_viewModel;

    /**
     * Costruttore della classe ExploreMap
     * @param map
     */
    public ExploreMap(MapView map){
        super(map);

        location_viewModel = new Location_ViewModel();
        weather_viewModel = new Weather_ViewModel();

        this.marker = new Marker(mapView);
        this.marker.setIcon(red_Marker);
        this.marker.setInfoWindow(null);
    }

    /**
     * Il metodo permette di rimuove i marker (rossi) attraverso un single tap.
     * @param p Rappresenta un Geopoint (località), ossia un punto geolocalizzato sulla mappa caratterizzato da longitudine e latitudine.
     * @return Un valore booleano con valore true.
     */
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        this.mapView.getOverlays().remove(marker);
        return true;
    }

    /**
     * Il metodo permette di inserire nel punto premuto un marker (Geopoint) attraverso un long tap, centrando la mappa su di esso e
     * aggiornando la location in focus, in modo da cpomunicarlo alle altre classi.
     * @param p Rappresenta un Geopoint (località), ossia un punto geolocalizzato sulla mappa caratterizzato da longitudine e latitudine.
     * @return Un valore booleano con valore true.
     */
    @Override
    public boolean longPressHelper(GeoPoint p) {
        updateLocation(p);
        return true;
    }

    /**
     * Il metodo permette di settare un marker sulla mappa nella posizione indicata dal Geopoint (località) passato come parametro formale
     * @param p Rappresenta un Geopoint (località), ossia un punto geolocalizzato sulla mappa caratterizzato da longitudine e latitudine.
     */
    public void setMarkerPosition(GeoPoint p){
        mapView.getOverlays().remove(marker);

        marker.setPosition(p);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        myLocationOverlay.disableFollowLocation();
        mapView.getOverlays().add(marker);
        mapController.animateTo(marker.getPosition());
    }

    /**
     * Il metodo permette di ottenere dato un Geopoint (località) passato come parametro formale, i metadati ed il meteo della località, sfruttando
     * i metodi reverseGeocode della classe Location_ViewModel e getCoordinatesWeather della classe Weather_ViewModel.
     * @param position Rappresenta un Geopoint (località), ossia un punto geolocalizzato sulla mappa caratterizzato da longitudine e latitudine.
     * {@link Location_ViewModel}
     * {@link Weather_ViewModel}
     */
    public void updateLocation(GeoPoint position){
        if(position == null)
            position = myLocationOverlay.getMyLocation();
        location_viewModel.reverseGeocode(position.getLatitude(), position.getLongitude());
        weather_viewModel.getCoordinatesWeather(position.getLatitude(), position.getLongitude());
    }

    /**
     * Definisce a partire dalle località (Geopoints) contenute nell'arrayList Waypoints
     * il percorso/instradamento (Road) disegnando sulla mappa attraverso una Polyline il cammino precedentemente trovato.
     * In aggiunta precisa in relazione al percoso/instradamento, i punti intermedi aventi ruolo di indicazioni, specificando anche per essi tempo, distanza e nome di quest'ultimi.
     * @param waypoints ArrayList contenente Geopoints, utilizzato per la creazione del percorso/instradamento.
     * @throws Exception Eccezione generata se l'instradamento/percorso non viene trovato e dunque non generato.
     */
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