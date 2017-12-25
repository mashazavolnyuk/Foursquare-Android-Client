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
import com.mashazavolnyuk.client.viewmodels.ListPlacesViewModel;

import java.io.IOException;

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
        ListPlacesViewModel model = ViewModelProviders.of((FragmentActivity) getActivity()).get(ListPlacesViewModel.class);
        item = model.getSelected().getValue();
        fillTestData();
        return view;
    }

    private void fillTestData() {
        RetrofitClient.changeApiBaseUrl("https://maps.googleapis.com/");
        IRequestStaticMap iRequestListPlaces = RetrofitClient.getRetrofit().create(IRequestStaticMap.class);
        iRequestListPlaces.getStaticGoogleMap("40.714728,-73.9986729", "640x400", "15",
                "color:blue|40.714728,-73.9986729|size:mid|label:S",
                "AIzaSyDMofTdX08zRW6ldhaPiCPhA6tz5dz_PVQ"
        ).enqueue(new Callback<ResponseBody>() {
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
