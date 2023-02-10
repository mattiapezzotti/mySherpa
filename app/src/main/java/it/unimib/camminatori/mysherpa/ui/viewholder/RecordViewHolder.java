package it.unimib.camminatori.mysherpa.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import it.unimib.camminatori.mysherpa.R;

public class RecordViewHolder extends RecyclerView.ViewHolder {
    private final TextView locationTextView;
    private final TextView dateTextView;
    private final TextView distanceTextView;
    private final TextView totalTimeTextView;
    private final TextView noBookmarksTextView;
    private final FrameLayout layoutExpandView;
    private final ImageButton deleteButton;
    private final ImageButton shareButton;
    private final Context rootContext;
    private final ImageButton exploreButton;

    public RecordViewHolder(View view) {
        super(view);


        this.locationTextView = view.findViewById(R.id.fav_location_text_view);
        this.dateTextView = view.findViewById(R.id.fav_date_text_view);
        this.distanceTextView = view.findViewById(R.id.traveled_record_distance_text_view);
        this.totalTimeTextView = view.findViewById(R.id.total_record_time_text_view);
        this.noBookmarksTextView = view.findViewById(R.id.no_bookmarks_text_view);
        this.deleteButton = view.findViewById(R.id.fav_delete_button);
        this.layoutExpandView = view.findViewById(R.id.save_record_click_layout);
        this.shareButton = view.findViewById(R.id.fav_share_button);
        this.rootContext = view.getContext();
        this.exploreButton = view.findViewById(R.id.fav_explore_button);
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

    public ImageButton getShareButton() {
        return this.shareButton;
    }

    public Context getRootContext() {
        return rootContext;
    }

    public ImageButton getExploreButton() {
        return this.exploreButton;
    }
}
