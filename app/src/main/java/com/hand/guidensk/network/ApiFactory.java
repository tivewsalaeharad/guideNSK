package com.hand.guidensk.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private static final String ROOT_URL = "https://maps.googleapis.com";

    private static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RouteApi getService() {
        return buildRetrofit().create(RouteApi.class);
    }
}