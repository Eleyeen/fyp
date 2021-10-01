package com.hostelfinder.hostellocator.Common.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.hostelfinder.hostellocator.R;
public class SignUp3rdClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_sign_up3rd_class);
    }

    public void callOTPScreen(View view) {
        Intent i = new Intent(getApplicationContext(), VerifyOTP.class);
        startActivity(i);
    }
}