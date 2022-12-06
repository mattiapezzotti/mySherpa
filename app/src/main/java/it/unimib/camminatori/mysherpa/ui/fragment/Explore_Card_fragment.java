package it.unimib.camminatori.mysherpa.ui.fragment;

import static android.view.View.INVISIBLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.osmdroid.util.GeoPoint;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.pojo.Location;
import it.unimib.camminatori.mysherpa.viewmodel.Explore_ViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Explore_Card_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Explore_Card_fragment extends Fragment {

    public Explore_Card_fragment() {
        // Required empty public constructor
    }

    private TextView cardText;
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
        cardText = view.findViewById(R.id.standard_bottom_sheet).findViewById(R.id.place_text);
        final Observer<Location> updateLabel = l -> {
            if(l.getDisplayName() != null) {
                cardText.setText(l.getDisplayName());
            }
        };

        explore_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLabel);
    }
}