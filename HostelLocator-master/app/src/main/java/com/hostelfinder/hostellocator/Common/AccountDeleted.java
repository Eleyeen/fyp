package com.hostelfinder.hostellocator.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.hostelfinder.hostellocator.R;

public class AccountDeleted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account_deleted);
    }

    public void GoHome(View view) {
        Intent gohome = new Intent(getApplicationContext(), The_Dashboard.class);
        startActivity(gohome);
        finish();
    }
}