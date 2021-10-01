package com.hostelfinder.hostellocator.Common.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.hostelfinder.hostellocator.R;

public class SetNewPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_set_new_password);
    }

    public void GoToSuccessMessagePage(View view) {
        Intent gotosuccesPage = new Intent(getApplicationContext(), SuccessPasswordMessage.class);
        startActivity(gotosuccesPage);
    }
}