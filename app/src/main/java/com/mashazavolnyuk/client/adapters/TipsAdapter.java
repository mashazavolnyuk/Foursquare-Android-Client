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

import com.mashazavolnyuk.client.data.Photo__;
import com.mashazavolnyuk.client.data.Tip;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.HolderAdapter> {
    private Context context;
    private List<Tip> tipList;

    public TipsAdapter(Context context, List<Tip> tipList) {
        this.context = context;
        this.tipList = tipList;
    }

    @Override
    public HolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tip, parent, false);
        return new HolderAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(HolderAdapter holder, int position) {
        Tip tip = tipList.get(position);
        holder.messageUser.setText(tip.getText());
        String userName = tip.getUser().getFirstName() + " " + tip.getUser().getLastName();
        holder.nameUser.setText(userName);
        decideIsShowLikes(holder, tip);
        Photo__ photoUser = tip.getUser().getPhoto();
        if (photoUser != null) {
            String path = photoUser.getPrefix() + "500x500" + photoUser.getSuffix();
            Uri uri = Uri.parse(path);
            Picasso.with(context).load(uri).error(R.drawable.ic_error_image).into(holder.photoUser);
        }
    }

    private void decideIsShowLikes(HolderAdapter holder, Tip tip) {
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

    @Override
    public int getItemCount() {
        return tipList.size();
    }

    class HolderAdapter extends RecyclerView.ViewHolder {
        @BindView(R.id.photoUser) ImageView photoUser;
        @BindView(R.id.messageUser) TextView messageUser;
        @BindView(R.id.nameUser) TextView nameUser;
        @BindView(R.id.dataMessage) TextView dataMessage;
        @BindView(R.id.countLike) TextView countLike;
        @BindView(R.id.imageLikes)ImageView imageLikes;

        HolderAdapter(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
