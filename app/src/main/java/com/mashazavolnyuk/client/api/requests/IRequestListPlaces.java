package com.mashazavolnyuk.client.api.requests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRequestListPlaces {

    @GET("venues/search?v=20182412&limit=1")
    Call<String> getListPlaces(@Query("client_id") String clientID,
                               @Query("client_secret") String clientSecret,
                               @Query("ll")String s);
}