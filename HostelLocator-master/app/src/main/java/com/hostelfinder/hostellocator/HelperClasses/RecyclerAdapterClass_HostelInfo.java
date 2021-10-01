package com.hostelfinder.hostellocator.HelperClasses;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.DetailedHostel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class RecyclerAdapterClass_HostelInfo extends FirebaseRecyclerAdapter<HolderClass_Hostel_Info, RecyclerAdapterClass_HostelInfo.ViewHolder_HostelInfo> {
    Context context;
    String Latitude;
    String Longitude;
    String addressss;

    public double distanceCalcc;
    ImageView HostelImagePrevieww;
    /*String WardenUid;*/

    private LocationManager locationManager;
    FusedLocationProviderClient client;


    public RecyclerAdapterClass_HostelInfo(@NonNull FirebaseRecyclerOptions<HolderClass_Hostel_Info> options, Context context) {
        super(options);
        this.context = context;


    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder_HostelInfo holder, int position, @NonNull HolderClass_Hostel_Info model) {
        holder.HostelName.setText(model.getHostelname());
        holder.HostelCity.setText(model.getHostelcity().toLowerCase());
        holder.HostelDescription.setText(model.getHosteldescription());
        Glide.with(holder.HostelImagePreview.getContext()).load(model.getHostelimage()).into(holder.HostelImagePreview);
        holder.latitudee.setText(String.valueOf(model.getLatitude()));
        holder.longitudee.setText(String.valueOf(model.getLongitude()));

       /* Latitude = String.valueOf(model.getLatitude());
        Longitude = String.valueOf(model.getLongitude());*/
        String imageurl = model.getHostelimage();
        String wardenname = model.getWardenName();
        String wardenphone = model.getWardenphonenumber();
        String description = model.getHosteldescription();
        String cityname = model.getHostelcity().toUpperCase();
        String Hostelnamee = model.getHostelname();
        String HostelWuid = model.getWardenUid();
        double Hlat = model.getLatitude();
        double Hlong = model.getLongitude();
        String completeLocationaddress = model.getCompleteLocationAddress();


        if (completeLocationaddress != null){
            addressss = completeLocationaddress;
        }
        else {
            addressss = "Anonymous Address";
        }

        client = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Location location) {

                if (location != null) {

                    double currentlat = location.getLatitude();
                    double currentlong = location.getLongitude();

                    final double hoslat = model.getLatitude();
                    final double hoslong = model.getLongitude();

                    float[] results = new float[1];
                    Location.distanceBetween(currentlat, currentlong, hoslat, hoslong, results);
                    float distance = results[0];
                    float DistanceInkilometer = distance / 1000;

                    holder.distanceCalculated.setText(new DecimalFormat("##.##").format(DistanceInkilometer) + " Km");
                    distanceCalcc = DistanceInkilometer;
                }
            }
        });






        Glide.with(holder.HostelImagePreview.getContext()).load(model.getHostelimage()).into(holder.HostelImagePreview);

        holder.wholeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), DetailedHostel.class);

                intent.putExtra("hostelimage", imageurl);
                intent.putExtra("wardenname", wardenname);
                intent.putExtra("wardenphone", wardenphone);
                intent.putExtra("latitude", Hlat);
                intent.putExtra("longitude", Hlong);
                intent.putExtra("description", description);
                intent.putExtra("cityname", cityname);
                intent.putExtra("hosstelname", Hostelnamee);
                intent.putExtra("wardenUid", HostelWuid);
                intent.putExtra("completeLocationAdd", addressss);


                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

                context.getApplicationContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder_HostelInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hostelview_in_recyclerview, parent, false);
        return new ViewHolder_HostelInfo(view);
    }

    class ViewHolder_HostelInfo extends RecyclerView.ViewHolder {
        ImageView HostelImagePreview;
        TextView HostelName, HostelCity, HostelDescription, distanceCalculated, latitudee, longitudee, Hostelwardenuid;
        RatingBar ratingBar;
        RelativeLayout wholeCardView;


        public ViewHolder_HostelInfo(@NonNull View itemView) {
            super(itemView);
            HostelImagePreview = (ImageView) itemView.findViewById(R.id.hostelImage);
            HostelName = (TextView) itemView.findViewById(R.id.Hostelname);
            HostelCity = (TextView) itemView.findViewById(R.id.HostelcityName);
            HostelDescription = (TextView) itemView.findViewById(R.id.hostelDesc);
            distanceCalculated = (TextView) itemView.findViewById(R.id.distance);
            latitudee = (TextView) itemView.findViewById(R.id.latitudeeee);
            longitudee = (TextView) itemView.findViewById(R.id.longitudeeee);
            wholeCardView = (RelativeLayout) itemView.findViewById(R.id.wholeCard);
            Hostelwardenuid = itemView.findViewById(R.id.hostelwardenuid);
        }
    }

}
