package com.hostelfinder.hostellocator.Common.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.hostelfinder.hostellocator.R;

public class User_Startup_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user__startup__screen);
    }

    public void callLoginScreen(View view) {
        Intent callLoginScreen = new Intent(getApplicationContext(), Login.class);


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.login_btn), "transition_login");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(User_Startup_Screen.this, pairs);
            startActivity(callLoginScreen, options.toBundle());
        } else {
            startActivity(callLoginScreen);
        }
    }

    public void callSignupScreen(View view) {
        Intent callSignupScreen = new Intent(getApplicationContext(), Simple_SignUp_Screen.class);


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.signup_btn), "transition_Signup");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(User_Startup_Screen.this, pairs);
            startActivity(callSignupScreen, options.toBundle());
        } else {
            startActivity(callSignupScreen);
        }
    }

    public void Howwework(View view) {
        Toast.makeText(this, "We dont work, we just kidding and making others fool!", Toast.LENGTH_SHORT).show();
    }
}