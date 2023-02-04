package it.unimib.camminatori.mysherpa.ui.fragment;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.osmdroid.util.GeoPoint;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.map.RouteMap;

/**
 * La classe permette di gestire le invocazioni dovute alle interazioni dell'utente con i diversi componenti grafici che la compongono,
 * richiamando i metodi della classe Route_Map_Fragment corrispondenti.
 * {@link Route_Map_Fragment}
 */
public class Route_Fragment extends Fragment {

    private TextInputEditText textPartenza;
    private TextInputEditText textDestinazione;

    private Button navigateButton;

    private ImageButton deletePath;
    private ImageButton invertPath;

    private TextView timeText;
    private TextView kmText;

    private CardView cardInfo;
    private FloatingActionButton myLocationFAB;

    private Route_Map_Fragment rmf;

    /**
     * Costruttore della classe Route_Fragment
     */
    public Route_Fragment() {
    }

    /**
     * Il metodo restituisce una nuova istanza della classe Route_Fragment
     * @return Una nuova istanza della classe Route_Fragment
     */
    public static Route_Fragment newInstance() {
        return new Route_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        textPartenza = view.findViewById(R.id.search_bar_textStart);
        textDestinazione = view.findViewById(R.id.search_bar_textEnd);
        navigateButton = view.findViewById(R.id.button_navigate);
        invertPath = view.findViewById(R.id.swapPath);
        deletePath = view.findViewById(R.id.deletePath);

        cardInfo = view.findViewById(R.id.cardInfo);
        timeText = view.findViewById(R.id.time_text);
        kmText = view.findViewById(R.id.kilometers_text);
        myLocationFAB = view.findViewById(R.id.fab_getMyLocation);

        cardInfo.setVisibility(View.GONE);

        rmf = (Route_Map_Fragment) getChildFragmentManager().findFragmentById(R.id.fragment_map_route);

        myLocationFAB.clearFocus();

        myLocationFAB.setOnClickListener(v ->
                rmf.resetCenter()
        );

        navigateButton.setOnClickListener(v -> {
            String a = String.valueOf(textPartenza.getText()).trim();
            String b = String.valueOf(textDestinazione.getText()).trim();

            if(!a.isEmpty() && !b.isEmpty()) {
                rmf.findPathTextOnly(a, b);
                (new Handler()).postDelayed(()
                        -> updateInfoCard(), 1000
                );
            }
            else{
                Snackbar.make(this.getView().getRootView(),"Inserisci Partenza e Destinazione", Snackbar.LENGTH_SHORT)
                                .show();
            }
        });

        invertPath.setOnClickListener(v -> {
            String newEndText = String.valueOf(textPartenza.getText()).trim();
            String newStartText = String.valueOf(textDestinazione.getText()).trim();

            if(!newEndText.isEmpty() && !newStartText.isEmpty()) {
                textPartenza.setText(newStartText);
                textDestinazione.setText(newEndText);
                rmf.invertPath(newStartText,newEndText);
            }
        });

        deletePath.setOnClickListener(v -> {
            textPartenza.setText("");
            textDestinazione.setText("");
            rmf.deletePath();
            cardInfo.setVisibility(View.GONE);
        });

        if(getArguments() != null) {
            if(getArguments().getString("destText") != null) {
                textPartenza.setText("myPosition");
                textDestinazione.setText(getArguments().getString("destText"));

                String destText = getArguments().getString("destText");
                Double lat = getArguments().getDouble("destLat");
                Double lon = getArguments().getDouble("destLon");

                (new Handler()).postDelayed(()
                        -> rmf.findPathWithNode(new GeoPoint(lat, lon), destText), 1000
                );

                (new Handler()).postDelayed(()
                        -> updateInfoCard(), 1000
                );
            }
        }
    }

    /**
     * Il metodo successivamente alla definizione del percorso/instradamento, permette di visualizzare le informazioni quali
     * tempo e distanza attraverso una card, sfruttando i metodi getPathLenght() e getPathTime() della classe Route_Map_Fragment usufuendo del
     * metodo setText().
     * {@link Route_Map_Fragment}
     */
    public void updateInfoCard(){
        kmText.setText(rmf.getPathLength());
        timeText.setText(rmf.getPathTime());
        cardInfo.setVisibility(View.VISIBLE);
    }
}