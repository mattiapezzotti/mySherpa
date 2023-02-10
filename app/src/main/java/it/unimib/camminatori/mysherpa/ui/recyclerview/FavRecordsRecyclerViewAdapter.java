package it.unimib.camminatori.mysherpa.ui.recyclerview;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.SavedLocation;
import it.unimib.camminatori.mysherpa.model.SavedRecord;
import it.unimib.camminatori.mysherpa.model.SavedRecordDAO;
import it.unimib.camminatori.mysherpa.ui.viewholder.RecordViewHolder;
import it.unimib.camminatori.mysherpa.viewmodel.Record_ViewModel;


public class FavRecordsRecyclerViewAdapter extends RecyclerView.Adapter<RecordViewHolder> {
    final private String TAG = "FavRecordsRecyclerViewAdapter";

    private OnItemsChangedListener changedListener;
    private OnShareClickedListener shareClickedListener;
    private OnDeleteClickedListener deleteClickedListener;
    private onExploreClickedListener exploreClickedListener;
    private Record_ViewModel recordViewModel;

    private ArrayList<SavedRecord> localFavDataBkp;
    private ArrayList<SavedRecord> localFavData;

    public FavRecordsRecyclerViewAdapter(Record_ViewModel recordViewModel) {
        localFavData = recordViewModel.getSavedRecord();
        this.recordViewModel = recordViewModel;
        localFavDataBkp = new ArrayList<>();
        localFavDataBkp.addAll(localFavData);
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.saved_records_list_layout, viewGroup, false);

        return new RecordViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecordViewHolder recordViewHolder, final int position) {
        TextView distanceView = recordViewHolder.getDistanceTextView();
        TextView totalTimeView = recordViewHolder.getTotalTimeTextView();
        FrameLayout expandView = recordViewHolder.getLayoutExpandView();

        recordViewHolder.getDateTextView().setText(localFavData.get(position).dateString);
        recordViewHolder.getLocationTextView().setText(localFavData.get(position).locationString);
        distanceView.setText(
                recordViewHolder.itemView.getContext().getResources().getString(
                        R.string.default_distance_description)
                        + " " + distanceStringFormat(localFavData.get(position).metersDistance));
        totalTimeView.setText(
                recordViewHolder.itemView.getContext().getResources().getString(
                        R.string.traveled_distance_prefix)
                        + " " + timeStringFormat(localFavData.get(position).millisecondsTime));

        recordViewHolder.getDeleteButton().setOnClickListener(v -> {
            if (deleteClickedListener != null)
                deleteClickedListener.onDeleteClickedListener(recordViewHolder.getAdapterPosition());

            localFavData = recordViewModel.removeRecord(recordViewHolder.getAdapterPosition());
            notifyItemRemoved(recordViewHolder.getAdapterPosition());

            localFavDataBkp.clear();
            localFavDataBkp.addAll(localFavData);

            callChangeListener();
        });

        recordViewHolder.getShareButton().setOnClickListener(v -> {
            if (shareClickedListener != null) {
                shareClickedListener.onShareClicked(recordViewHolder.getAdapterPosition());
            }
        });

        recordViewHolder.getExploreButton().setOnClickListener(v -> {
            if (exploreClickedListener != null)
                exploreClickedListener.onExploreClicked(recordViewHolder.getAdapterPosition());
        });

        expandView.setOnClickListener(v -> {
            if (distanceView.getVisibility() == View.GONE) {
                distanceView.setVisibility(View.VISIBLE);
                totalTimeView.setVisibility(View.VISIBLE);
                recordViewHolder.getShareButton().setVisibility(View.VISIBLE);
            } else {
                distanceView.setVisibility(View.GONE);
                totalTimeView.setVisibility(View.GONE);
                recordViewHolder.getShareButton().setVisibility(View.GONE);
            }
        });

        callChangeListener();
    }

    @Override
    public int getItemCount() {
        if (localFavData == null)
            return 0;
        return localFavData.size();
    }

    public void filter(CharSequence sequence) {
        ArrayList<SavedRecord> tmp = new ArrayList<>();

        if (!TextUtils.isEmpty(sequence)) {
            for (SavedRecord record : localFavDataBkp) {
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

    public void setOnItemsChangedListener(OnItemsChangedListener changedListener) {
        this.changedListener = changedListener;
    }

    public void setOnShareClickedListener(OnShareClickedListener shareClickedListener) {
        this.shareClickedListener = shareClickedListener;
    }


    public void setOnDeleteClickedListener(OnDeleteClickedListener deleteClickedListener) {
        this.deleteClickedListener = deleteClickedListener;
    }

    public void setOnExploreClickedListener(onExploreClickedListener exploreClickedListener) {
        this.exploreClickedListener = exploreClickedListener;
    }

    private void callChangeListener() {
        if (changedListener != null) {
            changedListener.onItemsChanged(getItemCount());
        }
    }

    private String distanceStringFormat(long meters) {
        String distanceString;

        if (meters >= 1000)
            distanceString = ((float) meters / 1000.0) + " Km";
        else
            distanceString = meters + " m";

        return distanceString;
    }

    private String timeStringFormat(long millisecond) {
        String timeString = "";
        long seconds = millisecond / 1000;
        long minutes = (seconds % 3600) / 60;
        long hours = seconds / 3600;

        if (hours > 0)
            timeString += hours + " h  ";
        if (minutes > 0)
            timeString += minutes + " m  ";
        timeString += seconds % 60 + " s";

        return timeString;
    }

    public interface OnItemsChangedListener {
        void onItemsChanged(int size);
    }

    public interface OnShareClickedListener {
        void onShareClicked(int index);
    }


    public interface OnDeleteClickedListener {
        void onDeleteClickedListener(int index);
    }

    public interface onExploreClickedListener {
        void onExploreClicked(int index);
    }
}
