package com.hostelfinder.hostellocator.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.InputType;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.hostelfinder.hostellocator.HelperClasses.Adapter_ForListOfHostels;
import com.hostelfinder.hostellocator.HelperClasses.Adapter_Nearby_Hostels;
import com.hostelfinder.hostellocator.HelperClasses.CheckInternetConnection;
import com.hostelfinder.hostellocator.HelperClasses.Data_Model_Class;
import com.hostelfinder.hostellocator.HelperClasses.HolderClass_Hostel_Info;
import com.hostelfinder.hostellocator.HelperClasses.MyViewHolder;
import com.hostelfinder.hostellocator.HelperClasses.RecyclerAdapterClass_HostelInfo;
import com.hostelfinder.hostellocator.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Nearby_Hostels_List extends AppCompatActivity {
    RecyclerView recyclerView;
    /* Adapter_Nearby_Hostels adapter_nearby_hostels;*/
    RecyclerAdapterClass_HostelInfo recyclerAdapterClass_hostelInfo;
    ProgressBar progressBar;
    SearchView searchView;
    private LocationManager locationManager;
    List<HolderClass_Hostel_Info> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_nearby_hostels_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        recyclerView = (RecyclerView) findViewById(R.id.hostelslistrecylcerview);
        progressBar = findViewById(R.id.progressbar);
        searchView = findViewById(R.id.list_searchbar);

        checkLocation();
        locationpermission();


        String city = getIntent().getStringExtra("Cityselected");
        searchView.setQuery(city.toLowerCase(), true);
        /*searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);*/
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCity(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                FirebaseRecyclerOptions<HolderClass_Hostel_Info> options =
                        new FirebaseRecyclerOptions.Builder<HolderClass_Hostel_Info>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("Hostels").orderByChild("hostelcity").startAt(newText).endAt(newText + "\uf8ff"), HolderClass_Hostel_Info.class)
                                .build();

                recyclerAdapterClass_hostelInfo = new RecyclerAdapterClass_HostelInfo(options, Nearby_Hostels_List.this);
                recyclerAdapterClass_hostelInfo.startListening();
                recyclerView.setAdapter(recyclerAdapterClass_hostelInfo);
                return false;
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<HolderClass_Hostel_Info> options =
                new FirebaseRecyclerOptions.Builder<HolderClass_Hostel_Info>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Hostels"), HolderClass_Hostel_Info.class)
                        .build();

        recyclerAdapterClass_hostelInfo = new RecyclerAdapterClass_HostelInfo(options, this);
        recyclerView.setAdapter(recyclerAdapterClass_hostelInfo);


    }

    private void searchCity(String query) {

        FirebaseRecyclerOptions<HolderClass_Hostel_Info> options =
                new FirebaseRecyclerOptions.Builder<HolderClass_Hostel_Info>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Hostels").orderByChild("hostelcity").startAt(query).endAt(query + "\uf8ff"), HolderClass_Hostel_Info.class)
                        .build();
        recyclerAdapterClass_hostelInfo = new RecyclerAdapterClass_HostelInfo(options, this);
        recyclerAdapterClass_hostelInfo.startListening();
        recyclerView.setAdapter(recyclerAdapterClass_hostelInfo);


    }


    private void showAlert() {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setCancelable(false)
                .setMessage("GPS is off. \nPlease Enable Location to " + "use this app")
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), UserDashboard.class);
                        startActivity(i);
                        finish();
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    private boolean checkLocation() {

        if (!isLocationEnabled()) {
            showAlert();

        }
        return isLocationEnabled();
    }


    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapterClass_hostelInfo.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapterClass_hostelInfo.stopListening();
    }

    public void locationpermission() {
        Dexter.withContext(Nearby_Hostels_List.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


}