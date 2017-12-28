package com.mashazavolnyuk.client.repositories;

import android.arch.lifecycle.MutableLiveData;

import com.mashazavolnyuk.client.api.RetrofitClient;
import com.mashazavolnyuk.client.api.requests.IRequestListPlaces;
import com.mashazavolnyuk.client.data.Data;
import com.mashazavolnyuk.client.data.Item;

import java.util.List;

import retrofit2.Callback;

public class ListPlaceRepository {

    private MutableLiveData<List<Item>> data;

    public void getListPlaces(double latitude, double longitude,
                              final CallbackResponse<List<Item>> callbackResponse) {
        data = new MutableLiveData<>();
        IRequestListPlaces iRequestListPlaces = RetrofitClient.getRetrofit().create(IRequestListPlaces.class);
        String id = "XCVWHEE4CR51K5UHTEOW1KUU4QT3RKBRZLQMG1ZN0APZPWVR";
        String secret = "TJNGAOBUDM1ILMOZJZ5Y02LWJS5SM3QGO55HJMNBEXIYKK0W";
        iRequestListPlaces.getListRecommendationPlaces(id, secret, "" + latitude + "," + longitude,
                1).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(retrofit2.Call<Data> call, retrofit2.Response<Data> response) {
                data.setValue(response.body().getResponse().getGroups().get(0).getItems());
                callbackResponse.response(data.getValue());
            }
            @Override
            public void onFailure(retrofit2.Call<Data> call, Throwable t) {

            }
        });
    }
}
