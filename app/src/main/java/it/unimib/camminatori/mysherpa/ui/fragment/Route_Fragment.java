package it.unimib.camminatori.mysherpa.ui.fragment;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.BuildConfig;
import it.unimib.camminatori.mysherpa.MapWrapper;
import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.viewmodel.Explore_ViewModel;

public class Route_Fragment extends Fragment {

    private Explore_ViewModel explore_viewModel;
    private TextInputEditText textPartenza;
    private TextInputEditText textDestinazione;
    private Button bottone_ricerca;
    private GeoPoint startPoint;
    private GeoPoint endPoint;

    private RoadManager roadManager;

    public Route_Fragment() {
    }

    public static Route_Fragment newInstance() {
       return new Route_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        explore_viewModel = new ViewModelProvider(requireParentFragment()).get(Explore_ViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        roadManager = new OSRMRoadManager(getContext(), BuildConfig.APPLICATION_ID);

        textPartenza = view.findViewById(R.id.search_bar_textStart);
        textDestinazione = view.findViewById(R.id.search_bar_textEnd);
        bottone_ricerca = view.findViewById(R.id.button_navigate);

        MapView mapView = view.findViewById(R.id.map);
        mapView.setMultiTouchControls(true);
        Marker startMarker = new Marker(mapView);
        Marker endMarker = new Marker(mapView);
        IMapController mapController = mapView.getController();
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        mapController.setCenter(mapView.getMapCenter());
        mapController.setZoom(17.0);

        bottone_ricerca.setOnClickListener(view1 -> {
            String partenza = String.valueOf(textPartenza.getText());
            String destinazione = String.valueOf(textDestinazione.getText());
            explore_viewModel.geocodePlace(partenza);

            //Definizione punto di arrivo e creazione del tragitto(road) e della polyline sulla mappa
            final Observer<Location> updateLocationArrivo = m -> {
                if(m.getLon() != null && m.getLat() != null) {
                    endPoint =
                            new GeoPoint(
                                    Double.parseDouble(m.getLat()), Double.parseDouble(m.getLon())
                            );
                    //CREAZIONE MARKER E PUNTO DI ARRIVO
                    endMarker.setPosition(endPoint);
                    endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    mapView.getOverlays().add(endMarker);
                    mapView.invalidate();
                    endMarker.setIcon(getResources().getDrawable(R.drawable.ic_baseline_circle_24_marker_red));
                    endMarker.setTitle("Destinazione: " + destinazione);
                    mapView.invalidate();
                    waypoints.add(endPoint);
                    Road road = roadManager.getRoad(waypoints);
                    Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                    mapView.getOverlays().add(roadOverlay);
                    mapView.invalidate();
                    startMarker.setSubDescription(Road.getLengthDurationText(getContext(), 0, 0));
                    endMarker.setSubDescription(Road.getLengthDurationText(getContext(), road.mLength, road.mDuration));
                    //testoKM.setText("Distanza,Tempo:" + (Road.getLengthDurationText(MainActivity.this, road.mLength, road.mDuration)));
                    //testoKM.setText("Distanza: " + Math.round((road.mLength)*10.0) / 10.0 + " Km");
                    //testoTime.setText("Tempo: " + Math.round((road.mDuration/60) * 10.0) / 10.0 + " minuti");

                    //Creazione dei possibili nodi intermedi(indicazioni de percorso)
                    Drawable roadIcon = getResources().getDrawable(R.drawable.ic_baseline_circle_24_marker_black);
                    for (int i=0; i<road.mNodes.size(); i++){
                        RoadNode node = road.mNodes.get(i);
                        Marker roadMarker= new Marker(mapView);
                        roadMarker.setPosition(node.mLocation);
                        roadMarker.setIcon(roadIcon);
                        roadMarker.setTitle("Step "+ i);
                        mapView.getOverlays().add(roadMarker);
                        roadMarker.setSnippet(node.mInstructions);
                        roadMarker.setSubDescription(Road.getLengthDurationText(getContext(), node.mLength, node.mDuration));
                        Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_arrow_upward_24);
                        roadMarker.setImage(icon);
                    }
                    mapView.invalidate();
                }
            };

            //Definizione punto di partenza
            final Observer<Location> updateLocationPartenza = l -> {
                if(l.getLon() != null && l.getLat() != null) {
                    startPoint =
                            new GeoPoint(
                                    Double.parseDouble(l.getLat()), Double.parseDouble(l.getLon())
                            );

                    //CREAZIONE MARKER E PUNTO DI PARTENZA
                    startMarker.setPosition(startPoint);
                    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    mapController.animateTo(startPoint);
                    mapController.setCenter(startPoint);
                    mapView.invalidate();
                    startMarker.setIcon(getResources().getDrawable(R.drawable.ic_baseline_circle_24_marker_blue));
                    startMarker.setTitle("Partenza: " + partenza);
                    mapView.getOverlays().add(startMarker);
                    mapView.invalidate();
                    waypoints.add(startPoint);
                }
                explore_viewModel.geocodePlace(destinazione);
                explore_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocationArrivo);
            };
            explore_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocationPartenza);
        });
    }
}