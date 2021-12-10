package com.project.plantappui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

//@TODO experiment with this official Google MapFragment. For debugging purposes stick with the placeholder below
// import com.google.android.gms.maps.MapFragment;
import com.google.android.material.navigation.NavigationView;
import com.project.plantappui.menu.camera.CameraFragment;
import com.project.plantappui.menu.map.MapFragment;
import com.project.plantappui.menu.home.HomeFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;
    AnimatedBottomBar animatedBottomBar;
    FragmentManager fragmentManager;

    /* for location */
    // store longitude and latitude
    Map<String, Double> locationMap = new HashMap<String, Double>(){
        {put("longitude", -1.0);
        put("latitude", -1.0);}
    };
    private LocationManager locationManager;
    private String provider;// position provider


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        initViews(savedInstanceState);
        initComponentsNavHeader();

        getLocation();
        // test: show position
        Toast.makeText(getApplicationContext(), locationMap.get("longitude")+","+locationMap.get("latitude"),Toast.LENGTH_LONG).show();

    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(0);
    }

    @SuppressLint("NonConstantResourceId")
    private void initViews(Bundle savedInstanceState) {
        /**
         * Menu Bottom Navigation Drawer
         * */
        animatedBottomBar = findViewById(R.id.navigation);

        if (savedInstanceState == null) {
            animatedBottomBar.selectTabById(R.id.nav_menu_home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment)
                    .commit();
        }

        animatedBottomBar.setOnTabSelectListener((lastIndex, lastTab, newIndex, newTab) -> {
            Fragment fragment = null;
            boolean no_need_for_frag = false;
            switch (newTab.getId()) {
                case R.id.nav_menu_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.nav_map:
                    fragment = new MapFragment();
                    break;
                case R.id.nav_camera:
                    fragment = new CameraFragment();
                    break;
                case R.id.nav_network:
                    Intent intent = new Intent(getApplicationContext(), NetworkActivity.class);
                    startActivity(intent);
                    no_need_for_frag = true;
                    break;
            }

            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                        .commit();
            } else {
                if (!no_need_for_frag) {
                    Log.e(TAG, "Error in creating Fragment");
                }
            }
        });

        /**
         * Menu Navigation Drawer
         **/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(view -> drawer.openDrawer(GravityCompat.START));
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        toggle.syncState();
    }

    private void initComponentsNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setItemIconTintList(null); //disable tint on each icon to use color icon svg
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.my_favorite_drawer:
                        Pesan("Menu My Favorite");
                        Intent intent = new Intent(getApplicationContext(), FavoriteListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.my_location_drawer:
                        Pesan("Menu My Locations");
                        break;
                    case R.id.my_lib_drawer:
                        Pesan("Menu My Library");
                        break;
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            private void Pesan(String pesan) {
                Toast.makeText(MainActivity.this, pesan, Toast.LENGTH_SHORT).show();
            }
        });
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

    /*----------- Location relevant - start --------------*/
    public void getLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // update current location

        // Get all available position provider
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            // no position provider available
            Toast.makeText(MainActivity.this, "no position provider available", Toast.LENGTH_LONG).show();
            locationMap.put("longitude", -1.0);
            locationMap.put("latitude", -1.0);
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return ;
        }
        locationManager.requestLocationUpdates(provider, 10 * 1000, 1,locationListener);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            // show current location
            locationMap.put("longitude", location.getLongitude());
            locationMap.put("latitude", location.getLatitude());
        }
    }


    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onLocationChanged(Location location) {
            locationMap.put("longitude", location.getLongitude());
            locationMap.put("latitude", location.getLatitude());
        }
    };
    /*----------- Location relevant - end --------------*/

    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    };


}