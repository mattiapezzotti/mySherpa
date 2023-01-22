package it.unimib.camminatori.mysherpa;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.viewmodel.RecordViewModel;


public class FavRecordsRecyclerViewAdapter extends RecyclerView.Adapter<FavRecordsRecyclerViewAdapter.ViewHolder> {
    final private String TAG = "FavRecordsRecyclerViewAdapter";

    private ArrayList<RecordViewModel.SaveRecordInfo> localFavData;
    private ArrayList<RecordViewModel.SaveRecordInfo> localFavDataBkp;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView locationTextView;
        private final TextView dateTextView;
        private final ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);

            this.locationTextView = (TextView) view.findViewById(R.id.fav_location_text_view);
            this.dateTextView = (TextView) view.findViewById(R.id.fav_date_text_view);
            this.deleteButton = (ImageButton) view.findViewById(R.id.fav_delete_button);
        }

        public TextView getLocationTextView() {
            return this.locationTextView;
        }

        public TextView getDateTextView() {
            return this.dateTextView;
        }

        public ImageButton getDeleteButton() {
            return this.deleteButton;
        }
    }

    public FavRecordsRecyclerViewAdapter(ArrayList<RecordViewModel.SaveRecordInfo> data) {
        this.localFavData = data;
        this.localFavDataBkp = new ArrayList<>();

        if (data != null)
            localFavDataBkp.addAll(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.saved_records_list_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getLocationTextView().setText(localFavData.get(position).locationString);
        viewHolder.getDateTextView().setText(localFavData.get(position).dateString);

        viewHolder.getDeleteButton().setOnClickListener(v -> {
            localFavData = RecordViewModel.removeRecord(viewHolder.getAdapterPosition());
            notifyItemRemoved(viewHolder.getAdapterPosition());
            localFavDataBkp.clear();
            localFavDataBkp.addAll(localFavData);
        });
    }

    @Override
    public int getItemCount() {
        return localFavData.size();
    }

    private void deleteFavRecord(int position) {
    }

    public void filter(CharSequence sequence) {
        Log.i(TAG, "data backup: " + localFavDataBkp.toString());

        ArrayList<RecordViewModel.SaveRecordInfo> tmp = new ArrayList<>();

        if (!TextUtils.isEmpty(sequence)) {
            for (RecordViewModel.SaveRecordInfo record : localFavData) {
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
}
