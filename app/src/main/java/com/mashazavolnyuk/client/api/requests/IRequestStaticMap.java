package com.mashazavolnyuk.client.api.requests;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRequestStaticMap {
    @GET("maps/api/staticmap")
    Call<ResponseBody> getStaticGoogleMap(@Query("center") String center,
                                          @Query("size") String size,
                                          @Query("zoom") String zoom,
                                          @Query("markers") String markerStyle,
                                          @Query("key") String key
                                          );
}
