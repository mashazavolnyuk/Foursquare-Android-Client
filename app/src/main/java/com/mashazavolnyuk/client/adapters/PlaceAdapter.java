package com.mashazavolnyuk.client.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.customview.RatingView;
import com.mashazavolnyuk.client.data.IDataDetailVenue;
import com.mashazavolnyuk.client.data.Price;
import com.mashazavolnyuk.client.data.detailedVenue.DataInfoVenue;
import com.mashazavolnyuk.client.data.detailedVenue.HeaderTip;
import com.mashazavolnyuk.client.data.photos.PhotoItem;
import com.mashazavolnyuk.client.data.tips.DetailTip;
import com.mashazavolnyuk.client.utils.ConverterForPrice;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private Context context;
    private List<IDataDetailVenue> data;

    private static final int ITEM_TYPE_INFO = 0;
    private static final int ITEM_HEADER_TIP = 1;
    private static final int ITEM_TYPE_TIP = 2;

    public PlaceAdapter(Context context, List<IDataDetailVenue> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_INFO:
                View infoView = LayoutInflater.from(context).inflate(R.layout.item_info_venue, parent, false);
                return new InfoHolder(infoView);

            case ITEM_HEADER_TIP:
                View tipHeaderView = LayoutInflater.from(context).inflate(R.layout.item_header_tip, parent, false);
                return new TipHeaderHolder(tipHeaderView);

            case ITEM_TYPE_TIP:
                View tipView = LayoutInflater.from(context).inflate(R.layout.item_tip, parent, false);
                return new TipHolder(tipView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof DataInfoVenue) {
            return ITEM_TYPE_INFO;
        } else {
            if (data.get(position) instanceof HeaderTip) {
                return ITEM_HEADER_TIP;
            }
        }
        return ITEM_TYPE_TIP;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void updateData(List<IDataDetailVenue> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    public void updateItem(int position) {
        notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Uri uri;
        switch (holder.getItemViewType()) {

            case ITEM_TYPE_INFO:
                DataInfoVenue dataInfoVenue = (DataInfoVenue) data.get(position);
                InfoHolder infoHolder = (InfoHolder) holder;
                List<PhotoItem> photoItem = dataInfoVenue.getGallery();
                fillGallery(infoHolder, photoItem);
                infoHolder.firstPositionText.setText(dataInfoVenue.getListInfo().get(0));
                infoHolder.secondPositionText.setText(dataInfoVenue.getListInfo().get(1));
                infoHolder.thirdPositionText.setText(dataInfoVenue.getListInfo().get(2));
                infoHolder.ratingView.setRating(dataInfoVenue.getRating(), dataInfoVenue.getColor());
                Price price = dataInfoVenue.getPrice();
                String valuePrice = price != null ? ConverterForPrice.getFormatMessageByPrice(price) : "";
                infoHolder.textCurrency.setText(valuePrice);

                infoHolder.location.setText(dataInfoVenue.getLocation());
                infoHolder.mapView.setImageBitmap(dataInfoVenue.getMap());
                break;

            case ITEM_HEADER_TIP:
                break;

            case ITEM_TYPE_TIP:
                DetailTip tip = (DetailTip) data.get(position);
                TipHolder tipHolder = (TipHolder) holder;
                tipHolder.messageUser.setText(tip.getText());
                String lastName = tip.getUser().getLastName() != null ? tip.getUser().getLastName() : "";
                String firstName = tip.getUser().getFirstName() != null ? tip.getUser().getFirstName() : "";
                String userName = firstName + " " + lastName;
                tipHolder.nameUser.setText(userName);
                showLikes(tipHolder, tip);
                String photoUrl = tip.getPhotourl();
                if (photoUrl != null) {
                    uri = Uri.parse(photoUrl);
                    Picasso.with(context).load(uri).error(R.drawable.ic_error_image).into(tipHolder.photoUser);
                } else {
                    tipHolder.photoUser.setImageResource(R.drawable.ic_photo);
                }
                break;
        }
    }

    private void fillGallery(InfoHolder infoHolder, List<PhotoItem> photoItem) {
        Uri uri;
        ImageView imageView = null;
        if (photoItem.size() != 0) {
            for (int index = 0; index < 3; index++) {
                switch (index) {
                    case 0:
                        imageView = infoHolder.firstPopularPhoto;
                        break;
                    case 1:
                        imageView = infoHolder.secondPopularPhoto;
                        break;
                    case 2:
                        imageView = infoHolder.thirdPopularPhoto;
                        break;
                }
                if(photoItem.size() > index) {
                    String firstUri = photoItem.get(index).getPrefix() + "640x400" + photoItem.get(index).getSuffix();
                    uri = Uri.parse(firstUri);
                    Picasso.with(context).load(uri).error(R.drawable.ic_error_image).into(imageView);
                }
            }
        }
    }

    private void showLikes(TipHolder holder, DetailTip tip) {
        if (tip.getLikes() == null || tip.getLikes().getCount() == null || tip.getLikes().getCount() == 0) {
            holder.imageLikes.setVisibility(View.INVISIBLE);
            holder.countLike.setText("");
        } else {
            Integer countLikes = tip.getLikes().getCount();
            holder.imageLikes.setVisibility(View.VISIBLE);
            holder.countLike.setText(String.format("%s", countLikes));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class InfoHolder extends ViewHolder {

        @BindView(R.id.firstPopularPhoto)
        ImageView firstPopularPhoto;

        @BindView(R.id.secondPopularPhoto)
        ImageView secondPopularPhoto;

        @BindView(R.id.thirdPopularPhoto)
        ImageView thirdPopularPhoto;

        @BindView(R.id.firstPositionText)
        TextView firstPositionText;

        @BindView(R.id.secondPositionText)
        TextView secondPositionText;

        @BindView(R.id.thirdPositionText)
        TextView thirdPositionText;

        @BindView(R.id.textCurrency)
        TextView textCurrency;

        @BindView(R.id.rating)
        RatingView ratingView;

        @BindView(R.id.locationVenue)
        TextView location;

        @BindView(R.id.mapVenue)
        ImageView mapView;

        InfoHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class TipHeaderHolder extends ViewHolder {
        @BindView(R.id.headerTip)
        TextView headerTip;

        TipHeaderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class TipHolder extends ViewHolder {

        @BindView(R.id.photoUser)
        ImageView photoUser;
        @BindView(R.id.messageUser)
        TextView messageUser;
        @BindView(R.id.nameUser)
        TextView nameUser;
        @BindView(R.id.countLike)
        TextView countLike;
        @BindView(R.id.imageLikes)
        ImageView imageLikes;

        TipHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
