package com.mashazavolnyuk.client.api.requests;

import com.mashazavolnyuk.client.data.Data;
import com.mashazavolnyuk.client.data.photos.PhotoResponseData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRequestListPlaces {

    @GET("venues/explore?v=20182912&radius=3000&limit=250")
    Call<Data> getListRecommendationPlaces(@Query("client_id") String clientID,
                                           @Query("client_secret") String clientSecret,
                                           @Query("ll") String s,
                                           @Query("venuePhotos") Integer isNeedPhoto,
                                           @Query("sortByDistance") String isSortByDistance,
                                           @Query("price") String listPrice
    );

    @GET("venues/{id}/photos?v=20182412")
    Call<String> getDetailedPhotosById(@Path("id") String idVenue,
                                       @Query("client_id") String clientID,
                                       @Query("client_secret") String clientSecret
    );
}
