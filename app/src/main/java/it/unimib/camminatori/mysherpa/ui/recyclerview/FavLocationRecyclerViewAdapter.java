package it.unimib.camminatori.mysherpa.ui.recyclerview;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
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


public class FavLocationRecyclerViewAdapter extends RecyclerView.Adapter<FavLocationRecyclerViewAdapter.ViewHolder>{
    final private String TAG = "FavLocationRecyclerViewAdapter";

    private ArrayList<Data_Location_ViewModel.SavedLocationInfo> localFavData;
    private final ArrayList<Data_Location_ViewModel.SavedLocationInfo> localFavDataBkp;
    private OnItemsChangedListener changedListener;
    private SavedLocation_Fragment savedLocationFragment;


    public FavLocationRecyclerViewAdapter(SavedLocation_Fragment savedLocationFragment, ArrayList<Data_Location_ViewModel.SavedLocationInfo> data) {
        this.localFavData = data;
        this.savedLocationFragment = savedLocationFragment;

        localFavDataBkp = new ArrayList<>();
        localFavDataBkp.addAll(localFavData);
    }

    public FavLocationRecyclerViewAdapter(ArrayList<Data_Location_ViewModel.SavedLocationInfo> data) {
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

        viewHolder.getLocationTextView().setText(localFavData.get(position).locationString);

        viewHolder.getDeleteButton().setOnClickListener(v -> {
            ArrayList<Data_Location_ViewModel.SavedLocationInfo> favList = savedLocationFragment.RemoveLocation(viewHolder.getAdapterPosition());
            notifyItemRemoved(viewHolder.getAdapterPosition());

            localFavDataBkp.clear();
            localFavDataBkp.addAll(localFavData);

            callChangeListener();
        });

        // OnClick Image
        viewHolder.getForestImage().setOnClickListener(v -> {
            savedLocationFragment.OpenLocation(viewHolder.getAdapterPosition());
        });

        // OnClick location
        viewHolder.getLocationTextView().setOnClickListener(v -> {
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

    @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
    public void filter(CharSequence sequence) {
        Log.i(TAG, "data backup: " + localFavDataBkp.toString());

        ArrayList<Data_Location_ViewModel.SavedLocationInfo> tmp = new ArrayList<>();

        if (!TextUtils.isEmpty(sequence)) {
            for (Data_Location_ViewModel.SavedLocationInfo record : localFavDataBkp) {
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

        private final ImageView forestImage;
        private final TextView locationView;
        private final ImageButton deleteButton;
        private final TextView noLocationsTextView;
        ConstraintLayout fragment_saved_location;


        public ViewHolder(View view) {
            super(view);

            this.forestImage = (ImageView) view.findViewById(R.id.imageViewForest);
            this.locationView = (TextView) view.findViewById(R.id.fav_location_text_view);
            this.deleteButton = (ImageButton) view.findViewById(R.id.fav_delete_button);
            this.noLocationsTextView = (TextView) view.findViewById(R.id.no_location_text_view);
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

        public TextView getNoLocationTextView() {
            return this.noLocationsTextView;
        }

    }

    public interface OnItemsChangedListener {
        void onItemsChanged(int size);
    }
}
