package com.hostelfinder.hostellocator.HostelOwner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.hostelfinder.hostellocator.Common.The_Dashboard;
import com.hostelfinder.hostellocator.R;

import java.util.HashMap;
import java.util.Map;

public class UpdateHostelInfo extends AppCompatActivity {

    EditText wardenname, wardenphone, wardenemail, hostelname, hostelcity, hosteldesc;
    Button updaterecords;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_warden_update_hostel_info);


        wardenname = findViewById(R.id.editwardenname);
        wardenphone = findViewById(R.id.editphonenumber);
        wardenemail = findViewById(R.id.editemail);
        hostelname = findViewById(R.id.edithostelname);
        hostelcity = findViewById(R.id.edithostelcity);
        hosteldesc = findViewById(R.id.editdescription);

        wardenname.setText(getIntent().getStringExtra("WardenName"));
        wardenphone.setText(getIntent().getStringExtra("WardenPhoneNumber"));
        wardenemail.setText(getIntent().getStringExtra("WardenEmail"));
        hostelname.setText(getIntent().getStringExtra("HostelName"));
        hostelcity.setText(getIntent().getStringExtra("HostelCity"));
        hosteldesc.setText(getIntent().getStringExtra("HostelDescription"));

        countryCodePicker = (CountryCodePicker) findViewById(R.id.countryCodePick);
        countryCodePicker.registerCarrierNumberEditText(wardenphone);


    }

    public void updateHostelinfo(View view) {

        if (!validateHostelName()
                | !validateFullName()
                | !validateEmail()
                | !validateDescription()
                | !validatePhone()
                | !validateCity()) {
            return;
        }
        showdilog();
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fuser != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hostels").child(fuser.getUid());

            reference.child("wardenName").setValue(wardenname.getText().toString());
            reference.child("wardenphonenumber").setValue(countryCodePicker.getFullNumberWithPlus());
            reference.child("wardenEmail").setValue(wardenemail.getText().toString());
            reference.child("hostelcity").setValue(hostelcity.getText().toString());
            reference.child("hostelname").setValue(hostelname.getText().toString());
            reference.child("hosteldescription").setValue(hosteldesc.getText().toString());


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(UpdateHostelInfo.this, "Records updated", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), Show_Hostel_Detail.class);
                    startActivity(i);
                    finish();
                }
            }, 2000);

        }
    }

    private void showdilog() {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Updating...");
        dialog.setMessage("Please wait the until the process is completed");
        dialog.show();
    }

    private boolean validateHostelName() {
        String HostelName = hostelname.getText().toString();
        if (HostelName.isEmpty()) {
            hostelname.setError("Field cannot be empty!");
            return false;
        } else if (!HostelName.matches("[a-zA-Z ]+")) {
            hostelname.setError("Enter Alphabetical characters only ");
            return false;

        } else if (!HostelName.contains("Hostel")) {
            hostelname.setError("This field should must contain the keyword 'Hostel'");
            return false;

        } else if (HostelName.length() > 30) {
            hostelname.setError("Hostel Name cannot be too long");
            return false;
        } else {
            hostelname.setError(null);
            return true;
        }
    }

    private boolean validateCity() {
        String city = hostelcity.getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (city.isEmpty()) {
            hostelcity.setError("Field cannot be empty!");
            return false;
        } else {
            hostelcity.setError(null);
            return true;
        }
    }

    private boolean validateDescription() {
        String description = hosteldesc.getText().toString();
        if (description.isEmpty()) {
            hosteldesc.setError("Please write something about your hostel");
            return false;
        } else if (description.length() < 80) {
            hosteldesc.setError("Description must be 80 letters long");
            return false;
        } else {
            hosteldesc.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String phone = wardenphone.getText().toString().trim();
        String pattern = "[0-9]";
        if (phone.isEmpty()) {
            wardenphone.setError("Please Enter Phone No");
            return false;
        } else if (phone.length() != 11) {
            wardenphone.setError("Invalid Phone No");
            return false;
        } else {
            wardenphone.setError(null);
            return true;
        }
    }

    private boolean validateFullName() {
        String FullName = wardenname.getText().toString().trim();
        if (FullName.isEmpty()) {
            wardenname.setError("Field cannot be empty");
            return false;
        } else {
            wardenname.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String UserEmail = wardenemail.getText().toString().trim();
        String checkemail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (UserEmail.isEmpty()) {
            wardenemail.setError("Please Enter an email!");
            return false;
        } else if (!UserEmail.matches(checkemail)) {
            wardenemail.setError("Invalid email!");
            return false;
        } else {
            wardenemail.setError(null);
            return true;
        }
    }
}