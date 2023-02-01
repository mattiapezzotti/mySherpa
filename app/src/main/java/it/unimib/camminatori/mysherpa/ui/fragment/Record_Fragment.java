package it.unimib.camminatori.mysherpa.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.viewmodel.Record_ViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Record_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Record_Fragment extends Fragment {
    final private String TAG = "fragment_record";

    // Shared preferences key names
    final static public String TOTAL_METERS_SHAREDPREF = "TOTAL_METERS";
    final static public String TOTAL_METERS_VAL = "TOTAL_METERS_VAL";

    // Text View
    private TextView recordTimeView;
    private TextView distanceView;
    private TextView elevationView;

    // View Model
    private Record_ViewModel recordViewModel;

    public Record_Fragment() {
        // Required empty public constructor
    }

    public static Record_Fragment newInstance() {
        return new Record_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recordViewModel = new ViewModelProvider(requireActivity()).get(Record_ViewModel.class);
        Log.i(TAG, "viewmodel: " + recordViewModel);

        final Observer<Record_ViewModel.RecordInfo> recordInfoObserver = recordInfo -> {
            String timerText = (recordInfo.timerText != null) ? recordInfo.timerText : requireContext().getResources().getString(R.string.default_timer_text);
            String metersText = (recordInfo.metersText != null) ? recordInfo.metersText : requireContext().getResources().getString(R.string.default_meters_text);
            String elevationText = (recordInfo.elevationText != null) ? recordInfo.elevationText : requireContext().getResources().getString(R.string.default_meters_text);

            recordTimeView.setText(timerText);
            distanceView.setText(metersText);
            elevationView.setText(elevationText);

            SharedPreferences totMetersPreferences = requireContext().getSharedPreferences(TOTAL_METERS_SHAREDPREF, Context.MODE_PRIVATE);
            float totMeters = totMetersPreferences.getFloat (TOTAL_METERS_VAL, 0) + recordInfo.updateMeters;

            SharedPreferences.Editor totMetersEditor = totMetersPreferences.edit();
            totMetersEditor.putFloat(TOTAL_METERS_VAL, totMeters);

            totMetersEditor.apply();

            Log.i(TAG, "Total meters: " + totMeters);
        };

        recordViewModel.getRecordInfo(getContext()).observe(this, recordInfoObserver);
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

        recordTimeView = view.findViewById(R.id.record_time_view);
        distanceView = view.findViewById(R.id.distance_view);
        elevationView = view.findViewById(R.id.elevation_view);

        ImageButton saveRecordButton = view.findViewById(R.id.button_save_record);
        ImageButton playRecordButton = view.findViewById(R.id.button_start_record);
        ImageButton stopRecordButton = view.findViewById(R.id.button_stop_record);

        MaterialAlertDialogBuilder dialogBuilder =  new MaterialAlertDialogBuilder(requireActivity());
        dialogBuilder.setTitle(R.string.save_dialog_title);

        View inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.save_record_dialog, (ViewGroup) getView(), false);

        saveRecordButton.setOnClickListener(v -> {
            dialogBuilder.setPositiveButton(R.string.save, (dialog, which) -> {
                        String recordName = ((EditText) inflatedView.findViewById(R.id.save_record_input_text)).getText().toString();

                        recordViewModel.buttonSaveClicked(recordName);

                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                    .setView(inflatedView);

            dialogBuilder.setOnDismissListener(dialog -> ((ViewGroup) inflatedView.getParent()).removeView(inflatedView))
                    .show();
        });

        playRecordButton.setOnClickListener(v -> {
            if (!recordViewModel.buttonPlayClicked()) {
                //Snackbar snackbar =

                Snackbar.make(requireActivity().findViewById(R.id.container_main_activity), R.string.gps_not_enabled, Snackbar.LENGTH_LONG)
                        .setAction(R.string.ok, sview -> {})
                        .show();

                return;
            }

            if (recordViewModel.isRecordPaused())
                playRecordButton.setImageResource(R.drawable.ic_round_play_circle_24);
            else
                playRecordButton.setImageResource(R.drawable.ic_baseline_pause_circle_24);
        });

        stopRecordButton.setOnClickListener(v -> {
            recordViewModel.buttonStopClicked();
            playRecordButton.setImageResource(R.drawable.ic_round_play_circle_24);
        });
    }
}