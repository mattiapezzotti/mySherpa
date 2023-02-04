package it.unimib.camminatori.mysherpa.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.databinding.FragmentRecordBinding;
import it.unimib.camminatori.mysherpa.viewmodel.RecordViewModel;

public class Record_Fragment extends Fragment {
    final private String TAG = "fragment_record";

    // Shared preferences key names
    final static public String TOTAL_METERS_SHAREDPREF = "TOTAL_METERS";
    final static public String TOTAL_METERS_VAL = "TOTAL_METERS_VAL";

    private FragmentRecordBinding binding;

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);

        final Observer<RecordViewModel.RecordInfo> recordInfoObserver = recordInfo -> {
            String timerText = (recordInfo.timerText != null) ? recordInfo.timerText : requireContext().getResources().getString(R.string.default_timer_text);
            String metersText = (recordInfo.metersText != null) ? recordInfo.metersText : requireContext().getResources().getString(R.string.default_meters_text);
            String elevationText = (recordInfo.elevationText != null) ? recordInfo.elevationText : requireContext().getResources().getString(R.string.default_meters_text);

            binding.recordTimeView.setText(timerText);
            binding.distanceView.setText(metersText);
            binding.elevationView.setText(elevationText);

            SharedPreferences totMetersPreferences = requireContext().getSharedPreferences(TOTAL_METERS_SHAREDPREF, Context.MODE_PRIVATE);
            float totMeters = totMetersPreferences.getFloat (TOTAL_METERS_VAL, 0) + recordInfo.updateMeters;

            SharedPreferences.Editor totMetersEditor = totMetersPreferences.edit();
            totMetersEditor.putFloat(TOTAL_METERS_VAL, totMeters);

            totMetersEditor.apply();
        };
        recordViewModel.getRecordInfo(getContext()).observe(this, recordInfoObserver);

        MaterialAlertDialogBuilder dialogBuilder =  new MaterialAlertDialogBuilder(requireActivity());
        dialogBuilder.setTitle(R.string.save_dialog_title);

        View inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.save_record_dialog, (ViewGroup) getView(), false);

        binding.buttonSaveRecord.setOnClickListener(v -> {
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

        binding.buttonStartRecord.setOnClickListener(v -> {
            if (!recordViewModel.buttonPlayClicked()) {
                Snackbar.make(requireActivity().findViewById(R.id.container_main_activity), R.string.gps_not_enabled, Snackbar.LENGTH_LONG)
                        .setAction(R.string.ok, sview -> {})
                        .show();

                return;
            }

            updateButtons();
        });

        binding.buttonStopRecord.setOnClickListener(v -> {
            recordViewModel.buttonStopClicked();

            updateButtons();
        });

        updateButtons();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.d(TAG, "onSaveInstanceState");
        SavedRecords_Fragment.saveFavRecords(requireContext(), recordViewModel);
    }

    private void updateButtons() {
        int color;
        boolean enabled;

        if (recordViewModel.isRecordPaused() || !recordViewModel.isRecordStarted()) {
            binding.buttonStartRecord.setImageResource(R.drawable.ic_round_play_circle_24);
        }
        else {
            binding.buttonStartRecord.setImageResource(R.drawable.ic_baseline_pause_circle_24);
        }

        if (recordViewModel.isRecordStarted()) {
            enabled = true;
            color = R.color.button_stop_enabled;
        } else {
            enabled = false;
            color = R.color.button_stop_disabled;
        }

        binding.buttonStopRecord.setEnabled(enabled);
        binding.buttonStopRecord.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(color)));
    }
}