package com.hostelfinder.hostellocator.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.provider.ContactsContract;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.DetailedHostel;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Adapter_Nearby_Hostels extends RecyclerView.Adapter<MyViewHolder> implements Filterable {
    /////////// this is an array list which will hold all the data and is recieved by the model class
    ArrayList<Data_Model_Class> Data;
    ArrayList<Data_Model_Class> backup;
    Context context;

    public Adapter_Nearby_Hostels(ArrayList<Data_Model_Class> data, Context context) {
        Data = data;
        backup = new ArrayList<>(Data);
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ////////// this onCreateViewHolder method create an empty view and then through onBind Method It fills the data in

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hostelview_in_recyclerview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ///// this is a data feeding method , the data is feeded to all the textviews
        holder.HostelName.setText(Data.get(position).getHostelName());
        holder.HostelDescription.setText(Data.get(position).getHostelDescription());
        holder.imageView.setImageResource(Data.get(position).getHostelImage());
        holder.Cityname.setText(Data.get(position).getCityName());
        /*holder.distanceCalculated.setText((int) Data.get(position).getDistance());*/
        /* holder.ratingBar.setRating(Data.);*/
        /*holder.distanceCalculated.setText((int) Data.get(position).getDistance());*/
        holder.wholeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /////// moving towords the detailed screen and also taking the data with Intent to detailed Hostel screen
                Intent intent = new Intent(context.getApplicationContext(), DetailedHostel.class);

                intent.putExtra("Hostel_Image", Data.get(position).HostelImage);
                intent.putExtra("Hostel_Name", Data.get(position).HostelName);
                intent.putExtra("Hostel_Description", Data.get(position).HostelDescription);
                intent.putExtra("DistanceCalculated", Data.get(position).Distance);
                intent.putExtra("CityName", Data.get(position).CityName);

                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        ////// the very important step is to return the size of the data which will show in the recyclerv view
        return Data.size();
    }

    @Override
    public Filter getFilter() {

        return filter;
    }

    Filter filter = new Filter() {
        @Override
        //////// this filtering techneque works through background thread
        protected FilterResults performFiltering(CharSequence keyword) {
            ArrayList<Data_Model_Class> FilteredData = new ArrayList<>();
            if (keyword.toString().isEmpty())
                FilteredData.addAll(backup);
            else {

                for (Data_Model_Class obj : backup) {
                    ///////// match the text search by the user with that backup
                    ////// for testing i am gonna match the keyword with the Hostel name later i will change it to the specific city
                    String CityName = obj.getCityName().toString().toLowerCase();
                    if (CityName.contains(keyword.toString().toLowerCase()))
                        FilteredData.add(obj);
                }
            }
            FilterResults results = new FilterResults();
            ///// set the final filtered list in the offermentioned FilteredData variable
            results.values = FilteredData;
            return results;
        }

        @Override
        ////// this method word through main Ui thread
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Data.clear();
            ///// change the following model class to holderclass hostel info
            Data.addAll((ArrayList<Data_Model_Class>) results.values);
            ///// the following method tells that the main ui thread had just completed the task
            notifyDataSetChanged();
        }
    };
}
