package com.mashazavolnyuk.client.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static String BASE_URL = "https://api.foursquare.com/v2/";
    private static Retrofit retrofit = null;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL);

    static {
        builder = new retrofit2.Retrofit.Builder().
                baseUrl(BASE_URL).
                client(getHttpClient().build())
                .addConverterFactory(ScalarsConverterFactory.create()).
                        addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();
    }

    public static void changeApiBaseUrl(String newApiBaseUrl) {
        BASE_URL = newApiBaseUrl;

        builder = new retrofit2.Retrofit.Builder().
                baseUrl(BASE_URL).
                client( getHttpClient().build())
                .addConverterFactory(ScalarsConverterFactory.create()).
                        addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();
    }

    private static OkHttpClient.Builder getHttpClient() {
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
        return httpClient;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
