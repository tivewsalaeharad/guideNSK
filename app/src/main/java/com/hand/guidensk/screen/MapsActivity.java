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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.hand.guidensk.R;
import com.hand.guidensk.db.DB;
import com.hand.guidensk.models.Route;
import com.hand.guidensk.models.RouteResponse;
import com.hand.guidensk.network.ApiFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Cursor cursor;
        if (filter == -1) cursor = DB.db.rawQuery("SELECT * FROM sites", null);
        else cursor = DB.db.rawQuery("SELECT * FROM sites WHERE category=?", new String[] {filter + ""});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LatLng point = putMarkerFromCursor(cursor);
            if (point != null) builder.include(point);
        }
        cursor.close();
        setCameraAndZoomToBuilder(builder);
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

    private LatLng putMarkerFromCursor(Cursor cursor) {
       try {
            LatLng point = new LatLng(cursor.getFloat(11), cursor.getFloat(12));
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .icon(BitmapDescriptorFactory.fromResource(markers[cursor.getInt(13)])));
            marker.setTag(cursor.getInt(0));
            cursor.moveToNext();
            return point;
       } catch (Exception ignore) {
            cursor.moveToNext();
            return null;
        }
    }

    private void drawRoute() {
        Call<RouteResponse> call = ApiFactory.getService().getRoute(
                "55.0302577,82.9233965",
                "55.075086,82.908811",
                true,
                "ru");
        call.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if (response.isSuccessful()) {
                    List<Route> routes = response.body().getRoutes();
                    if ((routes != null) && (routes.size()>0))
                        drawRouteFromString(routes.get(0).getOverviewPolyline().getPoints());
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {}
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
}
