package com.hostelfinder.hostellocator.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hostelfinder.hostellocator.Common.All_Chats;
import com.hostelfinder.hostellocator.Common.LoginSignUp.Login;
import com.hostelfinder.hostellocator.Common.LoginSignUp.User_Startup_Screen;
import com.hostelfinder.hostellocator.Common.Message_Activity;
import com.hostelfinder.hostellocator.Common.The_Dashboard;
import com.hostelfinder.hostellocator.HelperClasses.CheckInternetConnection;
import com.hostelfinder.hostellocator.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailedHostel extends AppCompatActivity /*implements OnMapReadyCallback*/ {

    ImageView HostelImage;
    TextView HostelName, Hostel_Description, DistanceCalculated, CityName, WardenName, WardenPhone, completeLocation;
    double HostelLatitude;
    double HostelLongitude;

    private LocationManager locationManager;
    /* double currentlat;
     double currentlong;*/
    String hostelname;
    ImageButton chat, call, dislike;
    String HostelWardenName;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String WardenUniqueIDinDataBase;
    String image, Hostelnameee;

    String Wardenph;
    private GoogleMap mMap;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_detailed_hostel);

        chat = (ImageButton) findViewById(R.id.chats);
        call = (ImageButton) findViewById(R.id.call);
        WardenName = (TextView) findViewById(R.id.wardennametext);
        WardenPhone = (TextView) findViewById(R.id.wardennumber);
        HostelImage = (ImageView) findViewById(R.id.hostelimages);
        Hostel_Description = (TextView) findViewById(R.id.hostelDesc);
        DistanceCalculated = (TextView) findViewById(R.id.distancecalculated);
        CityName = (TextView) findViewById(R.id.cityname);
        HostelName = (TextView) findViewById(R.id.hosstelname);
        completeLocation = findViewById(R.id.hosteldesctext);
        Hostel_Description.setMovementMethod(new ScrollingMovementMethod());

        HostelWardenName = getIntent().getStringExtra("wardenname");
        Hostelnameee = getIntent().getStringExtra("hosstelname").toLowerCase();
        image = getIntent().getStringExtra("hostelimage");
        completeLocation.setText(getIntent().getStringExtra("completeLocationAdd"));

        HostelLatitude = getIntent().getDoubleExtra("latitude", 0.00);
        HostelLongitude = getIntent().getDoubleExtra("longitude", 0.00);

        hostelname = getIntent().getStringExtra("hosstelname");
        WardenUniqueIDinDataBase = getIntent().getStringExtra("wardenUid");

        WardenPhone.setText(getIntent().getStringExtra("wardenphone"));
        Wardenph = getIntent().getStringExtra("wardenphone");
        Hostel_Description.setText(getIntent().getStringExtra("description"));
        CityName.setText(getIntent().getStringExtra("cityname"));
        WardenName.setText(getIntent().getStringExtra("wardenname"));
        HostelName.setText(getIntent().getStringExtra("hosstelname").toLowerCase());
        Glide.with(this).load(image).into(HostelImage);

        checkLocation();

        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Location location) {

                if (location != null) {

                    double currentlat = location.getLatitude();
                    double currentlong = location.getLongitude();

                    /*final double hoslat = HostelLatitude;
                    final double hoslong = HostelLongitude;*/

                    float[] calculated = new float[1];
                    Location.distanceBetween(currentlat, currentlong, HostelLatitude, HostelLongitude, calculated);
                    float distance = calculated[0];
                    float inKm = distance / 1000;
                   /* float[] results = new float[1];
                    Location.distanceBetween(currentlat, currentlong, HostelLatitude, HostelLongitude, results);
                    float distance = results[0];
                    float kilometer = distance / 1000;
*/
                    DistanceCalculated.setText(new DecimalFormat("##.##").format(inKm) + " Km away");
                }
            }
        });
        DistanceCalculated.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailedHostel.this);
                builder.setMessage("Latitude : " + HostelLatitude + "\n" + "Longitude :" + HostelLongitude);
                builder.setCancelable(true);
                builder.show();
                return false;
            }
        });

        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);*/

      /*  int hostelImageurl = getIntent().getExtras().getInt("Hostel_Image");
        Glide.with(this).load(hostelImageurl).into(HostelImage);*/

       /* HostelImage.setImageResource(getIntent().getExtras().getInt("Hostel_Image"));
        HostelName.setText(getIntent().getExtras().getString("Hostel_Name"));
        Hostel_Description.setText(getIntent().getExtras().getString("Hostel_Description"));
        CityName.setText(getIntent().getExtras().getString("CityName"));
        Latitude = Double.parseDouble(getIntent().getExtras().getString("Latitude"));
        Longitude = Double.parseDouble(getIntent().getExtras().getString("Longitude"));*/

        /*DistanceCalculated.setText(getIntent().getExtras().getString("DistanceCalculated"));*/

    }

    public void ShowInMap(View view) {

        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        if (checkInternetConnection.isConnected(this)) {
            //////////// firest sent user to permission activity
            Intent gotomap = new Intent(getApplicationContext(), PermissionActivity.class);
            gotomap.putExtra("latitude", HostelLatitude);
            gotomap.putExtra("longitude", HostelLongitude);
            gotomap.putExtra("hostelname", hostelname);
            startActivity(gotomap);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailedHostel.this);
            builder.setTitle("No Internet Connection")
                    .setCancelable(false)
                    .setMessage("Please turn on your internet connection to access the list of hostels")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                            finish();
                        }
                    })
                    .setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    }).show();
        }
    }


    public void Callwarden(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedHostel.this);
        builder.setTitle("Call?")
                .setCancelable(false)
                .setMessage("Do you want to call the Warden of this Hostel?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent call = new Intent(Intent.ACTION_DIAL);
                        call.setData(Uri.parse("tel:" + Wardenph));
                        startActivity(call);
                    }
                }).show();


    }


    public void Chat(View view) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            Intent chats = new Intent(getApplicationContext(), Message_Activity.class);
            chats.putExtra("userid", WardenUniqueIDinDataBase);
            chats.putExtra("wardenname", HostelWardenName);
            chats.putExtra("userimage", image);
            chats.putExtra("Hostelnameee", Hostelnameee);
            startActivity(chats);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailedHostel.this);
            builder.setTitle("Please Login")
                    .setCancelable(true)
                    .setMessage("Please Login first to continue..")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent gotologin = new Intent(getApplicationContext(), User_Startup_Screen.class);
                            startActivity(gotologin);
                        }
                    }).show();
        }
    }

    private boolean checkLocationPermission() {
        Dexter.withContext(DetailedHostel.this)
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
                });
        return true;
    }

    public void CheckDistance() {
        Dexter.withContext(DetailedHostel.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        if (ActivityCompat.checkSelfPermission(DetailedHostel.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DetailedHostel.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    final double currentlat = location.getLatitude();
                                    final double currentlong = location.getLongitude();

                                    final double hoslat = HostelLatitude;
                                    final double hoslong = HostelLongitude;
                                    float[] results = new float[1];
                                    Location.distanceBetween(currentlat, currentlong, hoslat, hoslong, results);
                                    float distance = results[0];
                                    float kilometer = distance / 100;

                                    DistanceCalculated.setText(kilometer + " Km away");
                                }
                            }
                        });

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


    private void showAlert() {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("GPS is OFF. \nPlease Enable Location to " + "use this app")
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


}