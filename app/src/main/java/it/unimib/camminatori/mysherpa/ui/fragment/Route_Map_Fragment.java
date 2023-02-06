package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import org.osmdroid.util.GeoPoint;

import java.util.Objects;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.model.map.RouteMap;
import it.unimib.camminatori.mysherpa.viewmodel.Location_ViewModel;

/**
 * La classe permette di gestire la logica derivante dalle interazioni dell'utente con il Route_Fragment,
 * invocando i metodi della classe RouteMap corrispondenti.
 * {@link RouteMap}
 * {@link Route_Fragment}
 * Per vedere la logica relativa alla Mappa, visitare {@link it.unimib.camminatori.mysherpa.model.map.Map}
 */

public class Route_Map_Fragment extends Fragment{
    private RouteMap routeMap;
    private Location_ViewModel location_viewModel;
    private boolean start = true;
    private boolean pathStart = false;
    private final String startOnMyPostionString = "myPosition";

    /**
     * Costruttore della classe Route_Map_Fragment
     */
    public Route_Map_Fragment() { super(R.layout.fragment_route_map); }

    /**
     * Il metodo restituisce una nuova istanza della classe Route_Map_Fragment
     * @return Una nuova istanza Route_Map_Fragment
     */
    public Route_Map_Fragment newInstance() {
        return new Route_Map_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location_viewModel = new ViewModelProvider(requireParentFragment()).get(Location_ViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_route_map, container, false);
        routeMap = new RouteMap(rootView.findViewById(R.id.mappa_naviga));

        final Observer<Location> updateLocation = l -> {
            if (l != null) {

                GeoPoint p = new GeoPoint(
                        Double.parseDouble(l.getLat()), Double.parseDouble(l.getLon())
                );

                if (pathStart)
                    if (start) {
                        routeMap.updateStartNavigationPath(p, l.getDisplayName());
                        start = false;
                    } else {
                        try {
                            routeMap.updateDestinationNavigationPath(p, l.getDisplayName());
                        } catch (Exception e) {
                            this.printError(e.getMessage());
                        }
                        start = true;
                        pathStart = false;
                    }
            } else {
                Snackbar.make(this.getView(), "Qualcosa è andato storto. Riprova.", Snackbar.LENGTH_SHORT)
                        .show();
            }
        };
        location_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocation);
        return rootView;
    }

    /**
     * Il metodo richiama il metodo resetCenter(), presente nella classe Map, al fine di resettare il centro della mappa nel Geopoint della
     * posizione attuale dell'utente.
     * {@link RouteMap}
     */
    public void resetCenter(){
        routeMap.resetCenter();
    }

    /**
     * Il metodo permette di definire il percorso/instradamento tra due località, richiamando i metodi updateStartNavigation() e updateDestinationNavigationPath() della
     * classe RouteMap. I Geopoints delle località di partenza e di arrivo vengono ricavate tramite il metodo geocodePlace() della classe Location_ViewModel, passando
     * come parametri formali le stringhe contenenti il nome delle due località.
     * @param startText
     * @param endText
     * {@link RouteMap}
     */
    public void findPathTextOnly(String startText, String endText) {
        start = true;
        pathStart = true;

        if (!Objects.equals(startText, startOnMyPostionString))
            location_viewModel.geocodePlace(startText);
        else {
            routeMap.updateStartNavigationPath(routeMap.getMyLocationOverlay().getMyLocation(), "My Position");
            start = false;
        }

        if (!Objects.equals(endText, startOnMyPostionString))
            if (!Objects.equals(startText, startOnMyPostionString))
                (new Handler()).postDelayed(()
                        -> location_viewModel.geocodePlace(endText), 500
                );
            else
                location_viewModel.geocodePlace(endText);
        else if (!Objects.equals(startText, startOnMyPostionString))
            (new Handler()).postDelayed(()
                            -> {
                        try {
                            routeMap.updateDestinationNavigationPath(routeMap.getMyLocationOverlay().getMyLocation(), "My Position");
                        } catch (Exception e) {
                            this.printError(e.getMessage());
                        }
                    }, 500
            );
        else {
            try {
                routeMap.updateDestinationNavigationPath(routeMap.getMyLocationOverlay().getMyLocation(), "My Position");
            } catch (Exception e) {
                this.printError(e.getMessage());
            }
        }

    }

    /**
     * Il metodo permette di definire il percorso/instradamento tra due località, richiamando i metodi updateStartNavigation() e updateDestinationNavigationPath() della
     * classe RouteMap. Il Geopoint della località di partenza viene ricavato richiamando il metodo getMyLocation() il quale ritornerà la posizione attuale, mentre
     * il Geopoint relativo alla località di arrivo verrà passtao come parametro formale in fase di chiamata.
     * @param endNode Rappresenta un Geopoint (della località di arrivo), ossia un punto geolocalizzato sulla mappa caratterizzato da longitudine e latitudine.
     * @param endText
     * {@link RouteMap}
     */
    public void findPathWithNode(GeoPoint endNode, String endText){
        routeMap.updateStartNavigationPath(routeMap.getMyLocationOverlay().getMyLocation(), "My Position");
        try {
            routeMap.updateDestinationNavigationPath(endNode, endText);
        } catch (Exception e) {
            this.printError(e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.routeMap.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.routeMap.pause();
    }

    /**
     * Il metodo richiama il metodo getPathLenght() della classe RouteMap e
     * permette di ottenere attraverso una stringa, la distanza tra le due località (in Km) del cammino/instradamento generato.
     * @return Una stringa contente la distanza tra le due località (in Km) del cammino/instrdamento generato.
     * {@link RouteMap}
     */
    public String getPathLength() {
        return routeMap.getPathLength();
    }

    /**
     * Il metodo richiama il metodo getPathTime() della classe RouteMap e
     * permette di ottenere attraverso una stringa, il tempo (in ore e minuti) del cammino/instradamento generato.
     * @return Una stringa contente il tempo (in ore e minuti) del cammino/instrdamento generato.
     * {@link RouteMap}
     */
    public String getPathTime() {
        return routeMap.getPathTime();
    }

    /**
     * Il metodo richiama il metodo invertPath() della classe RouteMap e
     * permette di definire il percorso/instradamento inverso rispetto a due località.
     * @param startText
     * @param endText
     * {@link RouteMap}
     */
    public void invertPath(String startText, String endText) {
        try {
            this.routeMap.invertPath(startText, endText);
        } catch (Exception e) {
            this.printError(e.getMessage());
        }
    }

    /**
     * Il metodo richima il metodo deletePath() della lasse RouteMap e
     * permette di resettare il cammino e le informazioni relative ad esso, riportando la mappa nella condizione di default.
     * {@link RouteMap}
     */
    public void deletePath() {
        this.routeMap.deletePath();
    }

    /**
     * Il metodo tramite una Snackbar permette di evidenziare all'utente se vi è un errore dovuto alla mancanza di inserimento dei
     * parametri necessari alla creazione del percorso/intradamento.
     * @param errorMessage
     * {@link RouteMap}
     */
    public void printError(String errorMessage){
        Snackbar.make(this.getView(), errorMessage, Snackbar.LENGTH_SHORT).show();
    }
}