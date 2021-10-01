package com.hostelfinder.hostellocator.User;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.hostelfinder.hostellocator.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import javax.net.ssl.ManagerFactoryParameters;

public class Map_HostelLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double Latitudeee;
    double Longitudeee;
    private String HostelNameee;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_user_hostel_location);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Latitudeee = getIntent().getDoubleExtra("latitude", 0.00);
        Longitudeee = getIntent().getDoubleExtra("longitude", 0.00);
        HostelNameee = getIntent().getStringExtra("hostelname");

        spinner = (Spinner) findViewById(R.id.spinner);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String MapTypes[] = {"Click to select map type", "Normal", "Satellite", "Terrain", "Hybrid"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MapTypes);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().equals("Normal")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (spinner.getSelectedItem().equals("Satellite")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (spinner.getSelectedItem().equals("Terrain")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                } else if (spinner.getSelectedItem().equals("Hybrid")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else {
                    Toast.makeText(Map_HostelLocation.this, "You can change map type", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);*/
        LatLng hostelCoordinates = new LatLng(Latitudeee, Longitudeee);
        MarkerOptions markerOptions = new MarkerOptions().position(hostelCoordinates).title(HostelNameee);
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hostelCoordinates, 14f));

    }

    ////////////// code for current location

    private void CheckMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Your Location");
                        mMap.addMarker(markerOptions);
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                    }
                });

            }
        });
    }


}