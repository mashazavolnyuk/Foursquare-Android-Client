package com.mashazavolnyuk.client.api.requests;

import com.mashazavolnyuk.client.data.Data;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRequestListPlaces {

    @GET("venues/explore?v=20182912&limit=250")
    Call<Data> getListRecommendationPlaces(@Query("client_id") String clientID,
                                           @Query("client_secret") String clientSecret,
                                           @Query("ll") String s,
                                           @Query("venuePhotos") Integer isNeedPhoto,
                                           @Query("sortByDistance") String isSortByDistance,
                                           @Query("price") String listPrice,
                                           @Query("radius") String radius
    );

    @GET("venues/{id}/photos?v=20182412&limit=3")
    Call<String> getDetailedPhotosById(@Path("id") String idVenue,
                                       @Query("client_id") String clientID,
                                       @Query("client_secret") String clientSecret
    );

    @GET("venues/{id}/tips?v=20182412")
    Call<String> getDetailedTipsById(@Path("id") String idVenue,
                                       @Query("client_id") String clientID,
                                       @Query("client_secret") String clientSecret
    );
}
