package com.hostelfinder.hostellocator.Common.LoginSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hostelfinder.hostellocator.Common.The_Dashboard;
import com.hostelfinder.hostellocator.HelperClasses.CheckInternetConnection;
import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.UserDashboard;

public class Login extends AppCompatActivity {
    //////// declaring variables globally //////////////////
    TextInputLayout email;
    TextInputLayout password;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///////////// full screen Mode /////////////////////
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_login);

        ////////////  Hooks //////////////////////

        email = (TextInputLayout) findViewById(R.id.login_email);
        password = (TextInputLayout) findViewById(R.id.login_userPassword);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_login);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    //////////////// Login Screeen back button Onclick listener
    public void LoginScreenbackbutton(View view) {
        Login.super.onBackPressed();
    }


    /////////////// Call sign Up screeen (Onclick listner )

    public void CallSignUpScreen(View view) {
        startActivity(new Intent(getApplication(), Simple_SignUp_Screen.class));
    }
    ////////// call forgot password screeen  (Onclick listner )
    /////////(Onclick listner )

    public void callForgotPasswordScreen(View view) {
        startActivity(new Intent(getApplication(), ForgetPassword.class));

    }
    ///////////// Login Successfull after Entering password and username

    public void ButtonLoginSuccessFull(View view) {
        //////// checking internet connection
        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        if (checkInternetConnection.isConnected(this)) {

            //////// checking if validation methods are valid then
            if (!validateEmail() | !validatePassword()) {
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            String UserEmail = email.getEditText().getText().toString().trim();
            String Password = password.getEditText().getText().toString().trim();

            firebaseAuth.signInWithEmailAndPassword(email.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim())
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                /////// make the fields invisible
                                progressBar.setVisibility(View.GONE);
                                email.getEditText().setText("");
                                password.getEditText().setText("");

                                Intent gotoDashbaord = new Intent(getApplicationContext(), The_Dashboard.class);
                                finish();
                                gotoDashbaord.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                                gotoDashbaord.putExtra("Uid", firebaseAuth.getCurrentUser().getUid());
                                startActivity(gotoDashbaord);
                                /// you can also get more detail of the user like his password and name etc
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Failed to login \n"+task.getException().toString(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });

        } else {
            showCustomDialog();
        }


    }


    //////////// check internet connection function
    private boolean isConnected(Login login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo WifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo MobileConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((WifiConnection != null && WifiConnection.isConnected()) || (MobileConnection != null && MobileConnection.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Please connect to internet!")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), User_Startup_Screen.class));
                        finish();
                    }
                });
        builder.show();

    }

    ///////////////////  Validation Methods //////////////////////////////////
    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            password.setError("Please Enter Password!");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String UserEmail = email.getEditText().getText().toString().trim();
        String checkemail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (UserEmail.isEmpty()) {
            email.setError("Please Enter an email!");
            return false;
        } else if (!UserEmail.matches(checkemail)) {
            email.setError("Invalid email!");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }
}