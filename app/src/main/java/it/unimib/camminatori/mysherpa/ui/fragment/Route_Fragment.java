package it.unimib.camminatori.mysherpa.ui.fragment;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.StrictMode;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.Map;

import it.unimib.camminatori.mysherpa.BuildConfig;
import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.ui.activity.MainActivity;
import it.unimib.camminatori.mysherpa.viewmodel.Explore_ViewModel;

public class Route_Fragment extends Fragment {

    private Explore_ViewModel explore_viewModel;
    private TextInputEditText textPartenza;
    private TextInputEditText textDestinazione;
    private TextView testoKM;
    private TextView testoTime;
    private Button bottone_ricerca;
    private GeoPoint startPoint;
    private GeoPoint endPoint;

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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        textPartenza = view.findViewById(R.id.search_bar_text);
        textDestinazione = view.findViewById(R.id.search_bar_text2);
        bottone_ricerca = view.findViewById(R.id.button_navigate);

        MapView map = view.findViewById(R.id.map);
        map.setMultiTouchControls(true);
        Marker startMarker = new Marker(map);
        Marker startMarker2 = new Marker(map);
        IMapController mapController = map.getController();
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        RoadManager roadManager = new OSRMRoadManager(getContext(), BuildConfig.APPLICATION_ID);
        mapController.setZoom(12);
        mapController.setCenter(map.getMapCenter());

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
                    startMarker2.setPosition(endPoint);
                    startMarker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    map.getOverlays().add(startMarker2);
                    map.invalidate();
                    startMarker2.setIcon(getResources().getDrawable(R.drawable.ic_baseline_circle_24_marker));
                    startMarker2.setTitle("Destinazione: " + destinazione);
                    map.invalidate();
                    waypoints.add(endPoint);
                    Road road = roadManager.getRoad(waypoints);
                    Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                    map.getOverlays().add(roadOverlay);
                    map.invalidate();
                    startMarker.setSubDescription(Road.getLengthDurationText(getContext(), 0, 0));
                    startMarker2.setSubDescription(Road.getLengthDurationText(getContext(), road.mLength, road.mDuration));
                    //testoKM.setText("Distanza,Tempo:" + (Road.getLengthDurationText(MainActivity.this, road.mLength, road.mDuration)));
                    //testoKM.setText("Distanza: " + Math.round((road.mLength)*10.0) / 10.0 + " Km");
                    //testoTime.setText("Tempo: " + Math.round((road.mDuration/60) * 10.0) / 10.0 + " minuti");
                    //Creazione dei possibili nodi intermedi(indicazioni de percorso)
                    Drawable nodeIcon = getResources().getDrawable(R.drawable.ic_baseline_circle_24_marker3);
                    for (int i=0; i<road.mNodes.size(); i++){
                        RoadNode node = road.mNodes.get(i);
                        Marker nodeMarker3= new Marker(map);
                        nodeMarker3.setPosition(node.mLocation);
                        nodeMarker3.setIcon(nodeIcon);
                        nodeMarker3.setTitle("Step "+i);
                        map.getOverlays().add(nodeMarker3);
                        nodeMarker3.setSnippet(node.mInstructions);
                        nodeMarker3.setSubDescription(Road.getLengthDurationText(getContext(), node.mLength, node.mDuration));
                        Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_arrow_upward_24);
                        nodeMarker3.setImage(icon);
                    }
                    map.invalidate();
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
                    map.invalidate();
                    startMarker.setIcon(getResources().getDrawable(R.drawable.ic_baseline_circle_24_marker2));
                    startMarker.setTitle("Partenza: " + partenza);
                    map.getOverlays().add(startMarker);
                    map.invalidate();
                    waypoints.add(startPoint);
                }
                explore_viewModel.geocodePlace(destinazione);
                explore_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocationArrivo);
            };
            explore_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocationPartenza);
        });
    }
}