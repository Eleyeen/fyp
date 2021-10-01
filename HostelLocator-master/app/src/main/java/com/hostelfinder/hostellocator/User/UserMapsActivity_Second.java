package com.hostelfinder.hostellocator.User;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.hostelfinder.hostellocator.R;

public class UserMapsActivity_Second extends FragmentActivity implements OnMapReadyCallback {

    private final static int REQUEST_CODE = 101;
    GoogleMap mMap;
    Location mLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    Double Latitudeee;
    Double Longitudeee;
    String HostelNameee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_maps_activity_second);


        TextView lat = findViewById(R.id.latitude);
        TextView lon = findViewById(R.id.longitude);
        TextView name = findViewById(R.id.name);

        /*Latitudeee = Double.valueOf(getIntent().getStringExtra("latitude"));
        Longitudeee = Double.valueOf(getIntent().getStringExtra("longitude"));
        HostelNameee = getIntent().getStringExtra("hostelname");

        lat.setText(Latitudeee.toString());
        lon.setText(Longitudeee.toString());
        name.setText(HostelNameee);*/


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        /* getLastLocation();*/

    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mLocation = location;
                    Toast.makeText(UserMapsActivity_Second.this, mLocation.getLatitude() + "\n" + mLocation.getLongitude(), Toast.LENGTH_LONG).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.google_map);
                    /*supportMapFragment.getMapAsync(this);*/
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /* LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());*/
        LatLng latLng = new LatLng(Latitudeee, Longitudeee);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(HostelNameee);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                }
                break;
        }
    }
}