package com.hostelfinder.hostellocator.Common.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hostelfinder.hostellocator.R;

public class SuccessPasswordMessage extends AppCompatActivity {
    TextView email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_success_password_message);
        email = (TextView)findViewById(R.id.emailtext);
        String mail = getIntent().getStringExtra("email");
        email.setText("An email has been sent to \n"+mail +"\n"+ "Please go and Check");
    }

    public void gotoLoginPage(View view) {
        Intent gotologinpage = new Intent(getApplicationContext(), Login.class);
        startActivity(gotologinpage);
    }

    public void gotoHostellist(View view) {
    }
}