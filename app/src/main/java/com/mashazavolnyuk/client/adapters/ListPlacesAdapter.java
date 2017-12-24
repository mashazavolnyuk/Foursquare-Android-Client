package com.mashazavolnyuk.client.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ListPlacesAdapter extends RecyclerView.Adapter<ListPlacesAdapter.HolderAdapter>{

    @Override
    public ListPlacesAdapter.HolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ListPlacesAdapter.HolderAdapter holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class HolderAdapter extends RecyclerView.ViewHolder {
        public HolderAdapter(View itemView) {
            super(itemView);
        }
    }
}
