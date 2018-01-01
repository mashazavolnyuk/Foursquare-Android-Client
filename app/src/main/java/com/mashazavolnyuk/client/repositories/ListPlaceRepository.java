package com.mashazavolnyuk.client.repositories;

import android.arch.lifecycle.MutableLiveData;

import com.mashazavolnyuk.client.ProjectCredentation;
import com.mashazavolnyuk.client.api.RetrofitClient;
import com.mashazavolnyuk.client.api.requests.IRequestListPlaces;
import com.mashazavolnyuk.client.data.Data;
import com.mashazavolnyuk.client.data.Item;

import java.util.Collections;
import java.util.List;

import retrofit2.Callback;

public class ListPlaceRepository {

    private MutableLiveData<List<Item>> data;

    public void getListPlaces(double latitude, double longitude,
                              boolean isSortByDistance, String prices, float radius,
                              final CallbackResponse<List<Item>> callbackResponse) {
        data = new MutableLiveData<>();
        RetrofitClient.changeApiBaseUrl("https://api.foursquare.com/v2/");
        IRequestListPlaces iRequestListPlaces = RetrofitClient.getRetrofit().create(IRequestListPlaces.class);
        String isSortByDistanceValue = isSortByDistance ? "1" : "0";
        iRequestListPlaces.getListRecommendationPlaces(ProjectCredentation.ID, ProjectCredentation.SECRET, "" + latitude + "," + longitude,
                1, isSortByDistanceValue, prices, String.valueOf(radius)).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(retrofit2.Call<Data> call, retrofit2.Response<Data> response) {
                Data responseData = response.body();
                if (responseData != null) {
                    data.setValue(responseData.getResponse().getGroups().get(0).getItems());
                    callbackResponse.response(data.getValue());
                } else {
                    callbackResponse.response(Collections.emptyList());
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Data> call, Throwable t) {

            }
        });
    }

}
