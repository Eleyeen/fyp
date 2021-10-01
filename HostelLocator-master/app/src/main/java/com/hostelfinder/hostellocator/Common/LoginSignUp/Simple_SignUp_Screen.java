package com.hostelfinder.hostellocator.Common.LoginSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hostelfinder.hostellocator.Common.The_Dashboard;
import com.hostelfinder.hostellocator.HelperClasses.User_Model;
import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.UserDashboard;

import java.util.HashMap;

public class Simple_SignUp_Screen extends AppCompatActivity {
    TextInputLayout
            username,
            fullname,
            email,
            password,
            confirmPassword;
    ProgressBar progressBar;
    FirebaseUser currentloggedinUser;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_simple__sign_up__screen);

        ///////Hooksss ///////////////
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_signup);
        username = (TextInputLayout) findViewById(R.id.signup_username);
        fullname = (TextInputLayout) findViewById(R.id.signup_fullname);
        email = (TextInputLayout) findViewById(R.id.signup_email);
        password = (TextInputLayout) findViewById(R.id.signup_password);
        confirmPassword = (TextInputLayout) findViewById(R.id.signup_confirmpassword);
        reference = FirebaseDatabase.getInstance().getReference();
        /////// FireBase instance  //////////
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void SignUpMe(View view) {
        if (!validateFullName() | !validateUserName() | !validateEmail() | !validatePassword() | !validateConfirmPassword()) {
            return;
        }
        String UserPassword = password.getEditText().getText().toString();
        String ConfirmPassword = confirmPassword.getEditText().getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);

        if (UserPassword.equals(ConfirmPassword)) {

            firebaseAuth.createUserWithEmailAndPassword(email.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim())
                    .addOnCompleteListener(Simple_SignUp_Screen.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                //////// make all the fields empty
                                FirebaseUser fuser = firebaseAuth.getCurrentUser();
                                assert fuser != null;
                                String userId = fuser.getUid();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AllUsers").child(userId);

                                User_Model userrr = new User_Model(
                                        userId,
                                        username.getEditText().getText().toString(),
                                        fullname.getEditText().getText().toString(),
                                        password.getEditText().getText().toString().trim(),
                                        email.getEditText().getText().toString());


                                ref.setValue(userrr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Simple_SignUp_Screen.this, "Records saved", Toast.LENGTH_SHORT).show();
                                            Intent gotologin = new Intent(getApplicationContext(), Login.class);
                                            gotologin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(gotologin);
                                            finish();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Simple_SignUp_Screen.this, "SignUp Completed, But records not saved", Toast.LENGTH_SHORT).show();
                                        Intent gotologinn = new Intent(getApplicationContext(), Login.class);
                                        startActivity(gotologinn);
                                        finish();
                                    }
                                });

                                Toast.makeText(Simple_SignUp_Screen.this, "SignUp Completed", Toast.LENGTH_SHORT).show();
                                username.getEditText().setText("");
                                fullname.getEditText().setText("");
                                email.getEditText().setText("");
                                password.getEditText().setText("");
                                confirmPassword.getEditText().setText("");
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Simple_SignUp_Screen.this, "Sorry! SignUp Failed!", Toast.LENGTH_LONG).show();
                            }

                        }
                    });


        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Password Dose not Match", Toast.LENGTH_SHORT).show();

        }


    }

    /////////// storing user records in the firebase database;

    public void StoreUserRecords(String id, String uname, String pass, String uemail, String fname) {

        User_Model user = new User_Model(id, uname, fname, pass, uemail);

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("AllUsers").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Simple_SignUp_Screen.this, "Records Saved", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Simple_SignUp_Screen.this, "Sorry Records not saved", Toast.LENGTH_SHORT).show();
            }
        });


    }

    ////////// letting the user signin Automatically after a signup

    private void SignInUserAndSaveRecords(String emailll, String Passworddd) {
        firebaseAuth.signInWithEmailAndPassword(emailll, Passworddd)
                .addOnCompleteListener(Simple_SignUp_Screen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            StoreUserRecords(firebaseAuth.getCurrentUser().getUid(),
                                    username.getEditText().getText().toString(),
                                    password.getEditText().getText().toString(),
                                    email.getEditText().getText().toString(),
                                    fullname.getEditText().getText().toString());

                            Intent gotoDashbaord = new Intent(getApplicationContext(), The_Dashboard.class);
                            finish();
                            startActivity(gotoDashbaord);

                            /// you can also get more detail of the user like his password and name etc
                        } else {
                            Toast.makeText(Simple_SignUp_Screen.this, "Failed to login \n" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    public void SignupScreenbackbutton(View view) {
        Simple_SignUp_Screen.super.onBackPressed();
    }


    private boolean validateConfirmPassword() {
        String ConfirmPassword = confirmPassword.getEditText().getText().toString().trim();

        /*  String checkpassword = "^" +
                "(?=.*[a-zA-Z])" +  //// any letter
                "(?=.*[@#$%^&+=])" +  //// atleast one special character
                "(?=\\S+$)" +        //// no white spaces
              ".{2,}" +            //// atleast 2 character
                "$";*/

        if (ConfirmPassword.isEmpty()) {
            password.setError("Please Re-type Your password!");
            return false;
        } else if (ConfirmPassword.length() < 6) {
            confirmPassword.setError("Password too Short");
            return false;
        } else {
            confirmPassword.setError(null);
            return true;
        }
    }

    private boolean validateUserName() {
        String UserName = username.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (UserName.isEmpty()) {
            username.setError("Field cannot be empty!");
            return false;
        } else if (UserName.length() > 20) {
            username.setError("Username is too large!");
            return false;
        } else if (!UserName.matches(checkspaces)) {
            username.setError("No white spaces are allowed!");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private boolean validateFullName() {
        String FullName = fullname.getEditText().getText().toString().trim();
        if (FullName.isEmpty()) {
            fullname.setError("Field cannot be empty");
            return false;
        } else {
            fullname.setError(null);
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

    private boolean validatePassword() {
        String UserPassword = password.getEditText().getText().toString().trim();

        /*  String checkpassword = "^" +
                "(?=.*[a-zA-Z])" +  //// any letter
                "(?=.*[@#$%^&+=])" +  //// atleast one special character
                "(?=\\S+$)" +        //// no white spaces
              ".{2,}" +            //// atleast 2 character
                "$";*/

        if (UserPassword.isEmpty()) {
            password.setError("Please Choose a Password!");
            return false;
        } else if (UserPassword.length() < 6) {
            password.setError("Password Is too Short");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void gotologinagain(View view) {
        Intent go = new Intent(getApplicationContext(), Login.class);
        startActivity(go);
        finish();
    }

}