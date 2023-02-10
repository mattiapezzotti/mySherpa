package it.unimib.camminatori.mysherpa.ui.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import it.unimib.camminatori.mysherpa.R;

public class LocationViewHolder extends RecyclerView.ViewHolder {

    private final ImageButton openButton;
    private final TextView locationView;
    private final ImageButton deleteButton;
    private final TextView noLocationsTextView;
    private final TextView locationLatLon;
    ConstraintLayout fragment_saved_location;


    public LocationViewHolder(View view) {
        super(view);

        this.locationView = view.findViewById(R.id.fav_location_text_view);
        this.openButton = view.findViewById(R.id.fav_open_button);
        this.locationLatLon = view.findViewById(R.id.fav_location_lat_lon);
        this.deleteButton = view.findViewById(R.id.fav_delete_button);
        this.noLocationsTextView = view.findViewById(R.id.no_location_text_view);
        this.fragment_saved_location = view.findViewById(R.id.saved_location_list);
    }

    public void bind(String text) {
        locationView.setText(text);
    }

    public TextView getLocationTextView() {
        return this.locationView;
    }

    public TextView getLocationLatLon() {
        return this.locationLatLon;
    }

    public ImageButton getDeleteButton() {
        return this.deleteButton;
    }

    public ImageButton getOpenButton() {
        return this.openButton;
    }

    public TextView getNoLocationTextView() {
        return this.noLocationsTextView;
    }

}
