package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.camminatori.mysherpa.R;

public class Explore_Fragment extends Fragment {



    public Explore_Fragment() {
        // Required empty public constructor
    }

    public static Explore_Fragment newInstance(String param1, String param2) {
        return new Explore_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }
}