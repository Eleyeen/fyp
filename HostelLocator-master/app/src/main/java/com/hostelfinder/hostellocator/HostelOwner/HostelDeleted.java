package com.hostelfinder.hostellocator.HostelOwner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.UserDashboard;

public class HostelDeleted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_warden_hostel_deleted);
    }

    public void GoHome(View view) {
        Intent gohome = new Intent(getApplicationContext(), UserDashboard.class);
        startActivity(gohome);
        finish();
    }
}