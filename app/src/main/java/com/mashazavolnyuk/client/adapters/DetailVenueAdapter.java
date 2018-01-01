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

public class DetailVenueAdapter extends RecyclerView.Adapter<DetailVenueAdapter.ViewHolder> {

    private Context context;
    private List<IDataDetailVenue> data;

    private static final int ITEM_TYPE_INFO = 0;
    private static final int ITEM_HEADER_TIP = 1;
    private static final int ITEM_TYPE_TIP = 2;

    public DetailVenueAdapter(Context context, List<IDataDetailVenue> data) {
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
                if (photoItem.size() != 0) {
                    String firstUri = photoItem.get(0).getPrefix() + "640x400" + photoItem.get(0).getSuffix();
                    uri = Uri.parse(firstUri);
                    Picasso.with(context).load(uri).error(R.drawable.ic_error_image).into(((InfoHolder) holder).firstPopularPhoto);
                }
                infoHolder.firstPositionText.setText(dataInfoVenue.getListInfo().get(0));
                infoHolder.secondPositionText.setText(dataInfoVenue.getListInfo().get(1));
                infoHolder.thirdPositionText.setText(dataInfoVenue.getListInfo().get(2));
                infoHolder.ratingView.setBackgroundShapeColor(dataInfoVenue.getColor());
                Price price = dataInfoVenue.getPrice();
                String valuePrice = price != null ? ConverterForPrice.getFormatMessageByPrice(price) : "";
                infoHolder.textCurrency.setText(valuePrice);
                String rating = String.valueOf(dataInfoVenue.getRating());
                infoHolder.ratingView.setText(rating);
                infoHolder.location.setText(dataInfoVenue.getLocation());
                infoHolder.mapView.setImageBitmap(dataInfoVenue.getMap());
                break;

            case ITEM_HEADER_TIP:
                HeaderTip headerTip = (HeaderTip) data.get(position);
                TipHeaderHolder tipHeaderHolder = (TipHeaderHolder) holder;
                tipHeaderHolder.headerTip.setText(headerTip.getHeader());
                break;

            case ITEM_TYPE_TIP:
                DetailTip tip = (DetailTip) data.get(position);
                TipHolder tipHolder = (TipHolder) holder;
                tipHolder.messageUser.setText(tip.getText());
                String userName = tip.getUser().getFirstName() + " " + tip.getUser().getLastName();
                tipHolder.nameUser.setText(userName);
                decideIsShowLikes(tipHolder, tip);
                String photoUrl = tip.getPhotourl();
                if (photoUrl != null) {
                    uri = Uri.parse(photoUrl);
                    Picasso.with(context).load(uri).error(R.drawable.ic_error_image).into(tipHolder.photoUser);
                }
                break;
        }
    }

    private void decideIsShowLikes(TipHolder holder, DetailTip tip) {
        if (tip.getLikes() == null) {
            holder.imageLikes.setVisibility(View.INVISIBLE);
        } else {
            Integer countLikes = tip.getLikes().getCount() != null ? tip.getLikes().getCount() : 0;
            if (countLikes > 0) {
                holder.imageLikes.setVisibility(View.VISIBLE);
                holder.countLike.setText(countLikes.toString());
            } else {
                holder.imageLikes.setVisibility(View.INVISIBLE);
            }
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
        @BindView(R.id.dataMessage)
        TextView dataMessage;
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
