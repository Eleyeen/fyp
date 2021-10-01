package com.hostelfinder.hostellocator.Common.LoginSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;
import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.UserDashboard;

public class ForgetPassword extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    TextInputLayout MobileNumber, enteredEmail;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_forget_password);

        MobileNumber = (TextInputLayout) findViewById(R.id.forgotpassword_phone_number);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.mycountrycodepicker);
        countryCodePicker.registerCarrierNumberEditText(MobileNumber.getEditText());
        enteredEmail = (TextInputLayout) findViewById(R.id.forgotpassword_email);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void CalllOtpScreen(View view) {

        if (enteredEmail.equals("")) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
        } else {
             ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Verifying");
            dialog.setMessage("Please wait, we will send you an email shortly");
            dialog.setCancelable(false);
            dialog.show();

            String email = enteredEmail.getEditText().getText().toString().trim();
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        dialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), SuccessPasswordMessage.class);
                        i.putExtra("email", email);
                        startActivity(i);
                        finish();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(ForgetPassword.this, "Sorry! Email cannot be Send", Toast.LENGTH_SHORT).show();
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(ForgetPassword.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        /*Intent VerifyOTP = new Intent(getApplicationContext(), VerifyOTP.class);

        VerifyOTP.putExtra("Mobile", countryCodePicker.getFullNumberWithPlus().trim());
        startActivity(VerifyOTP);*/
    }
}