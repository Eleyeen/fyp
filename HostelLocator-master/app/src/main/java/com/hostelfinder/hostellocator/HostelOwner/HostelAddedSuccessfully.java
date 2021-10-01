package com.hostelfinder.hostellocator.HostelOwner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.hostelfinder.hostellocator.Common.The_Dashboard;
import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.Nearby_Hostels_List;

public class HostelAddedSuccessfully extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_warden_hostel_added_successfully);
    }

    public void gotoHostellist(View view) {
        Intent i = new Intent(getApplicationContext(), The_Dashboard.class);
        startActivity(i);
        finish();
    }
}