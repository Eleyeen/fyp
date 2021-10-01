package com.hostelfinder.hostellocator.HelperClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hostelfinder.hostellocator.R;

import java.security.acl.LastOwnerException;

public class Adapter_ForListOfHostels extends RecyclerView.Adapter <Adapter_ForListOfHostels.Holder>{
    String Data[];

    public Adapter_ForListOfHostels(String[] data) {
        Data = data;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hostelview_in_recyclerview, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.HostelName.setText(Data[position]);


    }

    @Override
    public int getItemCount() {
        return Data.length;
    }


    public class Holder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView HostelName, HostelDescription, distanceCalculated;
        RatingBar ratingBar;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.hostelImage);
            HostelName = (TextView) itemView.findViewById(R.id.Hostelname);
            HostelDescription = (TextView)itemView.findViewById(R.id.hostelDesc);
            distanceCalculated =(TextView) itemView.findViewById(R.id.distance);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingbar);
        }
    }
}
