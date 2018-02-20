package com.hand.guidensk.screen;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import com.google.maps.android.clustering.ClusterManager;
import com.hand.guidensk.R;
import com.hand.guidensk.cluster.PlaceItem;
import com.hand.guidensk.cluster.PlaceRenderer;
import com.hand.guidensk.constant.Key;
import com.hand.guidensk.constant.S;
import com.hand.guidensk.db.DB;
import com.hand.guidensk.utils.ShowPlaceUtils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private int filter;
    private GoogleMap mMap;
    private ClusterManager<PlaceItem> manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        filter = intent.getIntExtra(Key.FILTER, -1);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        manager = new ClusterManager<>(this, mMap);
        manager.setRenderer(new PlaceRenderer(this, mMap, manager));
        manager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<PlaceItem>() {
            @Override
            public boolean onClusterItemClick(PlaceItem placeItem) {
                ShowPlaceUtils.showPlace(MapsActivity.this, placeItem.getIndex());
                return true;
            }
        });
        mMap.setOnCameraChangeListener(manager);
        mMap.setOnMarkerClickListener(manager);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Cursor cursor;
        if (filter == -1) cursor = DB.db.rawQuery(S.SQL_ALL, null);
        else cursor = DB.db.rawQuery(S.SQL_CATEGORY, new String[] {filter + ""});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LatLng point = putMarkerFromCursor(cursor);
            if (point != null) builder.include(point);
        }
        cursor.close();
        setCameraAndZoomToBuilder(builder);
    }

    private LatLng putMarkerFromCursor(Cursor cursor) {
       try {
           LatLng point = new LatLng(cursor.getFloat(11), cursor.getFloat(12));

           PlaceItem item = new PlaceItem(point.latitude, point.longitude, cursor.getInt(13), cursor.getInt(0));
           manager.addItem(item);
           cursor.moveToNext();
           return point;
       } catch (Exception ignore) {
           cursor.moveToNext();
           return null;
        }
    }

    private void setCameraAndZoomToBuilder(LatLngBounds.Builder builder) {
        int size = getResources().getDisplayMetrics().widthPixels;
        LatLngBounds latLngBounds = builder.build();
        CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 25);
        mMap.moveCamera(track);
    }
}
