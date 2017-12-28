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
import com.mashazavolnyuk.client.viewmodels.DetailAboutPlaceViewModel;

import java.util.List;
import java.util.Locale;


public class AboutSelectedPlaceFragment extends BaseFragment {

    TextView firstField;
    TextView secondField;
    TextView thirdField;
    TextView textCurrency;
    RatingView rating;
    ImageView imageMap;
    RecyclerView recyclerViewTips;
    Item item;
    DetailAboutPlaceViewModel model;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_selected_place, container, false);
        imageMap = view.findViewById(R.id.imageMapSelectedPlace);
        recyclerView = view.findViewById(R.id.listGallery);
        rating = view.findViewById(R.id.rating);
        firstField = view.findViewById(R.id.firstPositionText);
        secondField = view.findViewById(R.id.secondPositionText);
        thirdField = view.findViewById(R.id.thirdPositionText);
        textCurrency = view.findViewById(R.id.textCurrency);
        model = ViewModelProviders.of((FragmentActivity)
                getActivity()).get(DetailAboutPlaceViewModel.class);
        item = model.getSelected().getValue();
        recyclerViewTips = view.findViewById(R.id.listTips);
        fillData();
        return view;
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
                recyclerView.setLayoutManager(horizontalLayoutManagaer);
                recyclerView.setAdapter(galleryAdapter);
            }
        });

    }

    private void fillListTips() {
        List<Tip> tips = item.getTips();
        TipsAdapter tipsAdapter = new TipsAdapter(getActivity(), tips);
        recyclerViewTips.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewTips.setAdapter(tipsAdapter);
    }

    private void fillAboutPlace() {
        Venue venue = item.getVenue();
        rating.setText(String.format(Locale.ENGLISH, "%.2f", venue.getRating()));
        rating.setBackgroundShapeColor(Color.parseColor("#" + venue.getRatingColor()));
        firstField.setText(venue.getName());
        secondField.setText(venue.getCategories().get(0).getPluralName());
        if (venue.getPrice() != null) {
            textCurrency.setText(venue.getPrice().getCurrency());
        }
    }
}
