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

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.viewmodel.Explore_ViewModel;

public class Explore_Card_fragment extends Fragment {

    public Explore_Card_fragment() {
        // Required empty public constructor
    }

    private TextView cardText;
    private View card;
    private Button cardButton;
    private BottomSheetBehavior<View> bottomSheetBehavior;
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
        cardText = card.findViewById(R.id.place_text);

        // Il tasto "Navigate"
        cardButton = card.findViewById(R.id.bottomsheet_button);

        // Cliccando il tasto nagigate, inizia la naviazione dalla posizione attuale fino al luogo
        // nella sezione navigate dell'app
        cardButton.setOnClickListener(l -> {
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