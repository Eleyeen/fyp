package com.hostelfinder.hostellocator.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.hostelfinder.hostellocator.HelperClasses.Adapter_ForListOfHostels;
import com.hostelfinder.hostellocator.R;

public class List_Of_Hostels extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_of_hostels);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = findViewById(R.id.hostelslistrecylcerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String Array[]= {"My Hostel", "The Farm Hostel", "Beds Friends Hostel", "Premium Hostel","Your Hostel", "Move Onwords", "Talented People Hostel", "The City Hostel", "Studious People Hostel", "Sleep Well", "Arman Hostel", "The Best", "Welcome", "Five Star Hostel", "New Hostel", "Jannat Hostel", "Arman Hostel"};
        recyclerView.setAdapter(new Adapter_ForListOfHostels(Array));
    }
}