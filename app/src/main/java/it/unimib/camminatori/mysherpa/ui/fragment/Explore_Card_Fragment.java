package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.Location;
import it.unimib.camminatori.mysherpa.model.Weather;
import it.unimib.camminatori.mysherpa.ui.activity.MainActivity;
import it.unimib.camminatori.mysherpa.viewmodel.Data_Location_ViewModel;
import it.unimib.camminatori.mysherpa.viewmodel.Location_ViewModel;
import it.unimib.camminatori.mysherpa.viewmodel.Weather_ViewModel;

public class Explore_Card_Fragment extends Fragment {

    public Explore_Card_Fragment() {
        // Required empty public constructor
    }

    Context context;
    private View card;

    private TextView locationName;
    private TextView locationInfo2;
    private TextView locationInfo1;

    private TextView temperature;
    private TextView humidity;
    private TextView wind;

    private Button cardButtonNavigate;
    private Button cardButtonSave;

    private BottomSheetBehavior<View> bottomSheetBehavior;
    private Location_ViewModel location_viewModel;
    private Weather_ViewModel weather_viewModel;
    private Data_Location_ViewModel dataLocation_ViewModel;

    private double lat;
    private double lon;

    public static Explore_Card_Fragment newInstance() {
        return new Explore_Card_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location_viewModel = new ViewModelProvider(requireParentFragment()).get(Location_ViewModel.class);
        weather_viewModel = new ViewModelProvider(requireParentFragment()).get(Weather_ViewModel.class);
        dataLocation_ViewModel = new ViewModelProvider(requireParentFragment()).get(Data_Location_ViewModel.class);

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

        temperature = view.findViewById(R.id.temperature);
        wind = view.findViewById(R.id.windInfo);
        humidity = view.findViewById(R.id.humidityInfo);

        cardButtonNavigate = card.findViewById(R.id.bottomsheet_button_navigate);
        cardButtonSave = card.findViewById(R.id.bottomsheet_button_save);

        // Cliccando il tasto nagigate, inizia la naviazione dalla posizione attuale fino al luogo
        // nella sezione navigate dell'app
        cardButtonNavigate.setOnClickListener(l -> {
            this.transaction();
        });

        // Cliccando il tasto save, si salva il luogo nei preferiti
        cardButtonSave.setOnClickListener(l -> {
            LiveData<Location> location = location_viewModel.getGeocodedLocation();

            dataLocation_ViewModel.addRecord(
                    locationName.getText().toString(),
                    Double.parseDouble(location.getValue().getLat()),
                    Double.parseDouble(location.getValue().getLon()));

        });

        // Observer che aggiorna la label del posto nel BottomSheet
        final Observer<Location> updateLocationLabels = l -> {
            if(l != null) {
                String[] locationInfoText = l.getDisplayName().split(",", -1);
                locationName.setText(locationInfoText[0]);
                if(locationInfoText.length >= 2) {
                    locationInfo1.setText(locationInfoText[2] + ", ");
                    locationInfo2.setText(locationInfoText[1]);
                }
                else{
                    locationInfo1.setText("");
                    locationInfo2.setText("");
                }
                lat = Double.parseDouble(l.getLat());
                lon = Double.parseDouble(l.getLon());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            else{
                Snackbar.make(this.getView().getRootView(),"Qualcosa è andato storto. Riprova.", Snackbar.LENGTH_SHORT)
                        .show();
            }
        };

        final Observer<Weather> updateWeatherLabels = l -> {
            if(l != null) {
                humidity.setText(l.getHumidity() + " %");
                temperature.setText(l.getTemp() + "°");
                wind.setText(l.getWindSpeed() + " km/h");
            }
            else{
                Snackbar.make(this.getView().getRootView(),"Qualcosa è andato storto. Riprova.", Snackbar.LENGTH_SHORT)
                        .show();
            }

        };

        location_viewModel.getGeocodedLocation().observe(getViewLifecycleOwner(), updateLocationLabels);
        weather_viewModel.getWeather().observe(getViewLifecycleOwner(), updateWeatherLabels);
    }

    private void transaction(){
        Bundle bundle = new Bundle();
        bundle.putDouble("destLon", lon);
        bundle.putDouble("destLat", lat);
        bundle.putString("destText", locationName.getText().toString());
        Navigation.findNavController(this.getActivity().findViewById(R.id.nav_host_fragment)).popBackStack();
        Navigation.findNavController(this.getActivity().findViewById(R.id.nav_host_fragment)).navigate(R.id.fragment_route, bundle);

    }
}