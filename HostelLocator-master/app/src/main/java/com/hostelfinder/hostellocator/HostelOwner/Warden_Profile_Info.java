package com.hostelfinder.hostellocator.HostelOwner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hostelfinder.hostellocator.Common.AccountDeleted;
import com.hostelfinder.hostellocator.Common.LoginSignUp.User_Startup_Screen;
import com.hostelfinder.hostellocator.Common.The_Dashboard;
import com.hostelfinder.hostellocator.HelperClasses.CheckInternetConnection;
import com.hostelfinder.hostellocator.HelperClasses.User_Model;
import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.UserMapsActivity_Second;

import java.util.HashMap;

public class Warden_Profile_Info extends AppCompatActivity {
    TextInputLayout
            username,
            fullname;
    ProgressBar progressBar;
    FirebaseUser fuser;
    private FirebaseAuth firebaseAuth;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_warden_profile_info);
        ///// hookss
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        progressBar = (ProgressBar) findViewById(R.id.updaterecordProgressbar);
        username = (TextInputLayout) findViewById(R.id.Update_username);
        fullname = (TextInputLayout) findViewById(R.id.Update_fullname);
        /////// FireBase instance  //////////
        firebaseAuth = FirebaseAuth.getInstance();

        if (fuser != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AllUsers");
            ref.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot != null) {
                        User_Model user_model = snapshot.getValue(User_Model.class);
                        if (user_model != null) {
                            username.getEditText().setText(user_model.getUsername());
                            fullname.getEditText().setText(user_model.getFullname());
                            password = user_model.getPassword();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public void updateprofile(View view) {
        if (fuser != null) {

            CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
            if (checkInternetConnection.isConnected(this)) {
                UpdateRecords();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Warden_Profile_Info.this);
                builder.setTitle("Oops")
                        .setCancelable(false)
                        .setMessage("No Internet Connection, please connect to internet to proceed")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                            }
                        }).show();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Warden_Profile_Info.this);
            builder.setTitle("Not Logged In")
                    .setCancelable(false)
                    .setMessage("Please login to continue...")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), The_Dashboard.class));
                            finish();
                        }
                    })
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), User_Startup_Screen.class));
                        }
                    }).show();
        }


    }

    private void UpdateRecords() {

        if (
                !validateFullName()
                        | !validateUserName()
        ) {
            return;
        }

        showdilog();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AllUsers");
        reference.child(fuser.getUid()).child("username").setValue(username.getEditText().getText().toString());
        reference.child(fuser.getUid()).child("fullname").setValue(fullname.getEditText().getText().toString());
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Warden_Profile_Info.this, "Records updated", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), The_Dashboard.class);
                startActivity(i);
                finish();
                Toast.makeText(Warden_Profile_Info.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }

    private void showdilog() {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Updating...");
        dialog.setMessage("Please wait the until the process is completed");
        dialog.show();
    }

    public void DeletemyAccount(View view) {
        enterpasswordTodelete();
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

    private boolean enterpasswordTodelete() {

        AlertDialog.Builder AlertDiolog = new AlertDialog.Builder(Warden_Profile_Info.this);
        AlertDiolog.setTitle("Password");
        AlertDiolog.setCancelable(true);
        AlertDiolog.setMessage("Enter Your Password please");
        final EditText input = new EditText(Warden_Profile_Info.this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        AlertDiolog.setIcon(R.drawable.delete_medium_icon);
        AlertDiolog.setView(input);
        AlertDiolog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.toString().isEmpty()) {
                    Toast.makeText(Warden_Profile_Info.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                } else {
                    String userEnterendpass = input.getText().toString();

                    if (userEnterendpass.equals(password)) {
                        /////////////////// dialog interface

                        AlertDialog.Builder builder = new AlertDialog.Builder(Warden_Profile_Info.this);
                        builder.setTitle("Delete Account")
                                .setIcon(R.drawable.delete_icon)
                                .setCancelable(false)
                                .setMessage("Do you really want to delete your account? All the data with this account will be deleted Including your Hostel records if exists")
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // matching the password in database and userentered password first
                                        ProgressDialog dialogkhan = new ProgressDialog(Warden_Profile_Info.this);
                                        dialogkhan.setCancelable(false);
                                        dialogkhan.setTitle("Deleting");
                                        dialogkhan.setMessage("Please wait the until the process is completed");
                                        dialogkhan.show();

                                        fuser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                firebaseAuth.signOut();
                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AllUsers");
                                                RemoveHostelIfExists();
                                                reference.child(fuser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(Warden_Profile_Info.this, "Records Deleted", Toast.LENGTH_LONG).show();
                                                        Intent i = new Intent(getApplicationContext(), AccountDeleted.class);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Warden_Profile_Info.this, "Failed to Delete Records" + e.toString(), Toast.LENGTH_LONG).show();
                                                        dialogkhan.dismiss();
                                                    }
                                                });
                                                Toast.makeText(Warden_Profile_Info.this, "Account Deleted Successfully", Toast.LENGTH_LONG).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Warden_Profile_Info.this, "Account Deleted", Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(getApplicationContext(), AccountDeleted.class);
                                                startActivity(i);
                                                finish();
                                                dialogkhan.dismiss();
                                            }
                                        });

                                    }
                                }).show();
                    } else {
                        Toast.makeText(Warden_Profile_Info.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDiolog.show();
        return true;
    }

    private void RemoveHostelIfExists() {
        DatabaseReference hostelref = FirebaseDatabase.getInstance().getReference("Hostels");
        hostelref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(fuser.getUid())) {
                    hostelref.child(fuser.getUid()).removeValue().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Warden_Profile_Info.this, "Failed to Delete Hostel records" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Warden_Profile_Info.this, "Hostels Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Warden_Profile_Info.this, "No Hostel to be Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}