package it.unimib.camminatori.mysherpa.ui.recyclerview;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.SavedLocation;
import it.unimib.camminatori.mysherpa.ui.fragment.SavedLocation_Fragment;


public class FavLocationRecyclerViewAdapter extends RecyclerView.Adapter<FavLocationRecyclerViewAdapter.ViewHolder> {
    final private String TAG = "FavLocationRecyclerViewAdapter";

    private final ArrayList<SavedLocation> localFavData;
    private final ArrayList<SavedLocation> localFavDataBkp;
    private OnItemsChangedListener changedListener;
    private SavedLocation_Fragment savedLocationFragment;


    public FavLocationRecyclerViewAdapter(SavedLocation_Fragment savedLocationFragment, ArrayList<SavedLocation> data) {
        this.localFavData = data;
        this.savedLocationFragment = savedLocationFragment;

        localFavDataBkp = new ArrayList<>();
        localFavDataBkp.addAll(localFavData);
    }

    public FavLocationRecyclerViewAdapter(ArrayList<SavedLocation> data) {
        if (data == null)
            localFavData = new ArrayList<>();
        else
            localFavData = data;

        localFavDataBkp = new ArrayList<>();
        localFavDataBkp.addAll(localFavData);
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

        String lat = localFavData.get(position).lat + "";
        String lon = localFavData.get(position).lon + "";
        String locationLatLon = lat.substring(0, 8) + ", " + lon.substring(0, 8);

        viewHolder.getLocationTextView().setText(localFavData.get(position).locationString);
        viewHolder.getLocationLatLon().setText(locationLatLon);

        viewHolder.getDeleteButton().setOnClickListener(v -> {
            ArrayList<SavedLocation> favList = savedLocationFragment.RemoveLocation(viewHolder.getAdapterPosition());
            notifyItemRemoved(viewHolder.getAdapterPosition());

            localFavDataBkp.clear();
            localFavDataBkp.addAll(localFavData);

            callChangeListener();
        });

        // OnClick Image
        viewHolder.getOpenButton().setOnClickListener(v -> {
            savedLocationFragment.OpenLocation(viewHolder.getAdapterPosition());
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
        return localFavData.size();
    }


    public void filter(CharSequence sequence) {
        ArrayList<SavedLocation> tmp = new ArrayList<>();

        if (!TextUtils.isEmpty(sequence)) {
            for (SavedLocation record : localFavDataBkp) {
                if (record.locationString.toLowerCase().contains(sequence)) {
                    tmp.add(record);
                }
            }
        } else {
            tmp.addAll(localFavDataBkp);
        }

        localFavData.clear();
        localFavData.addAll(tmp);
        notifyDataSetChanged();

        tmp.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageButton openButton;
        private final TextView locationView;
        private final ImageButton deleteButton;
        private final TextView noLocationsTextView;
        private final TextView locationLatLon;
        ConstraintLayout fragment_saved_location;


        public ViewHolder(View view) {
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

    public interface OnItemsChangedListener {
        void onItemsChanged(int size);
    }
}
