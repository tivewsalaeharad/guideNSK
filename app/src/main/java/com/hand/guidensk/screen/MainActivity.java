package com.hand.guidensk.screen;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hand.guidensk.R;
import com.hand.guidensk.constant.Code;
import com.hand.guidensk.constant.Key;
import com.hand.guidensk.db.DB;
import com.hand.guidensk.fragment.CategoryFragment;
import com.hand.guidensk.fragment.RootFragment;
import com.hand.guidensk.fragment.ToSeeFragment;
import com.hand.guidensk.network.RouteManager;
import com.hand.guidensk.utils.FavouritesUtils;
import com.hand.guidensk.utils.PermissionUtils;
import com.hand.guidensk.dialog.PermissionDialog;
import com.stephentuso.welcome.WelcomeHelper;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionDialog.GetPermissionListener,
        View.OnClickListener{

    private static final String TAG_PERMISSION_DIALOG = "PermissionDialog";
    private static final int LOCATION_UPDATE_FREQUENCY = 5000;
    private static Boolean gpsEnabled;
    public static double latitude;
    public static double longitude;
    public static Marker mapsMarker;
    public static Marker routeMarker;
    public static RouteActivity routeActivity;
    public static PlaceActivity placeActivity;
    private FusedLocationProviderClient mFusedLocationClient;
    private Timer timer;
    private DrawerLayout mainLayout;
    private ProgressBar progressBar;
    private Menu navigationMenu;
    private CategoryFragment dinnerFragment;
    private CategoryFragment hotelFragment;
    private ToSeeFragment toSeeFragment;
    private CategoryFragment cinemaFragment;
    private CategoryFragment shoppingFragment;
    private CategoryFragment theatreFragment;
    private CategoryFragment museumFragment;
    private CategoryFragment interestFragment;
    private CategoryFragment entertainmentFragment;
    private FragmentTransaction transaction;
    WelcomeHelper welcomeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Демонстрация вводного экрана
        welcomeScreen = new WelcomeHelper(this, GuideWelcomeActivity.class);
        welcomeScreen.show(savedInstanceState);

        //Настройка панелей
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle("Выбор категории");
        mainLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mainLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Настройка навигационного меню
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        navigationMenu = navigationView.getMenu();

        //Установка содержания экрана
        RootFragment rootFragment = new RootFragment();
        dinnerFragment = new CategoryFragment(); dinnerFragment.category = 0;
        hotelFragment = new CategoryFragment(); hotelFragment.category = 1;
        toSeeFragment = new ToSeeFragment();
        cinemaFragment = new CategoryFragment(); cinemaFragment.category = 2;
        shoppingFragment = new CategoryFragment(); shoppingFragment.category = 3;
        theatreFragment = new CategoryFragment(); theatreFragment.category = 4;
        museumFragment = new CategoryFragment(); museumFragment.category = 5;
        interestFragment = new CategoryFragment(); interestFragment.category = 6;
        entertainmentFragment = new CategoryFragment(); entertainmentFragment.category = 7;
        transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.container_fragments, rootFragment);
        transaction.commit();

        //Настройка геолокации
        progressBar = findViewById(R.id.progress_bar);
        gpsEnabled = checkGPSEnabled();
        if(!gpsEnabled) {
            PermissionDialog fragment = new PermissionDialog();
            fragment.show(getFragmentManager(), TAG_PERMISSION_DIALOG);
        }
        else waitMessage();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (timer != null) timer.cancel();
        timer = new Timer();
        MyTimerTask timerTask = new MyTimerTask();
        timerTask.activity = this;
        timer.schedule(timerTask, LOCATION_UPDATE_FREQUENCY, LOCATION_UPDATE_FREQUENCY);

        //Настройка баз данных
        DB.init(this);

        //Загрузка избранного
        FavouritesUtils.get(this);
    }

    @Override
    protected void onStop() {
        FavouritesUtils.put(this);
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        transaction = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.dinner:
                transaction.replace(R.id.container_fragments, dinnerFragment);
                break;
            case R.id.hotel:
                transaction.replace(R.id.container_fragments, hotelFragment);
                break;
            case R.id.to_see:
                transaction.replace(R.id.container_fragments, toSeeFragment);
                break;
            case R.id.cinema:
                transaction.replace(R.id.container_fragments, cinemaFragment);
                break;
            case R.id.shopping:
                transaction.replace(R.id.container_fragments, shoppingFragment);
                break;
            case R.id.theatre:
                transaction.replace(R.id.container_fragments, theatreFragment);
                break;
            case R.id.museum:
                transaction.replace(R.id.container_fragments, museumFragment);
                break;
            case R.id.interest:
                transaction.replace(R.id.container_fragments, interestFragment);
                break;
            case R.id.entertainment:
                transaction.replace(R.id.container_fragments, entertainmentFragment);
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_map:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(Key.FILTER, Code.FILTER_ALL);
                startActivity(intent);
                break;
            case R.id.nav_dinner:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(Key.FILTER, Code.FILTER_DINNER);
                startActivity(intent);
                break;
            case R.id.nav_hotel:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(Key.FILTER, Code.FILTER_HOTEL);
                startActivity(intent);
                break;
            case R.id.nav_cinema:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(Key.FILTER, Code.FILTER_CINEMA);
                startActivity(intent);
                break;
            case R.id.nav_shopping:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(Key.FILTER, Code.FILTER_SHOPPING);
                startActivity(intent);
                break;
            case R.id.nav_theatre:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(Key.FILTER, Code.FILTER_THEATRE);
                startActivity(intent);
                break;
            case R.id.nav_museum:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(Key.FILTER, Code.FILTER_MUSEUM);
                startActivity(intent);
                break;
            case R.id.nav_interest:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(Key.FILTER, Code.FILTER_INTEREST);
                startActivity(intent);
                break;
            case R.id.nav_entertainment:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(Key.FILTER, Code.FILTER_ENTERTAINMENT);
                startActivity(intent);
                break;
            case R.id.nav_favourites:
                intent = new Intent(this, FavouritesActivity.class);
                startActivity(intent);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //Уведомление о состоянии GPS
        if (hasFocus) {
            if (gpsChanged()) {
                gpsEnabled = !gpsEnabled;
                if (gpsEnabled) waitMessage();
            }
            if (!gpsEnabled) gpsTurnedOffMessage();
        }
    }

    @Override
    public void onGetPermissionFromDialog(boolean confirmed) {
        if (confirmed) startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        else gpsTurnedOffMessage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtils.CODE_PERMISSION_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mainLayout, R.string.location_permission_granted, Snackbar.LENGTH_SHORT).show();
                setLocation();
            } else {
                Snackbar.make(mainLayout, R.string.location_permission_denied, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void setLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissionWithExplanation();
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    progressBar.setVisibility(View.GONE);
                    navigationMenu.setGroupEnabled(R.id.group_map, true);
                    LatLng point = new LatLng(latitude, longitude);
                    if (mapsMarker != null) mapsMarker.setPosition(point);
                    if (routeMarker != null) routeMarker.setPosition(point);
                    if (routeActivity != null) RouteManager.getRoute(routeActivity, point, routeActivity.place);
                    if (placeActivity != null) RouteManager.getDistanceAndTime(placeActivity, point,
                            new LatLng(placeActivity.latitude, placeActivity.longitude));
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    navigationMenu.setGroupEnabled(R.id.group_map, false);
                }
            }
        });
    }

    private void requestLocationPermissionWithExplanation() {
        if (PermissionUtils.shouldAskLocationPermission(this)) {
            Snackbar.make(mainLayout, "Required access to determine location",
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PermissionUtils.requestLocationPermissions(MainActivity.this);
                }
            }).show();
        } else {
            PermissionUtils.requestLocationPermissions(this);
        }
    }

    private boolean checkGPSEnabled(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        return provider.contains("gps");
    }

    private boolean gpsChanged() {
        return gpsEnabled != checkGPSEnabled();
    }

    private void waitMessage() {
        Snackbar.make(mainLayout, R.string.wait_message, Snackbar.LENGTH_LONG).setAction(R.string.ok, null).show();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void gpsTurnedOffMessage() {
        Snackbar.make(mainLayout, R.string.gps_turned_off, Snackbar.LENGTH_SHORT).setAction(R.string.ok, null).show();
        navigationMenu.setGroupEnabled(R.id.group_map, false);
        progressBar.setVisibility(View.GONE);
    }

    private class MyTimerTask extends TimerTask {
        MainActivity activity;
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(gpsEnabled) activity.setLocation();
                    else {
                        progressBar.setVisibility(View.GONE);
                        //navigationMenu.getItem(1).setEnabled(false);
                        navigationMenu.setGroupEnabled(R.id.group_map, false);
                    }
                }
            });
        }
    }

}
