package com.mashazavolnyuk.client.adapters;

import android.content.Context;
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
import com.mashazavolnyuk.client.data.photos.PhotoItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.HolderAdapter> {

    private Context context;
    private List<PhotoItem> items;

    public ImageGalleryAdapter(Context context, List<PhotoItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ImageGalleryAdapter.HolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pager_item, parent, false);
        return new ImageGalleryAdapter.HolderAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(ImageGalleryAdapter.HolderAdapter holder, int position) {
        PhotoItem photoItem = items.get(position);
        String path = photoItem.getPrefix() + "640x400" + photoItem.getSuffix();
        Uri uri = Uri.parse(path);
        Picasso.with(context).load(uri).error(R.drawable.ic_error_image).into(holder.photoVenue);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class HolderAdapter extends RecyclerView.ViewHolder {
        @BindView(R.id.photoVenue)
        ImageView photoVenue;

        HolderAdapter(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
