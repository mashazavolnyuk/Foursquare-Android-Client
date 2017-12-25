package com.mashazavolnyuk.client.repositories;

import android.arch.lifecycle.MutableLiveData;

import com.mashazavolnyuk.client.api.RetrofitClient;
import com.mashazavolnyuk.client.api.requests.IRequestListPlaces;
import com.mashazavolnyuk.client.data.Data;

import retrofit2.Callback;

public class DetailAboutPlaceRepository {
    private MutableLiveData<Data> data;

    public void getDetailedPhotoById(String idVenue,
                                     final IObserverListPlacesData iObserverListPlacesData) {
        data = new MutableLiveData<>();
        IRequestListPlaces iRequestListPlaces = RetrofitClient.getRetrofit().create(IRequestListPlaces.class);
        String id = "XCVWHEE4CR51K5UHTEOW1KUU4QT3RKBRZLQMG1ZN0APZPWVR";
        String secret = "TJNGAOBUDM1ILMOZJZ5Y02LWJS5SM3QGO55HJMNBEXIYKK0W";
        iRequestListPlaces.getDetailedPhotosById(idVenue, id, secret
        ).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(retrofit2.Call<Data> call, retrofit2.Response<Data> response) {
                data.setValue(response.body());
                iObserverListPlacesData.newData(data);
            }
            @Override
            public void onFailure(retrofit2.Call<Data> call, Throwable t) {

            }
        });
    }
}
