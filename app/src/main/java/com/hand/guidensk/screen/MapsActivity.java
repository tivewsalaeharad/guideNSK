package com.hand.guidensk.screen;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hand.guidensk.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 12.0f;
        LatLng myLatLng = new LatLng(MainActivity.latitude, MainActivity.longitude);
        Marker myLocation = mMap.addMarker(new MarkerOptions()
                .position(myLatLng)
                .title("Вы находитесь здесь")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_user)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, zoomLevel));
    }
}
