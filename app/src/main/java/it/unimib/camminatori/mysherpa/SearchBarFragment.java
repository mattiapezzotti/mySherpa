package it.unimib.camminatori.mysherpa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchBarFragment extends Fragment {

    public SearchBarFragment() {
        // Required empty public constructor
    }

    public static SearchBarFragment newInstance() {
        SearchBarFragment searchBarFragment = new SearchBarFragment();
        return searchBarFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_bar, container, false);
    }
}