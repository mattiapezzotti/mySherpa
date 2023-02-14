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

    /**
     * L'attributo localFavData è un'ArrayList con all'interno le località salvate.
     */
    private ArrayList<SavedLocation> localFavData;

    /**
     * L'attributo localFavDataBku è un'ArrayList che contiene una copia delle località salvate,
     * perchè la search bar utilizza direttamente la localFavData associata all'Adapter.
     */
    private ArrayList<SavedLocation> localFavDataBku;
    private OnItemsChangedListener changedListener;
    private SavedLocation_Fragment savedLocationFragment;


    public FavLocationRecyclerViewAdapter(SavedLocation_Fragment savedLocationFragment, ArrayList<SavedLocation> data) {
        this.localFavData = data;
        this.savedLocationFragment = savedLocationFragment;

        // Crea la favDataBku contenente una copia delle località salvate.
        localFavDataBku = new ArrayList<>();
        localFavDataBku.addAll(localFavData);
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.saved_location_list_layout, viewGroup, false);

        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder locationViewHolder, int position) {
        DecimalFormat numberFormat = new DecimalFormat("#.000000");
        String lat = numberFormat.format(localFavData.get(position).getLatitude());
        String lon = numberFormat.format(localFavData.get(position).getLongitude());
        String locationLatLon = lat + ", " + lon;

        locationViewHolder.getLocationTextView().setText(localFavData.get(position).getDisplayName());
        locationViewHolder.getLocationLatLon().setText(locationLatLon);

        locationViewHolder.getDeleteButton().setOnClickListener(v -> {

            //Identifica l'elemento da rimuovere nella lista locale filtrata
            SavedLocation locationToRemove = savedLocationFragment.getLocation(locationViewHolder.getAdapterPosition());
            localFavData.remove(locationToRemove);


            //Rimuove l'elemento dalla lista completa
            localFavDataBku = savedLocationFragment.removeLocationFromList(localFavDataBku, locationToRemove);

            notifyItemRemoved(locationViewHolder.getAdapterPosition());
            callChangeListener();
        });


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

    /**
     * Il metodo {@link #filter(CharSequence)} filtra la lista delle località in base al testo
     * passato in sequence.
     *
     * @param sequence
     */
    public void filter(CharSequence sequence) {
        ArrayList<SavedLocation> tmp = new ArrayList<>();

        if (!TextUtils.isEmpty(sequence)) {
            //Filtra le località in base alle sequenze di caratteri passati.
            for (SavedLocation record : localFavDataBku) {
                if (record.getDisplayName().toLowerCase().contains(sequence)) {
                    tmp.add(record);
                }
            }
        } else {
            //Se la sequenza è vuota caricara tutta la lista delle località.
            tmp.addAll(localFavDataBku);
        }

        //Aggiorna la lista utilizzata dall'adapter in base al risultato della ricerca.
        localFavData.clear();
        localFavData.addAll(tmp);
        notifyDataSetChanged();

        tmp.clear();
    }

    public interface OnItemsChangedListener {
        void onItemsChanged(int size);
    }
}
