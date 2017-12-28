package com.mashazavolnyuk.client.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashazavolnyuk.client.api.RetrofitClient;
import com.mashazavolnyuk.client.api.requests.IRequestListPlaces;
import com.mashazavolnyuk.client.api.requests.IRequestStaticMap;
import com.mashazavolnyuk.client.data.Photo;
import com.mashazavolnyuk.client.data.Venue;
import com.mashazavolnyuk.client.data.photos.DetailedPhoto;
import com.mashazavolnyuk.client.data.photos.PhotoItem;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailAboutPlaceRepository {
    private MutableLiveData<List<PhotoItem>> data;

    public void getDetailedPhotoById(String idVenue,
                                     final CallbackResponse<List<PhotoItem>> iObserverListPlacesData) {
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
                data.setValue(detailedPhoto.getItems());
                iObserverListPlacesData.response(data.getValue());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void loadStaticGoogleMap(Venue venue, CallbackResponse<Bitmap> bitmapCallbackResponse) {
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
                            bitmapCallbackResponse.response(bitmap);
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
