package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.map.ExploreMap;
import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.viewmodel.Location_ViewModel;

/**
 * La classe permette di gestire la logica derivante dalle interazioni dell'utente con l'Explore_Fragment,
 * invocando i metodi della classe ExploreMap corrispondenti.
 * {@link Explore_Fragment}
 * {@link ExploreMap}
 */

// Per vedere la logica dietro la Mappa, vedere la classe ExploreMap
public class Explore_Map_Fragment extends Fragment{
    private ExploreMap exploreMap;
    private Location_ViewModel location_viewModel;

    /**
     * Costruttore della classe Explore_Map_Fragment
     */
    public Explore_Map_Fragment() {
        super(R.layout.fragment_explore_map);
    }

    /**
     * Il metodo ritorna una nuova istanza della classe Explore_Map_Fragment
     * @return Una nuova istanza della classe Explore_Map_Fragment
     */
    public Explore_Map_Fragment newInstance() {
        return new Explore_Map_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location_viewModel = new ViewModelProvider(requireParentFragment()).get(Location_ViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observer che centra la mappa sulla posizione cercata o inserita tramite long press
        final Observer<Location> updateLocation = l -> {
            if(l != null) {
                exploreMap.setMarkerPosition(new GeoPoint(
                        Double.parseDouble(l.getLat()), Double.parseDouble(l.getLon())
                        )
                );
            }
            else{
                Snackbar.make(this.getView().getRootView(),"Impossibile contattare il server di Geocoding. Riprova.", Snackbar.LENGTH_SHORT)
                        .show();
            }
        };

        location_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_explore_map, container, false);
        exploreMap = new ExploreMap(rootView.findViewById(R.id.mappa_esplora));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.exploreMap.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.exploreMap.pause();
    }

    /**
     * Il metodo richiama il metodo resetCenter(), al fine di resettare il centro della mappa nel Geopoint della
     * posizione attuale dell'utente.
     */
    public void resetCenter() {
        this.exploreMap.resetCenter();
        this.exploreMap.getMapController().setZoom(17.0);
    }

    /**
     * Il metodo permette di definire e disegnare sulla mappa il percorso, richiamando il metodo drawRoad() della classe ExploreMap.
     * @param waypoints ArrayList contenente Geopoints, utilizzato per la creazione del percorso/instradamento.
     * @throws Exception Eccezione generata se l'instradamento/percorso non viene trovato e dunque non generato.
     * {@link ExploreMap}
     */
    public void drawRoad(ArrayList<GeoPoint> waypoints) throws Exception {
        this.exploreMap.drawRoad(waypoints);
    }

    /**
     * Il metodo permette di settare un marker sulla mappa nella posizione attuale, richiamando il metodo
     * setMarkerPosition() della classe ExploreMap
     * @param p Rappresenta un Geopoint (località), ossia un punto geolocalizzato sulla mappa caratterizzato da longitudine e latitudine.
     * {@link ExploreMap}
     */
    public void setCenter(GeoPoint p) {
        this.exploreMap.setMarkerPosition(p);
    }
}