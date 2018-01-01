package com.mashazavolnyuk.client.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.customview.RatingView;
import com.mashazavolnyuk.client.data.Item;
import com.mashazavolnyuk.client.data.Item__;
import com.mashazavolnyuk.client.data.Price;
import com.mashazavolnyuk.client.data.Venue;
import com.mashazavolnyuk.client.utils.ConverterForPrice;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListPlacesAdapter extends RecyclerView.Adapter<ListPlacesAdapter.HolderAdapter> {

    private Context context;
    private List<Item> data;
    private IListPlacesOnClickListener iListPlacesOnClickListener;

    public ListPlacesAdapter(Context context, List<Item> data, IListPlacesOnClickListener iListPlacesOnClickListener) {
        this.context = context;
        this.data = data;
        this.iListPlacesOnClickListener = iListPlacesOnClickListener;
    }


    @Override
    public ListPlacesAdapter.HolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new HolderAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(HolderAdapter holder, int position) {
        final Item item = data.get(position);
        Venue venue = item.getVenue();
        Price price = venue.getPrice();
        String valuePrice = price != null ? ConverterForPrice.getFormatMessageByPrice(price) : "";
        Item__ itemPhoto = venue.getPhotos().getGroups().get(0).getItems().get(0);
        if (itemPhoto != null) {
            String path = itemPhoto.getPrefix() + "500x500" + itemPhoto.getSuffix();
            Uri uri = Uri.parse(path);
            Picasso.with(context).load(uri).error(R.drawable.ic_error_image).into(holder.photoPlace);
        }
        holder.firstPositionText.setText(venue.getName());
        String valueSecondPostion = venue.getCategories().get(0).getName() + "," + valuePrice;
        holder.secondPositionText.setText(valueSecondPostion);
        String kmValue = getStringDistanceByValue(venue.getLocation().getDistance());
        String address = venue.getLocation().getAddress();
        String valueForThirdPosition = kmValue + "," + address;
        holder.thirdPositionText.setText(valueForThirdPosition);
        holder.rating.setText(String.format(Locale.ENGLISH, "%.1f", venue.getRating()));
        if (venue.getRatingColor() != null) {
            int color = Color.parseColor("#" + venue.getRatingColor());
            holder.rating.setBackgroundShapeColor(color);
        }
        holder.root.setOnClickListener(view -> iListPlacesOnClickListener.setItem(item));
    }

    private String getStringDistanceByValue(Integer value) {
        Double distance = value / 1000.0;
        return String.format(Locale.ENGLISH, "%.2f km", distance);
    }

    public void setNewData(List<Item> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    class HolderAdapter extends RecyclerView.ViewHolder {
        @BindView(R.id.rootList)
        LinearLayout root;
        @BindView(R.id.photoPlace)
        ImageView photoPlace;
        @BindView(R.id.firstPositionText)
        TextView firstPositionText;
        @BindView(R.id.secondPositionText)
        TextView secondPositionText;
        @BindView(R.id.thirdPositionText)
        TextView thirdPositionText;
        @BindView(R.id.rating)
        RatingView rating;

        HolderAdapter(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
