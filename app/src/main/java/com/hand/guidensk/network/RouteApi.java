package com.hand.guidensk.network;

import com.hand.guidensk.models.RouteResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RouteApi {
    @GET("/maps/api/directions/json")
    Call<RouteResponse> getRoute(
            @Query("origin") String position,
            @Query("destination") String destination,
            @Query("sensor") boolean sensor,
            @Query("language") String language);
}