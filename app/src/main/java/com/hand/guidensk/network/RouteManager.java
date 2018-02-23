package com.hand.guidensk.network;

import com.google.android.gms.maps.model.LatLng;
import com.hand.guidensk.model.Distance;
import com.hand.guidensk.model.Duration;
import com.hand.guidensk.model.Leg;
import com.hand.guidensk.model.Route;
import com.hand.guidensk.model.RouteResponse;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteManager {

    public static void getRoute(final OnGetRouteCallback activity, LatLng user, LatLng place) {
        Call<RouteResponse> call = ApiFactory.getService().getRoute(
                pointToString(user),
                pointToString(place),
                true,
                "ru");
        call.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if (response.isSuccessful()) {
                    List<Route> routes = response.body().getRoutes();
                    if ((routes != null) && (routes.size() > 0))
                        activity.onGetRoute(routes.get(0).getOverviewPolyline().getPoints());
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
            }
        });
    }

    public static void getDistanceAndTime(final OnGetDistanceAndTimeCallback activity, LatLng user, LatLng place) {
        Call<RouteResponse> call = ApiFactory.getService().getRoute(
                pointToString(user),
                pointToString(place),
                true,
                "ru");
        call.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if (response.isSuccessful()) {
                    List<Route> routes = response.body().getRoutes();
                    if ((routes != null) && (routes.size() > 0)) {
                        List<Leg> legs = routes.get(0).getLegs();
                        if ((legs != null) && (legs.size() > 0)) {
                            Leg leg = legs.get(0);
                            Distance distance = leg.getDistance();
                            Duration duration = leg.getDuration();
                            if ((distance != null) && (duration != null))
                                activity.onGetDistanceAndTime(distance.getText(), duration.getText());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
            }
        });
    }

    public interface OnGetRouteCallback { void onGetRoute(String points); }

    public interface OnGetDistanceAndTimeCallback { void onGetDistanceAndTime(String distance, String duration); }

    private static String pointToString(LatLng point) {
        return String.format(Locale.US, "%f,%f", point.latitude, point.longitude);
    }
}
