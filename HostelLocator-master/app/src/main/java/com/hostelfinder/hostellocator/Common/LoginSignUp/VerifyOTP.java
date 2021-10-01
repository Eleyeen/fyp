package com.hostelfinder.hostellocator.Common.LoginSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hostelfinder.hostellocator.R;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {

    PinView pinfromuser;
    String codeBySystem;
    FirebaseAuth firebaseAuth;
    Button verifyButton;
    TextView personnumber;

    String PhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_verify_o_t_p);
        ///// recieiving intent
        PhoneNumber = getIntent().getStringExtra("Mobile").toString();
        pinfromuser = findViewById(R.id.pinview);
        personnumber = (TextView) findViewById(R.id.personnumber);
        firebaseAuth = FirebaseAuth.getInstance();

        personnumber.setText("Enter OTP we have sent to "+ "\n" +PhoneNumber);
        InitiateOTP();

    }

    private void InitiateOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                PhoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                       codeBySystem = s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void CrossEverything(View view) {
        Intent harsaKhatm = new Intent(getApplicationContext(), SignUp.class);
        startActivity(harsaKhatm);
    }

    public void CallNewPasswordScreen(View view)
    {
    String code = pinfromuser.getText().toString();

       if (code.isEmpty()){
           Toast.makeText(this, "Empty field cannot be processed", Toast.LENGTH_SHORT).show();

        }else {
           PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, pinfromuser.getText().toString());
           signInWithPhoneAuthCredential(credential);
       }
    }

   /* private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }*/

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), SetNewPassword.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(VerifyOTP.this, "Verification completed", Toast.LENGTH_SHORT).show();
                        } else {
                                Toast.makeText(VerifyOTP.this, "Verification Not completed! Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}