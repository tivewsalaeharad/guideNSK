package com.hand.guidensk.screen;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hand.guidensk.R;
import com.hand.guidensk.db.DB;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int[] markers;
    private String[] categories = {"Где поесть", "Где остановиться", "Кино", "Шопинг", "Театры",
            "Музеи", "Интересные места", "Развлечения"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        markers = new int[] {
                R.drawable.marker_dinner,
                R.drawable.marker_hotel,
                R.drawable.marker_art,
                R.drawable.marker_shop,
                R.drawable.marker_music,
                R.drawable.marker_science,
                R.drawable.marker_science,
                R.drawable.marker_music
        };
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 11.0f, lat, lng;
        float maxlat = 0.0f, minlat = 200.0f, maxlng = 0.0f, minlng = 100.0f;
        String caption;
        LatLng center;
        int category;
        Cursor cursor = DB.db.rawQuery("SELECT * FROM sites", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) try {
            caption = cursor.getString(1);
            category = cursor.getInt(13);
            lat = cursor.getFloat(11); if (lat < minlat) minlat = lat; if (lat > maxlat) maxlat = lat;
            lng = cursor.getFloat(12); if (lng < minlng) minlng = lng; if (lng > maxlng) maxlng = lng;
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(caption)
                    .icon(BitmapDescriptorFactory.fromResource(markers[category])));
            Log.d("myLogs", caption + " " + lat + " " + lng);
            cursor.moveToNext();
        } catch (Exception ignore) { }
        cursor.close();
        center = new LatLng((minlat + maxlat)/2, (minlng + maxlng)/2);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
    }
}
