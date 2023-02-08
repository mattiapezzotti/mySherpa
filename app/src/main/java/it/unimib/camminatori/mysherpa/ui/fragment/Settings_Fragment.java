package it.unimib.camminatori.mysherpa.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Locale;

import it.unimib.camminatori.mysherpa.R;

public class Settings_Fragment extends Fragment {

    SwitchMaterial switcherDark, switcherEnglish;
    Boolean nightMode, English;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadLocale();

        /*switcherDark = view.findViewById(R.id.switchDark);
        switcherEnglish = view.findViewById(R.id.switchEnglish);

        sharedPreferences = requireActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);

        if(nightMode){
            switcherDark.setChecked(true);
        }

        switcherDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nightMode){

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);

                }else{

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();
            }
        });

        if(English){
            switcherEnglish.setChecked(true);
        }

        switcherEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(English){
                    setLocale("en");
                    getParentFragmentManager()
                            .beginTransaction()
                            .detach(Settings_Fragment.this)
                            .attach(Settings_Fragment.this)
                            .commit();

                }else{
                    setLocale("it");
                    getParentFragmentManager()
                            .beginTransaction()
                            .detach(Settings_Fragment.this)
                            .attach(Settings_Fragment.this)
                            .commit();

                }
            }
        });*/

    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        //salvo su sharedPreferences
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

}
