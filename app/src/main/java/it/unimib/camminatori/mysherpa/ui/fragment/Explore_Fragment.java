package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.osmdroid.util.GeoPoint;

import it.unimib.camminatori.mysherpa.R;

/**
 * La classe permette di gestire le invocazioni dovute alle interazioni dell'utente con i diversi componenti grafici che la compongono,
 * richiamando i metodi della classe Explore_Map_Fragment corrispondenti.
 * {@link Explore_Map_Fragment}
 */
public class Explore_Fragment extends Fragment {

    private Explore_Map_Fragment rme;

    /**
     * Costruttore della classe Explore_Fragment
     */
    public Explore_Fragment() {
        // Required empty public constructor
    }

    /**
     * Il metodo ritorna una nuova istanza della classe Explore_Fragment
     * @param param1
     * @param param2
     * @return Una nuova istanza della classe Explore_Fragment
     */
    public static Explore_Fragment newInstance(String param1, String param2) {
        return new Explore_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rme = (Explore_Map_Fragment) getChildFragmentManager().findFragmentById(R.id.fragment_map_explore);

        FloatingActionButton myLocationFAB = view.findViewById(R.id.fab_getMyLocation);

        myLocationFAB.clearFocus();

        myLocationFAB.setOnClickListener(v -> {
            rme.resetCenter();
        });

        if(getArguments() != null){
            if(getArguments().getParcelableArrayList("waypoints") != null){
                    (new Handler()).postDelayed(()
                            -> {
                                try {
                                    rme.drawRoad(getArguments().getParcelableArrayList("waypoints"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Snackbar.make(
                                            requireActivity().findViewById(R.id.container_main_activity),
                                            "Errore nel disegnare la strada", Snackbar.LENGTH_SHORT)
                                            .show();
                                }
                            }, 500
                    );
            }
        }
    }
}