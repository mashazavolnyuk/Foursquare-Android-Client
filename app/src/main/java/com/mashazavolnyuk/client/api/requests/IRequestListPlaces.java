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

    @GET("venues/search?v=20182412&limit=50")
    Call<Data> getListPlaces(@Query("client_id") String clientID,
                             @Query("client_secret") String clientSecret,
                             @Query("ll") String s);

    @GET("venues/explore?v=20182412")
    Call<Data> getListRecommendationPlaces(@Query("client_id") String clientID,
                                           @Query("client_secret") String clientSecret,
                                           @Query("ll") String s,
                                           @Query("venuePhotos") Integer isNeedPhoto);

    @GET("venues/{id}/photos?v=20182412")
    Call<String> getDetailedPhotosById(@Path("id") String idVenue,
                                             @Query("client_id") String clientID,
                                             @Query("client_secret") String clientSecret);
    @GET("venues/{id}/photos?v=20182412")
    Call<String> getDetailedPhotosTryById(@Path("id") String idVenue,
                                       @Query("client_id") String clientID,
                                       @Query("client_secret") String clientSecret);
}
