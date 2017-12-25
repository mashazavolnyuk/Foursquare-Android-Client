package com.mashazavolnyuk.client.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.data.Group;
import com.mashazavolnyuk.client.data.Item;
import com.mashazavolnyuk.client.data.Item__;
import com.mashazavolnyuk.client.data.Photos;
import com.mashazavolnyuk.client.data.Price;
import com.mashazavolnyuk.client.data.Venue;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;


public class ListPlacesAdapter extends RecyclerView.Adapter<ListPlacesAdapter.HolderAdapter> {

    private Context context;
    private Group groupList;

    public ListPlacesAdapter(Context context, Group groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public ListPlacesAdapter.HolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new HolderAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(ListPlacesAdapter.HolderAdapter holder, int position) {
        Item item = groupList.getItems().get(position);
        Venue venue = item.getVenue();
        Price price = venue.getPrice();
        String valuePrice = price != null ? price.getCurrency() : "";
        Item__ itemPhoto = venue.getPhotos().getGroups().get(0).getItems().get(0);
        if (itemPhoto != null) {
            String path = itemPhoto.getPrefix() + "500x500" + itemPhoto.getSuffix();
            Uri uri = Uri.parse(path);
            Picasso.with(context).load(uri).error(R.drawable.ic_error_image).into(holder.photoPlace);
        }
        holder.firstPositionText.setText(venue.getName());
        holder.secondPositionText.setText(venue.getCategories().get(0).getName() + "," + valuePrice);
        String kmValue = getStringDistanceByValue(venue.getLocation().getDistance());
        String address = venue.getLocation().getAddress();
        String valueForThirdPosition = kmValue+","+address;
        holder.thirdPositionText.setText(valueForThirdPosition);
        holder.rating.setText(String.format("%s",venue.getRating().toString()));
        holder.rating.setBackgroundColor(Color.parseColor("#" + venue.getRatingColor()));
    }

    private String getStringDistanceByValue(Integer value) {
        Double distance = value / 1000.0;
        return String.format(Locale.ENGLISH,"%.2f km", distance);
    }

    @Override
    public int getItemCount() {
        if (groupList != null) {
            return groupList.getItems().size();
        } else {
            return 0;
        }
    }

    class HolderAdapter extends RecyclerView.ViewHolder {
        ImageView photoPlace;
        TextView firstPositionText;
        TextView secondPositionText;
        TextView thirdPositionText;
        TextView rating;

        public HolderAdapter(View view) {
            super(view);
            photoPlace = view.findViewById(R.id.photoPlace);
            firstPositionText = view.findViewById(R.id.firstPositionText);
            secondPositionText = view.findViewById(R.id.secondPositionText);
            thirdPositionText = view.findViewById(R.id.thirdPositionText);
            rating = view.findViewById(R.id.rating);
        }
    }
}
