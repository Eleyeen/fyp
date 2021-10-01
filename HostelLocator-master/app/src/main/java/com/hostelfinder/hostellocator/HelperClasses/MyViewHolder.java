package com.hostelfinder.hostellocator.HelperClasses;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hostelfinder.hostellocator.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ////////////////// this class will just hold the references of the views defined in the ""hostel view in recycler view"" (Xml file)

    ImageView imageView;
    TextView HostelName, HostelDescription, distanceCalculated, Cityname;
    RatingBar ratingBar;
    RelativeLayout wholeCardView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.hostelImage);
        HostelName = (TextView) itemView.findViewById(R.id.Hostelname);
        HostelDescription = (TextView)itemView.findViewById(R.id.hostelDesc);
        distanceCalculated =(TextView) itemView.findViewById(R.id.distance);
        wholeCardView = (RelativeLayout) itemView.findViewById(R.id.wholeCard);
        Cityname = (TextView) itemView.findViewById(R.id.HostelcityName);
       /* ratingBar = (RatingBar) itemView.findViewById(R.id.ratingbar);*/
    }
}
