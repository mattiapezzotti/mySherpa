package it.unimib.camminatori.mysherpa.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.viewmodel.RecordViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Record_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Record_Fragment extends Fragment {
    final private String TAG = "fragment_record";

    // Shared preferences key names
    final static public String FAVOURITES_RECORDS_SHAREDPREFS = "FAVOURITES_RECORDS";
    final static public String FAVOURITES_RECORDS = "FAVOURITE_RECORDS_LIST";
    final static public String TOTAL_METERS_SHAREDPREF = "TOTAL_METERS";
    final static public String TOTAL_METERS_VAL = "TOTAL_METERS_VAL";

    // Text View
    private TextView recordTimeView;
    private TextView distanceView;

    // View Model
    private RecordViewModel recordViewModel;

    public Record_Fragment() {
        // Required empty public constructor
    }

    public static Record_Fragment newInstance() {
        return new Record_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);

        ArrayList<RecordViewModel.SaveRecordInfo> favList;

        SharedPreferences favRecordsPreferences = requireContext().getSharedPreferences(FAVOURITES_RECORDS_SHAREDPREFS, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        String saveJson = favRecordsPreferences.getString(FAVOURITES_RECORDS, "");
        if (saveJson.equals("")) {
            favList = new ArrayList<>();
        } else {
            favList = gson.fromJson(saveJson, new TypeToken<ArrayList<RecordViewModel.SaveRecordInfo>>() {
            }.getType());
        }

        final Observer<RecordViewModel.RecordInfo> recordInfoObserver = recordInfo -> {
            recordTimeView.setText(recordInfo.timerText);
            distanceView.setText(recordInfo.metersText);

            SharedPreferences totMetersPreferences = requireContext().getSharedPreferences(TOTAL_METERS_SHAREDPREF, Context.MODE_PRIVATE);
            float totMeters = totMetersPreferences.getFloat (TOTAL_METERS_VAL, 0) + recordInfo.updateMeters;

            SharedPreferences.Editor totMetersEditor = totMetersPreferences.edit();
            totMetersEditor.putFloat(TOTAL_METERS_VAL, totMeters);

            totMetersEditor.apply();

            Log.i(TAG, "Total meters: " + totMeters);
        };

        recordViewModel.getRecordInfo(getContext(), favList).observe(this, recordInfoObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordTimeView = (TextView) view.findViewById(R.id.record_time_view);
        distanceView = (TextView) view.findViewById(R.id.distance_view);

        ImageButton saveRecordButton = (ImageButton) view.findViewById(R.id.button_save_record);
        ImageButton playRecordButton = (ImageButton) view.findViewById(R.id.button_start_record);
        ImageButton stopRecordButton = (ImageButton) view.findViewById(R.id.button_stop_record);

        saveRecordButton.setOnClickListener(v -> {
            recordViewModel.buttonSaveClicked("Nome Localita_" + SystemClock.elapsedRealtime());
        });

        playRecordButton.setOnClickListener(v -> {
            recordViewModel.buttonPlayClicked();
        });

        stopRecordButton.setOnClickListener(v -> {
            recordViewModel.buttonStopClicked();
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        SharedPreferences favRecordsPreferences = requireContext().getSharedPreferences(FAVOURITES_RECORDS_SHAREDPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = favRecordsPreferences.edit();

        ArrayList<RecordViewModel.SaveRecordInfo> favList = recordViewModel.getFavList();

        Gson gson = new Gson();

        String json = gson.toJson(favList);

        editor.putString(FAVOURITES_RECORDS, json);
        editor.apply();
    }
}