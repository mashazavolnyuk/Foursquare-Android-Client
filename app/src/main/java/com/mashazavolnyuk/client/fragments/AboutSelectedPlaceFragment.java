package com.mashazavolnyuk.client.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.api.RetrofitClient;
import com.mashazavolnyuk.client.api.requests.IRequestStaticMap;
import com.mashazavolnyuk.client.data.Item;
import com.mashazavolnyuk.client.data.Venue;
import com.mashazavolnyuk.client.viewmodels.DetailAboutPlaceViewModel;

import java.io.IOException;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutSelectedPlaceFragment extends BaseFragment {

    ImageView imageMap;
    Item item;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_selected_place, container, false);
        imageMap = view.findViewById(R.id.imageMapSelectedPlace);
        DetailAboutPlaceViewModel model = ViewModelProviders.of((FragmentActivity) getActivity()).get(DetailAboutPlaceViewModel.class);
        item = model.getSelected().getValue();
        fillTestData(item);
        return view;
    }

    private void fillTestData(Item item) {
        Venue venue = item.getVenue();
        loadStaticGoogleMap(venue);
    }

    private void loadStaticGoogleMap(Venue venue) {
        Double lat = venue.getLocation().getLat();
        Double lng = venue.getLocation().getLng();
        String markerStyle = String.format(Locale.ENGLISH, "color:blue|%.0f,%.0f|size:mid|label:S", lat, lng);
        RetrofitClient.changeApiBaseUrl("https://maps.googleapis.com/");
        IRequestStaticMap iRequestListPlaces = RetrofitClient.getRetrofit().create(IRequestStaticMap.class);
        iRequestListPlaces.getStaticGoogleMap(lat + "," + lng, "640x400", "15",
                markerStyle, "AIzaSyDMofTdX08zRW6ldhaPiCPhA6tz5dz_PVQ")
                .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    byte[] bytes = response.body().bytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageMap.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
