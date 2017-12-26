package com.mashazavolnyuk.client.repositories;

import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashazavolnyuk.client.api.RetrofitClient;
import com.mashazavolnyuk.client.api.requests.IRequestListPlaces;
import com.mashazavolnyuk.client.data.photos.DetailedPhoto;
import com.mashazavolnyuk.client.data.photos.PhotoResponseData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailAboutPlaceRepository {
    private MutableLiveData<DetailedPhoto> data;

    public void getDetailedPhotoById(String idVenue,
                                     final IObserverDetailedPhoto iObserverListPlacesData) {
        data = new MutableLiveData<>();
        RetrofitClient.changeApiBaseUrl("https://api.foursquare.com/v2/");
        IRequestListPlaces iRequestListPlaces = RetrofitClient.getRetrofit().create(IRequestListPlaces.class);
        String id = "XCVWHEE4CR51K5UHTEOW1KUU4QT3RKBRZLQMG1ZN0APZPWVR";
        String secret = "TJNGAOBUDM1ILMOZJZ5Y02LWJS5SM3QGO55HJMNBEXIYKK0W";
        iRequestListPlaces.getDetailedPhotosById(idVenue, id,
                secret).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(response.body().toString(), JsonObject.class);
                DetailedPhoto detailedPhoto = gson.fromJson(((JsonObject) jsonObject.get("response")).get("photos"), DetailedPhoto.class);
                data.setValue(detailedPhoto);
                iObserverListPlacesData.newData(data);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
}
