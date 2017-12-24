package com.mashazavolnyuk.client.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.data.Venue;

import java.util.List;


public class ListPlacesAdapter extends RecyclerView.Adapter<ListPlacesAdapter.HolderAdapter> {

    private Context context;
    private List<Venue> venues;

    public ListPlacesAdapter(Context context, List<Venue> venues) {
        this.context = context;
        this.venues = venues;
    }

    @Override
    public ListPlacesAdapter.HolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new HolderAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(ListPlacesAdapter.HolderAdapter holder, int position) {
        holder.namePlace.setText(venues.get(position).getName());
       // holder.nameCategory.setText(venues.get(position));
    }

    @Override
    public int getItemCount() {
        if (venues != null) {
            return venues.size();
        } else {
            return 0;
        }
    }

    class HolderAdapter extends RecyclerView.ViewHolder {
        TextView namePlace;

        public HolderAdapter(View view) {
            super(view);
            namePlace = view.findViewById(R.id.namePlace);
        }
    }
}
