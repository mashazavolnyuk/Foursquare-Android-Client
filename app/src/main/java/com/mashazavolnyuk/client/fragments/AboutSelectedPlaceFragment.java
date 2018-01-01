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
import com.mashazavolnyuk.client.adapters.DetailVenueAdapter;
import com.mashazavolnyuk.client.adapters.IDataDetailVenue;
import com.mashazavolnyuk.client.data.detailedVenue.DataInfoVenue;
import com.mashazavolnyuk.client.data.Item;
import com.mashazavolnyuk.client.data.Venue;
import com.mashazavolnyuk.client.data.detailedVenue.HeaderTip;
import com.mashazavolnyuk.client.viewmodels.DetailAboutPlaceViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AboutSelectedPlaceFragment extends BaseFragment {

    @BindView(R.id.listTips)
    RecyclerView recyclerViewTips;

    private List<IDataDetailVenue> data;
    private Unbinder unbinder;
    private Item item;
    private DetailAboutPlaceViewModel model;
    private DetailVenueAdapter detailVenueAdapter;
    DataInfoVenue dataInfoVenue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_venue, container, false);
        unbinder = ButterKnife.bind(this, view);
        data = new ArrayList<>();
        detailVenueAdapter = new DetailVenueAdapter(getActivity(), data);
        model = ViewModelProviders.of((FragmentActivity)
                getActivity()).get(DetailAboutPlaceViewModel.class);
        item = model.getSelected().getValue();
        dataInfoVenue = new DataInfoVenue();
        recyclerViewTips.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewTips.setAdapter(detailVenueAdapter);
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
        loadStaticGoogleMap(item.getVenue());
        fillListTips();
        fillGallery();
    }

    private void loadStaticGoogleMap(Venue venue) {
        model.loadStaticGoogleMap(venue, map -> {
            dataInfoVenue.setMap(map);
            detailVenueAdapter.updateItem(0);
        });
    }

    private void fillGallery() {
        Venue venue = item.getVenue();
        model.getPhotoByIdVenue(venue.getId(), response -> {
            if (response != null) {
                dataInfoVenue.setPhotoItems(response);
                detailVenueAdapter.updateItem(0);
            }
        });
    }

    private void fillListTips() {
        Venue venue = item.getVenue();
        model.getTipsByIdVenue(venue.getId(), response -> {
            data.add(new HeaderTip());
            data.addAll(response);
            detailVenueAdapter.updateData(data);
        });
    }

    private void fillAboutPlace() {
        Venue venue = item.getVenue();
        dataInfoVenue.addInfo(venue.getName());
        dataInfoVenue.addInfo(venue.getCategories().get(0).getShortName());
        dataInfoVenue.addInfo(venue.getContact().getFormattedPhone());
        int color = Color.parseColor("#" + venue.getRatingColor());
        dataInfoVenue.setColor(color);
        dataInfoVenue.setRating(venue.getRating());
        dataInfoVenue.setLocation(venue.getLocation().getAddress());
        data.add(dataInfoVenue);
        dataInfoVenue.setPrice(venue.getPrice());
    }
}
