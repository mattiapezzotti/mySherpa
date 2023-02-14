package it.unimib.camminatori.mysherpa.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.materialswitch.MaterialSwitch;

import it.unimib.camminatori.mysherpa.R;

public class Settings_Fragment extends Fragment {

    private MaterialSwitch switcherDark, switcherEnglish;
    private Boolean nightMode, english;
    private SharedPreferences sharedPreferences;
    private ImageButton backButton;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switcherDark = view.findViewById(R.id.switchDark);
        switcherEnglish = view.findViewById(R.id.switchEnglish);
        sharedPreferences = requireActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
        english = sharedPreferences.getBoolean("english", false);

        switcherDark.setOnClickListener(v -> {
            editor = sharedPreferences.edit();
            nightMode = !nightMode;
            if (!nightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("night", false);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("night", true);
            }
            editor.apply();
        });

        if (english) {
            switcherEnglish.setChecked(true);
        }

        switcherEnglish.setOnClickListener(v -> {
            english = !english;
            editor = sharedPreferences.edit();
            if (english) {
                setLocale("en");
                editor.putBoolean("english", true);
            } else {
                setLocale("it");
                editor.putBoolean("english", false);
            }
            editor.apply();
        });

        backButton = view.findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(this.getActivity().findViewById(R.id.nav_host_fragment)).popBackStack();
            Navigation.findNavController(this.getActivity().findViewById(R.id.nav_host_fragment)).navigate(R.id.fragment_profile);
        });

    }

    private void setLocale(String lang) {
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(lang);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }

}
