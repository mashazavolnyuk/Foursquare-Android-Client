package com.mashazavolnyuk.client.repositories;

import android.arch.lifecycle.MutableLiveData;

import com.mashazavolnyuk.client.Constants;
import com.mashazavolnyuk.client.api.RetrofitClient;
import com.mashazavolnyuk.client.api.requests.IRequestListPlaces;
import com.mashazavolnyuk.client.data.Data;
import com.mashazavolnyuk.client.data.PlaceItem;
import com.mashazavolnyuk.client.utils.DateUtil;

import java.util.Collections;
import java.util.List;

import retrofit2.Callback;

public class ListPlaceRepository {

    private MutableLiveData<List<PlaceItem>> data;

    public void getListPlaces(double latitude, double longitude,
                              boolean isSortByDistance, String prices, float radius,
                              final CallbackResponse<List<PlaceItem>> callbackResponse) {
        data = new MutableLiveData<>();
        RetrofitClient.changeApiBaseUrl("https://api.foursquare.com/v2/");
        IRequestListPlaces iRequestListPlaces = RetrofitClient.getRetrofit().create(IRequestListPlaces.class);
        String isSortByDistanceValue = isSortByDistance ? "1" : "0";
        String date = DateUtil.getDataForResponse();
        iRequestListPlaces.getListRecommendationPlaces(Constants.ID, Constants.SECRET, "" + latitude + "," + longitude,

                1, isSortByDistanceValue, prices, String.valueOf(radius)).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(retrofit2.Call<Data> call, retrofit2.Response<Data> response) {
                Data responseData = response.body();
                if (responseData != null) {
                    data.setValue(responseData.getResponse().getGroups().get(0).getPlaceItems());
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
