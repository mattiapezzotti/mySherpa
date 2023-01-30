package it.unimib.camminatori.mysherpa.ui.fragment;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.osmdroid.util.GeoPoint;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.map.RouteMap;

public class Route_Fragment extends Fragment {

    private TextInputEditText textPartenza;
    private TextInputEditText textDestinazione;
    private Button navigateButton;
    private ImageButton deletePath;
    private ImageButton invertPath;
    private RouteMap routeMap;
    private FloatingActionButton myLocationFAB;

    public Route_Fragment() {
    }

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
        Route_Map_Fragment rmf = (Route_Map_Fragment) getChildFragmentManager().findFragmentById(R.id.fragment_map_route);

        myLocationFAB = view.findViewById(R.id.fab_getMyLocation);

        myLocationFAB.clearFocus();

        myLocationFAB.setOnClickListener(v -> {
            rmf.resetCenter();
        });

        navigateButton.setOnClickListener(v -> {
            rmf.findPathTextOnly(
                    String.valueOf(textPartenza.getText()).trim(),
                    String.valueOf(textDestinazione.getText()).trim()
            );
        });

        invertPath.setOnClickListener(v -> {
            String a = String.valueOf(textPartenza.getText()).trim();
            String b = String.valueOf(textDestinazione.getText()).trim();
            textPartenza.setText(b);
            textDestinazione.setText(a);
        });

        deletePath.setOnClickListener(v -> {
            textPartenza.setText("");
            textDestinazione.setText("");
        });

        if(getArguments() != null) {

                textPartenza.setText("myLocation");
                textDestinazione.setText(getArguments().getString("destText"));

                String destText = getArguments().getString("destText");
                Double lat = getArguments().getDouble("destLat");
                Double lon = getArguments().getDouble("destLon");

                (new Handler()).postDelayed(()
                        -> rmf.findPathWithNode(new GeoPoint(lat,lon), destText), 1000
                );
            }
    }
}