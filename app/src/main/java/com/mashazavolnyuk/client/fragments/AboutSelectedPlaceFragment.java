package com.mashazavolnyuk.client.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.adapters.ImageGalleryAdapter;
import com.mashazavolnyuk.client.customview.RatingView;
import com.mashazavolnyuk.client.adapters.TipsAdapter;
import com.mashazavolnyuk.client.data.Item;
import com.mashazavolnyuk.client.data.Tip;
import com.mashazavolnyuk.client.data.Venue;
import com.mashazavolnyuk.client.data.tips.DetailTip;
import com.mashazavolnyuk.client.repositories.CallbackResponse;
import com.mashazavolnyuk.client.viewmodels.DetailAboutPlaceViewModel;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AboutSelectedPlaceFragment extends BaseFragment {

    @BindView(R.id.firstPositionText) TextView firstField;
    @BindView(R.id.secondPositionText) TextView secondField;
    @BindView(R.id.thirdPositionText) TextView thirdField;
    @BindView(R.id.textCurrency) TextView textCurrency;
    @BindView(R.id.rating) RatingView rating;
    @BindView(R.id.imageMapSelectedPlace) ImageView imageMap;
    @BindView(R.id.listTips) RecyclerView recyclerViewTips;
    @BindView(R.id.listGallery) RecyclerView recyclerViewGallery;

    private Unbinder unbinder;
    Item item;
    DetailAboutPlaceViewModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_selected_place, container, false);
        unbinder = ButterKnife.bind(this, view);
        model = ViewModelProviders.of((FragmentActivity)
                getActivity()).get(DetailAboutPlaceViewModel.class);
        item = model.getSelected().getValue();
        fillData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void fillData() {
        fillTestData(item);
        fillGallery();
        fillListTips();
        fillAboutPlace();
    }

    private void fillTestData(Item item) {
        Venue venue = item.getVenue();
        loadStaticGoogleMap(venue);
    }

    private void loadStaticGoogleMap(Venue venue) {
        model.loadStaticGoogleMap(venue, map -> imageMap.setImageBitmap(map));
    }

    private void fillGallery() {
        Venue venue = item.getVenue();
        model.getPhotoByIdVenue(venue.getId(), response -> {
            if (response != null) {
                LinearLayoutManager horizontalLayoutManagaer
                        = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                ImageGalleryAdapter galleryAdapter = new ImageGalleryAdapter(getActivity(), response);
                recyclerViewGallery.setLayoutManager(horizontalLayoutManagaer);
                recyclerViewGallery.setAdapter(galleryAdapter);
            }
        });

    }

    private void fillListTips() {
        Venue venue = item.getVenue();
        model.getTipsByIdVenue(venue.getId(), new CallbackResponse<List<DetailTip>>() {
            @Override
            public void response(List<DetailTip> response) {
                List<DetailTip> tips = response;
                TipsAdapter tipsAdapter = new TipsAdapter(getActivity(), tips);
                recyclerViewTips.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewTips.setAdapter(tipsAdapter);
            }
        });
    }

    private void fillAboutPlace() {
        Venue venue = item.getVenue();
        rating.setText(String.format(Locale.ENGLISH, "%.1f", venue.getRating()));
        rating.setBackgroundShapeColor(Color.parseColor("#" + venue.getRatingColor()));
        firstField.setText(venue.getName());
        secondField.setText(venue.getCategories().get(0).getPluralName());
        if (venue.getPrice() != null) {
            textCurrency.setText(venue.getPrice().getCurrency());
        }
    }
}
