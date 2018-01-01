package com.mashazavolnyuk.client.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.adapters.PlaceAdapter;
import com.mashazavolnyuk.client.data.IDataDetailVenue;
import com.mashazavolnyuk.client.data.detailedVenue.DataInfoVenue;
import com.mashazavolnyuk.client.data.PlaceItem;
import com.mashazavolnyuk.client.data.Venue;
import com.mashazavolnyuk.client.data.detailedVenue.HeaderTip;
import com.mashazavolnyuk.client.viewmodels.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PlaceFragment extends BaseFragment {

    @BindView(R.id.listTips)
    RecyclerView recyclerViewTips;

    private List<IDataDetailVenue> data;
    private Unbinder unbinder;
    private PlaceItem placeItem;
    private PlaceViewModel model;
    private PlaceAdapter placeAdapter;
    DataInfoVenue dataInfoVenue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_venue, container, false);
        unbinder = ButterKnife.bind(this, view);
        data = new ArrayList<>();
        placeAdapter = new PlaceAdapter(getActivity(), data);
        model = ViewModelProviders.of((FragmentActivity)
                getActivity()).get(PlaceViewModel.class);
        placeItem = model.getSelectedPlace().getValue();
        dataInfoVenue = new DataInfoVenue();
        recyclerViewTips.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewTips.setAdapter(placeAdapter);
        recyclerViewTips.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        setDefaultValues();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setDefaultValues() {
        fillAboutPlace();
        loadStaticGoogleMap(placeItem.getVenue());
        fillListTips();
        fillGallery();
    }

    private void loadStaticGoogleMap(Venue venue) {
        model.loadStaticGoogleMap(venue, map -> {
            dataInfoVenue.setMap(map);
            placeAdapter.updateItem(0);
        });
    }

    private void fillGallery() {
        Venue venue = placeItem.getVenue();
        model.getPhotoByIdVenue(venue.getId(), response -> {
            if (response != null) {
                dataInfoVenue.setPhotoItems(response);
                placeAdapter.updateItem(0);
            }
        });
    }

    private void fillListTips() {
        Venue venue = placeItem.getVenue();
        model.getTipsByIdVenue(venue.getId(), response -> {
            data.add(new HeaderTip());
            data.addAll(response);
            placeAdapter.updateData(data);
        });
    }

    private void fillAboutPlace() {
        Venue venue = placeItem.getVenue();
        dataInfoVenue.addInfo(venue.getName());
        dataInfoVenue.addInfo(venue.getCategories().get(0).getShortName());
        dataInfoVenue.addInfo(venue.getContact().getFormattedPhone());
        dataInfoVenue.setColor(venue.getRatingColor());
        dataInfoVenue.setRating(venue.getRating());
        dataInfoVenue.setLocation(venue.getLocation().getAddress());
        data.add(dataInfoVenue);
        dataInfoVenue.setPrice(venue.getPrice());
    }
}
