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
import com.mashazavolnyuk.client.data.PlaceItem;
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

    private static final int SMALL_IMAGE_SIZE = 100;     //following foursquare API
    private static final int BIG_IMAGE_SIZE = 300;
    private static final int SMALL_IMAGE_LIMIT = 150;
    private Context context;
    private List<PlaceItem> data;
    private IListPlacesOnClickListener iListPlacesOnClickListener;
    private String imageSizeStr;

    public ListPlacesAdapter(Context context, IListPlacesOnClickListener iListPlacesOnClickListener) {
        this.context = context;
        this.iListPlacesOnClickListener = iListPlacesOnClickListener;
        int imageSize = (int) context.getResources().getDimension(R.dimen.image_wight_list_default);
        if (imageSize < SMALL_IMAGE_LIMIT) {
            imageSize = SMALL_IMAGE_SIZE;
        } else {
            imageSize = BIG_IMAGE_SIZE;
        }
        imageSizeStr = String.valueOf(imageSize) + "x" + String.valueOf(imageSize);
    }

    public void updateData(List<PlaceItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ListPlacesAdapter.HolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new HolderAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(HolderAdapter holder, int position) {
        final PlaceItem placeItem = data.get(position);
        Venue venue = placeItem.getVenue();
        Price price = venue.getPrice();
        String valuePrice = price != null ? ConverterForPrice.getFormatMessageByPrice(price) : "";
        if(venue.getPhotos() != null && venue.getPhotos().getGroups() != null && venue.getPhotos().getGroups().size() > 0 &&
                venue.getPhotos().getGroups().get(0).getItems() != null &&
                venue.getPhotos().getGroups().get(0).getItems().size() > 0 &&
                venue.getPhotos().getGroups().get(0).getItems().get(0) != null) {
            Item__ itemPhoto = venue.getPhotos().getGroups().get(0).getItems().get(0);
            String path = itemPhoto.getPrefix() + imageSizeStr + itemPhoto.getSuffix();
            Uri uri = Uri.parse(path);
            Picasso.with(context).load(uri).error(R.drawable.ic_error_image).into(holder.photoPlace);
        } else {
            holder.photoPlace.setImageResource(R.drawable.ic_photo);
        }
        holder.firstPositionText.setText(venue.getName());
        String valueSecondPostion = venue.getCategories().get(0).getName() + ", " + valuePrice;
        holder.secondPositionText.setText(valueSecondPostion);
        String kmValue = getStringDistanceByValue(venue.getLocation().getDistance());
        String address = venue.getLocation().getAddress() != null ? venue.getLocation().getAddress() : "";
        String valueForThirdPosition = kmValue + ", " + address;
        holder.thirdPositionText.setText(valueForThirdPosition);
        holder.rating.setRating(venue.getRating(), venue.getRatingColor());
        holder.root.setOnClickListener(view -> iListPlacesOnClickListener.setPlaceItem(placeItem));
    }

    private String getStringDistanceByValue(Integer value) {
        Double distance = value / 1000.0;
        return String.format(Locale.ENGLISH, "%.2f km", distance);
    }

    public void setNewData(List<PlaceItem> newData) {
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
