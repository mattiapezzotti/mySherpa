package it.unimib.camminatori.mysherpa;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavLocationRecyclerViewAdapter extends RecyclerView.Adapter {

    class Location_holder extends  RecyclerView.Adapter{
        private TextView fav_location_text_view;
        private TextView fav_temperature;
        private TextView fav_wind;
        private TextView fav_umidity;

        public Location_holder(View itemView){
            super(itemView);
            fav_location_text_view = itemView.findViewById(R.id.fav_location_text_view);
            fav_temperature
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

}
