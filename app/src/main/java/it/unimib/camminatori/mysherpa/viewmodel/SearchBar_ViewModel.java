package it.unimib.camminatori.mysherpa.viewmodel;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.network.geocoding.GeocodingAPI;

public class SearchBar_ViewModel {
    private final TextInputLayout searchBar;
    private final TextInputEditText searchBarText;
    private GeocodingAPI geocoder;

    public SearchBar_ViewModel(TextInputLayout sb){
        this.searchBar = sb;
        this.searchBarText = sb.findViewById(R.id.search_bar_text);
        geocoder = geocoder.getInstance();
        init();
    }


    private void init(){
        searchBarText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    try {
                        //geocoder.searchGeocoding(String.valueOf(searchBarText.getText()).trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
