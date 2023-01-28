package it.unimib.camminatori.mysherpa.ui.fragment;

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

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.repository.LocationRepository;
import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.viewmodel.Explore_ViewModel;

public class Explore_Card_fragment extends Fragment {

    public Explore_Card_fragment() {
        // Required empty public constructor
    }

    private TextView cardText;
    private View card;
    private Button cardButtonNavigate;
    private Button cardButtonSave;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private TextView kilometers;
    private TextView altitude;
    private TextView time;
    private TextView temperature;
    private TextView wind;
    private TextView precipitation;
    private TextView pressure;
    private String location;
    private Explore_ViewModel explore_viewModel;

    public static Explore_Card_fragment newInstance() {
        return new Explore_Card_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        explore_viewModel = new ViewModelProvider(requireParentFragment()).get(Explore_ViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explorecard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        card = view.findViewById(R.id.standard_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(card);
        kilometers = view.findViewById(R.id.kilometers_text);
        altitude = view.findViewById(R.id.altitude_text);
        time = view.findViewById(R.id.time_text);
        temperature = view.findViewById(R.id.temperature);
        wind = view.findViewById(R.id.wind);
        precipitation = view.findViewById(R.id.precipitation);
        pressure = view.findViewById(R.id.pressure);


        // Il tasto "Navigate"
        cardButtonNavigate = card.findViewById(R.id.bottomsheet_button_navigate);
        cardButtonSave = card.findViewById(R.id.bottomsheet_button_navigate);

        // Cliccando il tasto nagigate, inizia la naviazione dalla posizione attuale fino al luogo
        // nella sezione navigate dell'app
        cardButtonNavigate.setOnClickListener(l -> {
            //TODO: cambio fragment
        });

        // Cliccando il tasto nagigate, si salva il percorso nella card
        cardButtonSave.setOnClickListener(l -> {
            //TODO: cambio fragment
        });

        // Observer che aggiorna la label del posto nel BottomSheet
        final Observer<Location> updateLabel = l -> {
            if(l.getDisplayName() != null) {
                cardText.setText(l.getDisplayName().split(",")[0]);
            }
        };

        explore_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLabel);
    }
}