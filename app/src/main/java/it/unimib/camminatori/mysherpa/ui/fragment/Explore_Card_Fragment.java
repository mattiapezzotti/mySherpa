package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.viewmodel.Location_ViewModel;

public class Explore_Card_Fragment extends Fragment {

    public Explore_Card_Fragment() {
        // Required empty public constructor
    }

    private View card;
    private TextView locationName;
    private TextView locationInfo2;
    private TextView locationInfo1;
    private Button cardButtonNavigate;
    private Button cardButtonSave;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private Location_ViewModel location_viewModel;
    private double lat;
    private double lon;

    public static Explore_Card_Fragment newInstance() {
        return new Explore_Card_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location_viewModel = new ViewModelProvider(requireParentFragment()).get(Location_ViewModel.class);
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
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        locationName = view.findViewById(R.id.placeName);
        locationInfo1 = view.findViewById(R.id.placeInfo1);
        locationInfo2 = view.findViewById(R.id.placeInfo2);

        cardButtonNavigate = card.findViewById(R.id.bottomsheet_button_navigate);
        cardButtonSave = card.findViewById(R.id.bottomsheet_button_save);

        // Cliccando il tasto nagigate, inizia la naviazione dalla posizione attuale fino al luogo
        // nella sezione navigate dell'app
        cardButtonNavigate.setOnClickListener(l -> {
            //TODO : fix bug che non permette di ricliccare l'icona explore se non si torna indietro prima
            Bundle bundle = new Bundle();
            bundle.putDouble("destLon", lon);
            bundle.putDouble("destLat", lat);
            bundle.putString("destText", locationName.getText().toString());
            Navigation.findNavController(l).navigate(R.id.fragment_route, bundle);
        });

        // Cliccando il tasto save, si salva il luogo nei preferiti
        cardButtonSave.setOnClickListener(l -> {
            //TODO: cambio fragment
        });

        // Observer che aggiorna la label del posto nel BottomSheet
        final Observer<Location> updateLabel = l -> {
            if(l != null) {
                locationName.setText(l.getDisplayName().split(",")[0]);
                locationInfo1.setText(l.getDisplayName().split(",")[2] + ", ");
                locationInfo2.setText(l.getDisplayName().split(",")[1]);
                lat = Double.parseDouble(l.getLat());
                lon = Double.parseDouble(l.getLon());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        };

        location_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLabel);
    }
}