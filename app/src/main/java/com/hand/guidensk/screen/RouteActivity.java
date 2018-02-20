package com.hand.guidensk.screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.hand.guidensk.R;
import com.hand.guidensk.constant.Key;
import com.hand.guidensk.model.Route;
import com.hand.guidensk.model.RouteResponse;
import com.hand.guidensk.network.ApiFactory;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng user;
    private LatLng place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        user = new LatLng(
                intent.getDoubleExtra(Key.USER_LATITUDE, 55.0302577),
                intent.getDoubleExtra(Key.USER_LONGITUDE, 82.9233965));
        place = new LatLng(
                intent.getDoubleExtra(Key.PLACE_LATITUDE, 55.075086),
                intent.getDoubleExtra(Key.PLACE_LONGITUDE, 82.908811));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        putMarkers();
        drawRoute();
    }

    private void putMarkers() {
        mMap.addMarker(new MarkerOptions()
                .position(user)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_user)));
        mMap.addMarker(new MarkerOptions()
                .position(place)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_place)));
    }

    private void drawRoute() {
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
                        drawRouteFromString(routes.get(0).getOverviewPolyline().getPoints());
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
            }
        });
    }

    private void drawRouteFromString(String points) {
        List<LatLng> mPoints = PolyUtil.decode(points);
        PolylineOptions line = new PolylineOptions();
        line.width(4f).color(R.color.navy);
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        for (int i = 0; i < mPoints.size(); i++) {
            line.add(mPoints.get(i));
            latLngBuilder.include(mPoints.get(i));
        }
        mMap.addPolyline(line);
        setCameraAndZoomToBuilder(latLngBuilder);
    }

    private void setCameraAndZoomToBuilder(LatLngBounds.Builder builder) {
        int size = getResources().getDisplayMetrics().widthPixels;
        LatLngBounds latLngBounds = builder.build();
        CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 25);
        mMap.moveCamera(track);
    }

    private String pointToString(LatLng point) {
        return String.format(Locale.US, "%f,%f", point.latitude, point.longitude);
    }
}
