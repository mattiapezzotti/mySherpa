package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.viewmodel.SearchBar_ViewModel;

public class SearchBar_Fragment extends Fragment {

    private SearchBar_ViewModel sbm;

    public SearchBar_Fragment() {
        // Required empty public constructor
    }

    public static SearchBar_Fragment newInstance() {
        return new SearchBar_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_bar, container, false);
        this.sbm = new SearchBar_ViewModel(view.findViewById(R.id.search_bar_layout));
        return view;
    }
}