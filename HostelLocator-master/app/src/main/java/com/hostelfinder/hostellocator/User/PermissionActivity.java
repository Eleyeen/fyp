package com.hostelfinder.hostellocator.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.hostelfinder.hostellocator.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class PermissionActivity extends AppCompatActivity {

    private Button grantPermission;
    double latidude;
    double longitude;
    String hostelname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_permission);

        latidude = getIntent().getDoubleExtra("latitude", 0.00);
        longitude = getIntent().getDoubleExtra("longitude", 0.00);
        hostelname = getIntent().getStringExtra("hostelname");

        grantPermission = findViewById(R.id.btn_grant_permission);

        grantPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(PermissionActivity.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                    Intent intent = new Intent(getApplicationContext(), Map_HostelLocation.class);
                                    intent.putExtra("latitude", latidude);
                                    intent.putExtra("longitude", longitude);
                                    intent.putExtra("hostelname", hostelname);
                                    startActivity(intent);
                                Toast.makeText(PermissionActivity.this, "Permission Accepted!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                if (permissionDeniedResponse.isPermanentlyDenied()){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PermissionActivity.this);
                                    builder.setTitle("Permission Denied")
                                            .setMessage("Permission to access device location is permanently denied. You need to go to Settings to allow the Permissions")
                                            .setNegativeButton("Cancel", null)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    intent.setData(Uri.fromParts("Package", getPackageName(), null));
                                                }
                                            }).show();
                                }
                                else {
                                    Toast.makeText(PermissionActivity.this, "Permission Denied!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

    }
}