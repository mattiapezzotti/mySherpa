package it.unimib.camminatori.mysherpa;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.ui.fragment.SavedLocation_Fragment;
import it.unimib.camminatori.mysherpa.viewmodel.Data_Location_ViewModel;
import it.unimib.camminatori.mysherpa.viewmodel.Record_ViewModel;

public class FavLocationRecyclerViewAdapter extends RecyclerView.Adapter<FavLocationRecyclerViewAdapter.ViewHolder>{
    final private String TAG = "FavLocationRecyclerViewAdapter";

    private ArrayList<Data_Location_ViewModel.SavedLocationInfo> localFavData;
    private SavedLocation_Fragment savedLocationFragment;

    public FavLocationRecyclerViewAdapter(SavedLocation_Fragment savedLocationFragment, ArrayList<Data_Location_ViewModel.SavedLocationInfo> data) {
        this.localFavData = data;
        this.savedLocationFragment = savedLocationFragment;
    }

    @NonNull
    @Override
    // Alla creazione della vista
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.saved_location_list_layout, viewGroup, false);

        return new FavLocationRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    // Binding
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.getLocationTextView().setText(localFavData.get(position).locationString);

        viewHolder.getDeleteButton().setOnClickListener(v -> {
            Data_Location_ViewModel.removeRecord(viewHolder.getAdapterPosition());
            notifyItemRemoved(viewHolder.getAdapterPosition());
        });

        // OnClick Image
        viewHolder.getForestImage().setOnClickListener(v -> {
            Data_Location_ViewModel.SavedLocationInfo recordData = Data_Location_ViewModel.getRecord(viewHolder.getAdapterPosition());
            savedLocationFragment.onItemClick(recordData);
        });

        // OnClick location
        viewHolder.getLocationTextView().setOnClickListener(v -> {
            Data_Location_ViewModel.SavedLocationInfo recordData = Data_Location_ViewModel.getRecord(viewHolder.getAdapterPosition());
            savedLocationFragment.onItemClick(recordData);
        });
    }

    @Override
    public int getItemCount() {
        return localFavData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView forestImage;
        private final TextView locationView;
        private final ImageButton deleteButton;
        ConstraintLayout fragment_saved_location;

        public ViewHolder(View view) {
            super(view);

            this.forestImage = (ImageView) view.findViewById(R.id.imageViewForest);
            this.locationView = (TextView) view.findViewById(R.id.fav_location_text_view);
            this.deleteButton = (ImageButton) view.findViewById(R.id.fav_delete_button);
            this.fragment_saved_location = view.findViewById(R.id.saved_location_list);
        }

        public void bind(String text) {
            locationView.setText(text);
        }

        public ImageView getForestImage(){
            return this.forestImage;
        }

        public TextView getLocationTextView() {
            return this.locationView;
        }

        public ImageButton getDeleteButton() { return this.deleteButton; }
    }
}
