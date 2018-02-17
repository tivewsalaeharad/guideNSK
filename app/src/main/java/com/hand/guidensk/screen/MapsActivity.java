package com.hand.guidensk.screen;

import android.content.Intent;
import android.database.Cursor;
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
import com.hand.guidensk.db.DB;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String KEY_TAG = "Tag";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_PRE_DESCRIPTION = "PreDescription";
    private static final String KEY_DESCRIPTION = "Description";
    private static final String KEY_ADDRESS = "Address";
    private static final String KEY_PHONE = "Phone";
    private static final String KEY_WEBSITE = "Website";
    private static final String KEY_OPENED = "Opened";
    private static final String KEY_CLOSED = "Closed";
    private static final String KEY_BREAK_START = "Break start";
    private static final String KEY_BREAK_END = "Break end";
    private static final String KEY_LATITUDE = "Latitude";
    private static final String KEY_LONGITUDE = "Longitude";
    private static final String KEY_FILTER = "Filter";

    private int filter;
    private GoogleMap mMap;
    private int[]  markers = {
            R.drawable.marker_dinner,
            R.drawable.marker_hotel,
            R.drawable.marker_cinema,
            R.drawable.marker_shopping,
            R.drawable.marker_theatre,
            R.drawable.marker_museum,
            R.drawable.marker_science,
            R.drawable.marker_music
    };
    private String[] categories = {"Где поесть", "Где остановиться", "Кино", "Шопинг", "Театры",
            "Музеи", "Интересные места", "Развлечения"};

    float zoomLevel = 11.0f;
    double maxLat = 0, minLat = 200, maxLng = 0, minLng = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        filter = intent.getIntExtra(KEY_FILTER, -1);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng center;
        Cursor cursor;
        if (filter == -1) cursor = DB.db.rawQuery("SELECT * FROM sites", null);
        else cursor = DB.db.rawQuery("SELECT * FROM sites WHERE category=?", new String[] {filter + ""});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) putMarkerFromCursor(cursor);
        cursor.close();
        center = new LatLng((minLat + maxLat)/2, (minLng + maxLng)/2);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int tag = (int) marker.getTag();
        Cursor cursor = DB.db.rawQuery("SELECT * FROM sites WHERE _id=?", new String[] {tag + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Intent intent = new Intent(this, PlaceActivity.class);
            intent.putExtra(KEY_TAG, tag);
            intent.putExtra(KEY_TITLE, cursor.getString(1));
            intent.putExtra(KEY_PRE_DESCRIPTION, cursor.getString(2));
            intent.putExtra(KEY_DESCRIPTION, cursor.getString(3));
            intent.putExtra(KEY_ADDRESS, cursor.getString(4));
            intent.putExtra(KEY_PHONE, cursor.getString(5));
            intent.putExtra(KEY_WEBSITE, cursor.getString(6));
            intent.putExtra(KEY_OPENED, cursor.getString(7));
            intent.putExtra(KEY_CLOSED, cursor.getString(8));
            intent.putExtra(KEY_BREAK_START, cursor.getString(9));
            intent.putExtra(KEY_BREAK_END, cursor.getString(10));
            intent.putExtra(KEY_LATITUDE, cursor.getString(10));
            intent.putExtra(KEY_LONGITUDE, cursor.getString(10));
            startActivity(intent);
        }
        cursor.close();
        return false;
    }

    private void putMarkerFromCursor(Cursor cursor) {
        try {
            int tag = cursor.getInt(0);
            int category = cursor.getInt(13);
            float lat = cursor.getFloat(11);
            float lng = cursor.getFloat(12);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .icon(BitmapDescriptorFactory.fromResource(markers[category])));
            marker.setTag(tag);
            if (lat < minLat) minLat = lat; if (lat > maxLat) maxLat = lat;
            if (lng < minLng) minLng = lng; if (lng > maxLng) maxLng = lng;
            cursor.moveToNext();
        } catch (Exception ignore) {
            cursor.moveToNext();
        }
    }
}
