package it.unimib.camminatori.mysherpa.ui.recyclerview;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.SavedLocation;
import it.unimib.camminatori.mysherpa.ui.fragment.SavedLocation_Fragment;
import it.unimib.camminatori.mysherpa.ui.viewholder.LocationViewHolder;


public class FavLocationRecyclerViewAdapter extends RecyclerView.Adapter<LocationViewHolder> {
    final private String TAG = "FavLocationRecyclerViewAdapter";

    private ArrayList<SavedLocation> localFavData;
    private ArrayList<SavedLocation> localFavDataBku;
    private OnItemsChangedListener changedListener;
    private SavedLocation_Fragment savedLocationFragment;


    public FavLocationRecyclerViewAdapter(SavedLocation_Fragment savedLocationFragment, ArrayList<SavedLocation> data) {
        this.localFavData = data;
        this.savedLocationFragment = savedLocationFragment;

        localFavDataBku = new ArrayList<>();
        localFavDataBku.addAll(localFavData);
    }

    @NonNull
    @Override
    // Alla creazione della vista
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.saved_location_list_layout, viewGroup, false);

        return new LocationViewHolder(view);
    }

    @Override
    // Binding
    public void onBindViewHolder(@NonNull LocationViewHolder locationViewHolder, int position) {
        DecimalFormat numberFormat = new DecimalFormat("#.000000");
        String lat = numberFormat.format(localFavData.get(position).getLatitude());
        String lon = numberFormat.format(localFavData.get(position).getLongitude());
        String locationLatLon = lat + ", " + lon;

        locationViewHolder.getLocationTextView().setText(localFavData.get(position).getDisplayName());
        locationViewHolder.getLocationLatLon().setText(locationLatLon);

        locationViewHolder.getDeleteButton().setOnClickListener(v -> {
            // Identifica elemento da rimuovere nella lista locale filtrata
            SavedLocation locationToRemove = savedLocationFragment.getLocation(locationViewHolder.getAdapterPosition());
            localFavData.remove(locationToRemove);

            // Rimuovi elemento da lista completa
            localFavDataBku = savedLocationFragment.removeLocationFromList(localFavDataBku, locationToRemove);

            notifyItemRemoved(locationViewHolder.getAdapterPosition());
            callChangeListener();
        });

        // OnClick Image
        locationViewHolder.getOpenButton().setOnClickListener(v -> {
            savedLocationFragment.openLocation(locationViewHolder.getAdapterPosition());
        });

        callChangeListener();
    }

    public void setOnItemsChangedListener(OnItemsChangedListener changedListener) {
        this.changedListener = changedListener;
    }

    private void callChangeListener() {
        if (changedListener != null) {
            changedListener.onItemsChanged(getItemCount());
        }
    }

    @Override
    public int getItemCount() {
        if (localFavData == null)
            return 0;
        return localFavData.size();
    }

    public void filter(CharSequence sequence) {
        ArrayList<SavedLocation> tmp = new ArrayList<>();

        if (!TextUtils.isEmpty(sequence)) {
            for (SavedLocation record : localFavDataBku) {
                if (record.getDisplayName().toLowerCase().contains(sequence)) {
                    tmp.add(record);
                }
            }
        } else {
            tmp.addAll(localFavDataBku);
        }

        localFavData.clear();
        localFavData.addAll(tmp);
        notifyDataSetChanged();

        tmp.clear();
    }

    public interface OnItemsChangedListener {
        void onItemsChanged(int size);
    }
}
