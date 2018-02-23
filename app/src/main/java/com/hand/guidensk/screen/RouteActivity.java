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
import com.hand.guidensk.network.RouteManager;

import java.util.List;

public class RouteActivity extends FragmentActivity implements OnMapReadyCallback, RouteManager.OnGetRouteCallback {

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
        mMap.addMarker(new MarkerOptions()
                .position(user)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_user)));
        mMap.addMarker(new MarkerOptions()
                .position(place)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_place)));
        RouteManager.getRoute(this, user, place);
    }

    @Override
    public void onGetRoute(String points) {
        List<LatLng> mPoints = PolyUtil.decode(points);
        PolylineOptions line = new PolylineOptions();
        line.width(4f).color(R.color.navy);
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        for (int i = 0; i < mPoints.size(); i++) {
            line.add(mPoints.get(i));
            latLngBuilder.include(mPoints.get(i));
        }
        mMap.addPolyline(line);
        int size = getResources().getDisplayMetrics().widthPixels;
        LatLngBounds latLngBounds = latLngBuilder.build();
        CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 25);
        mMap.moveCamera(track);
    }

}
