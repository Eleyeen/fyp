package com.hostelfinder.hostellocator.Common.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.hostelfinder.hostellocator.R;


public class SignUp extends AppCompatActivity {

    ImageView backbtn;
    Button next, login;
    TextView titleText;
    TextInputLayout fullname, username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_sign_up);

        //Hooks
        backbtn = findViewById(R.id.signupbackbutton);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);

        ///hoooks for getting data

        fullname = findViewById(R.id.signup_fullname);
        username = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
    }

    public void SignupScreenbackbutton(View view) {
        SignUp.super.onBackPressed();
    }

    public void callNextSignUpScreen(View view) {
            if (!validateFullName() | !validateUserName() | !validateEmail() | !validatePassword()){
                return;
            }

        Intent intent = new Intent(getApplicationContext(), SignUp2ndClass.class);

        // add transitions
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(backbtn, "transitionbackbutton");
        pairs[1] = new Pair<View, String>(next, "transitionnextbutton");
        pairs[2] = new Pair<View, String>(login, "transitionLoginButton");
        pairs[3] = new Pair<View, String>(titleText, "transitiontitletext");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }



    private boolean validateFullName() {
        String val = fullname.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            fullname.setError("Field cannot be empty");
            return false;
        } else {
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName() {
        String val = username.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            username.setError("Field cannot be empty!");
            return false;
        } else if (val.length() > 20) {
            username.setError("Username is too large!");
            return false;
        } else if (!val.matches(checkspaces)) {
            username.setError("No white spaces are allowed!");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        String checkemail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            email.setError("Field cannot be empty!");
            return false;
        } else if (!val.matches(checkemail)) {
            email.setError("Invalid email!");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();

        /*  String checkpassword = "^" +
                "(?=.*[a-zA-Z])" +  //// any letter
                "(?=.*[@#$%^&+=])" +  //// atleast one special character
                "(?=\\S+$)" +        //// no white spaces
              ".{2,}" +            //// atleast 2 character
                "$";*/

        if (val.isEmpty()) {
            password.setError("Field cannot be empty!");
            return false;
        } /*else if (!val.matches(checkpassword)) {
            password.setError("Password Should contain 2 Characters");
            return false;
        } */else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}