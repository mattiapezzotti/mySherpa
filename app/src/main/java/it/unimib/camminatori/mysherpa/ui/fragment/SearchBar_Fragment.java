package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.viewmodel.Location_ViewModel;

public class SearchBar_Fragment extends Fragment {
    private Location_ViewModel location_viewModel;
    private TextInputEditText searchBarText;
    private TextInputLayout searchBarLayout;

    public SearchBar_Fragment() {
        // Required empty public constructor
    }

    public static SearchBar_Fragment newInstance() {
        return new SearchBar_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location_viewModel = new ViewModelProvider(requireParentFragment()).get(Location_ViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_bar, container, false);
        searchBarLayout = view.findViewById(R.id.search_bar_layout);
        searchBarText = searchBarLayout.findViewById(R.id.search_bar_text);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Metodo per evitare che ogni volta che si scriva parta l'action event
        // L'evento viene triggerato solo dopo aver premuto "invio" sulla tastiera
        searchBarText.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String input = String.valueOf(searchBarText.getText()).trim();
                if(input.length() > 0) {
                    location_viewModel.geocodePlace(input);
                    handled = true;
                }
                this.searchBarText.clearFocus();
                this.searchBarLayout.clearFocus();
            }
            return handled;
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        this.searchBarText.clearFocus();
        this.searchBarLayout.clearFocus();
    }
}