package it.unimib.camminatori.mysherpa;

import android.annotation.SuppressLint;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.viewmodel.RecordViewModel;


public class FavRecordsRecyclerViewAdapter extends RecyclerView.Adapter<FavRecordsRecyclerViewAdapter.ViewHolder> {
    final private String TAG = "FavRecordsRecyclerViewAdapter";

    private ArrayList<RecordViewModel.SaveRecordInfo> localFavData;
    private final ArrayList<RecordViewModel.SaveRecordInfo> localFavDataBkp;
    private OnItemsChangedListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView locationTextView;
        private final TextView dateTextView;
        private final TextView distanceTextView;
        private final TextView totalTimeTextView;
        private final TextView noBookmarksTextView;
        private final FrameLayout layoutExpandView;
        private final ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);


            this.locationTextView = view.findViewById(R.id.fav_location_text_view);
            this.dateTextView = view.findViewById(R.id.fav_date_text_view);
            this.distanceTextView = view.findViewById(R.id.traveled_record_distance_text_view);
            this.totalTimeTextView = view.findViewById(R.id.total_record_time_text_view);
            this.noBookmarksTextView = view.findViewById(R.id.no_bookmarks_text_view);
            this.deleteButton = view.findViewById(R.id.fav_delete_button);
            this.layoutExpandView = view.findViewById(R.id.save_record_click_layout);
        }

        public TextView getLocationTextView() {
            return this.locationTextView;
        }

        public TextView getDateTextView() {
            return this.dateTextView;
        }

        public TextView getDistanceTextView() {
            return this.distanceTextView;
        }

        public TextView getTotalTimeTextView() {
            return this.totalTimeTextView;
        }

        public TextView getNoBookmarksTextView() {
            return this.noBookmarksTextView;
        }

        public FrameLayout getLayoutExpandView() {
            return this.layoutExpandView;
        }

        public ImageButton getDeleteButton() {
            return this.deleteButton;
        }
    }

    public FavRecordsRecyclerViewAdapter(ArrayList<RecordViewModel.SaveRecordInfo> data) {
        if (data == null)
            localFavData = new ArrayList<>();
        else
            localFavData = data;

        localFavDataBkp = new ArrayList<>();
        localFavDataBkp.addAll(localFavData);
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
        TextView distanceView = viewHolder.getDistanceTextView();
        TextView totalTimeView = viewHolder.getTotalTimeTextView();
        FrameLayout expandView = viewHolder.getLayoutExpandView();


        viewHolder.getDateTextView().setText(localFavData.get(position).dateString);
        viewHolder.getLocationTextView().setText(localFavData.get(position).locationString);
        distanceView.setText(String.format("%s %s", viewHolder.itemView.getContext().getResources().getString(R.string.traveled_distance_prefix), distanceStringFormat(localFavData.get(position).metersDistance)));
        totalTimeView.setText(String.format("%s %s", viewHolder.itemView.getContext().getResources().getString(R.string.traveled_distance_prefix), timeStringFormat(localFavData.get(position).millisecondsTime)));

        viewHolder.getDeleteButton().setOnClickListener(v -> {
            localFavData = RecordViewModel.removeRecord(viewHolder.getAdapterPosition());
            notifyItemRemoved(viewHolder.getAdapterPosition());
            localFavDataBkp.clear();
            localFavDataBkp.addAll(localFavData);

            callListener();
        });

        expandView.setOnClickListener(v -> {
            if (distanceView.getVisibility() == View.GONE) {
                distanceView.setVisibility(View.VISIBLE);
                totalTimeView.setVisibility(View.VISIBLE);
            } else {
                distanceView.setVisibility(View.GONE);
                totalTimeView.setVisibility(View.GONE);
            }
        });

        callListener();
    }

    @Override
    public int getItemCount() {
        if (localFavData == null)
            return 0;


        return localFavData.size();
    }

    @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
    public void filter(CharSequence sequence) {
        Log.i(TAG, "data backup: " + localFavDataBkp.toString());

        ArrayList<RecordViewModel.SaveRecordInfo> tmp = new ArrayList<>();

        if (!TextUtils.isEmpty(sequence)) {
            for (RecordViewModel.SaveRecordInfo record : localFavDataBkp) {
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

    public void setOnItemsChangedListener(OnItemsChangedListener listener) {
        this.listener = listener;
    }

    private void callListener() {
        if (listener != null) {
            listener.onItemsChanged(getItemCount());
        }
    }

    private String distanceStringFormat(long meters) {
        String distanceString;

        if (meters >= 1000)
            distanceString = ((float)meters / 1000.0) + " Km";
        else
            distanceString = meters + " m";

        return distanceString;
    }

    private String timeStringFormat(long millisecond) {
        String timeString = "";
        long seconds = millisecond / 1000;
        long minutes = (seconds % 3600) / 60;
        long hours =  seconds / 3600;

        if (hours > 0)
            timeString += hours + " h  ";
        if (minutes > 0)
            timeString += minutes + " m  ";
        timeString += seconds + " s";

        return timeString;
    }

    public interface OnItemsChangedListener {
        void onItemsChanged(int size);
    }
}
