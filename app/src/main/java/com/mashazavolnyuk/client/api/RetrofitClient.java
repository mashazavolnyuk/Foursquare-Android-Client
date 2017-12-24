package com.mashazavolnyuk.client.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.foursquare.com/v2/";
    private static Retrofit retrofit = null;

    static {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request original = chain.request();
                // adds "Content-Type" header to all server requests
                final Request request = original.newBuilder().
                        header("Content-Type", "application/json").
                        method(original.method(), original.body()).
                        build();
                return chain.proceed(request);
            }
        }).addInterceptor(logging);

        Retrofit.Builder builder = new retrofit2.Retrofit.Builder().
                baseUrl(BASE_URL).
                client(httpClient.build()).
                addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
