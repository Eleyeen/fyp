package com.hostelfinder.hostellocator.HostelOwner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hostelfinder.hostellocator.Common.LoginSignUp.User_Startup_Screen;
import com.hostelfinder.hostellocator.Common.VerificationMessage;
import com.hostelfinder.hostellocator.HelperClasses.CheckInternetConnection;
import com.hostelfinder.hostellocator.HelperClasses.HolderClass_Hostel_Info;
import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.UserDashboard;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class Show_Hostel_Detail extends AppCompatActivity {
    TextView hostelname, wardenname, hosteldescription, wardenphone, wardenemail, hostelcity;
    ImageButton delete, update;
    ImageView img;
    LinearLayout LayoutNoData, layoutData;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    FirebaseUser AppUser;
    private String UserID;
    String UserEnteredPassword;
    String passwordINdatabase;


    private String WN, WPN, WE, HN, HC, HD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_warden_show_hostel_detail);

        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        if (checkInternetConnection.isConnected(this)) {
            LayoutNoData = (LinearLayout) findViewById(R.id.layout_Nodata);
            layoutData = (LinearLayout) findViewById(R.id.layout_data);
            firebaseAuth = FirebaseAuth.getInstance();
            reference = FirebaseDatabase.getInstance().getReference("Hostels");
            AppUser = firebaseAuth.getCurrentUser();
            UserID = AppUser.getUid();


            img = (ImageView) findViewById(R.id.imageview);
            hostelname = (TextView) findViewById(R.id.showHostelName);
            hostelcity = (TextView) findViewById(R.id.showHostelcity);
            wardenname = (TextView) findViewById(R.id.showWardenName);
            hosteldescription = (TextView) findViewById(R.id.showHostelDescription);
            wardenphone = (TextView) findViewById(R.id.showWardenNumber);
            wardenemail = (TextView) findViewById(R.id.showWardenEmail);

            if (AppUser != null) {

                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setCancelable(false);
                dialog.setTitle("Loading...");
                dialog.setMessage("Please wait the until the process is completed");
                dialog.show();

                reference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HolderClass_Hostel_Info userprofile = snapshot.getValue(HolderClass_Hostel_Info.class);
                        if (userprofile != null) {
                            ///////// the first rows fetches the password will is going to be matches with the user entered password and then he be able to delete his hostel......
                            passwordINdatabase = userprofile.wardenPassword;
                            String wardennamee = userprofile.wardenName;
                            String hostelnamee = userprofile.hostelname;
                            String wardenemaill = userprofile.wardenEmail;
                            String hostelimgg = userprofile.hostelimage;
                            String wardenphonee = userprofile.wardenphonenumber;
                            String hosteldescc = userprofile.hosteldescription;
                            String hostelcityy = userprofile.hostelcity;

                            Glide.with(Show_Hostel_Detail.this).load(hostelimgg).into(img);
                            hostelname.setText(hostelnamee);
                            hostelcity.setText(hostelcityy);
                            wardenname.setText(wardennamee);
                            hosteldescription.setText(hosteldescc);
                            wardenphone.setText(wardenphonee);
                            wardenemail.setText(wardenemaill);

                            WN = wardennamee.toString();
                            WE = wardenemaill.toString();
                            WPN = wardenphonee.toString();
                            HN = hostelnamee.toString();
                            HC = hostelcityy.toString();
                            HD = hosteldescc.toString();

                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(Show_Hostel_Detail.this, "No Records Found", Toast.LENGTH_SHORT).show();
                            LayoutNoData.setVisibility(View.VISIBLE);
                            layoutData.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();
                        Toast.makeText(Show_Hostel_Detail.this, "No Records Found", Toast.LENGTH_SHORT).show();
                        LayoutNoData.setVisibility(View.VISIBLE);
                        layoutData.setVisibility(View.GONE);
                    }
                });
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Show_Hostel_Detail.this);
            builder.setTitle("No Internet Connection")
                    .setCancelable(false)
                    .setMessage("Please turn on your internet connection to see your hostel's detail")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                        }
                    }).show();
        }
    }
    public void UpdateHostelDetail(View view) {
        Intent gotoupdate = new Intent(getApplicationContext(), UpdateHostelInfo.class);

        gotoupdate.putExtra("WardenName", WN);
        gotoupdate.putExtra("WardenEmail", WE);
        gotoupdate.putExtra("WardenPhoneNumber", WPN);
        gotoupdate.putExtra("HostelCity", HC);
        gotoupdate.putExtra("HostelDescription", HD);
        gotoupdate.putExtra("HostelName", HN);

        startActivity(gotoupdate);

    }



    private void showHostelDetail() {
        /* String UserName = AppUser.getDisplayName();*/
        String email = AppUser.getEmail();
       /* String Phone = AppUser.getPhoneNumber();
        String Uid = AppUser.getUid();*/
        if (email.isEmpty()) {
            Toast.makeText(this, "No Records", Toast.LENGTH_SHORT).show();
        } else {
            wardenemail.setText(email);
        }
    }

    public void LogMeOut(View view) {

        if (AppUser != null) {
            firebaseAuth.signOut();
            Intent i = new Intent(getApplicationContext(), UserDashboard.class);
            startActivity(i);
            finish();
        }
    }

    public void progressdialog() {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Loading...");
        dialog.setMessage("Please wait the until the process is completed");
        dialog.show();
    }

    @SuppressLint("ResourceType")
    public void DeleteTheHostel(View view) {

        AlertDialog.Builder AlertDiolog = new AlertDialog.Builder(Show_Hostel_Detail.this);
        AlertDiolog.setTitle("Password");
        AlertDiolog.setCancelable(true);
        AlertDiolog.setMessage("Enter Your Password please");
        final EditText input = new EditText(Show_Hostel_Detail.this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        AlertDiolog.setIcon(R.drawable.delete_medium_icon);
        AlertDiolog.setView(input);
        AlertDiolog.setIconAttribute(android.R.color.holo_red_dark);
        AlertDiolog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserEnteredPassword = input.getText().toString();

                if (UserEnteredPassword.isEmpty()) {
                    Toast.makeText(Show_Hostel_Detail.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                } else {
                    if (UserEnteredPassword.equals(passwordINdatabase)) {
                        /////////////////// dialog interface
                        ProgressDialog progressDialog = new ProgressDialog(Show_Hostel_Detail.this);
                        progressDialog.setTitle("Deleting");
                        progressDialog.setMessage("Deleting Your Hostel Information Please wait...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        //////////// write deleting node code and on completion deletion process just copy the following code and paste it there

                        //////////////////////////////////

                        reference.child(AppUser.getUid()).removeValue().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(Show_Hostel_Detail.this, "Sorry Delete Failed" + " \n " + e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Show_Hostel_Detail.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent deleted = new Intent(getApplicationContext(), HostelDeleted.class);
                                startActivity(deleted);
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(Show_Hostel_Detail.this, "Password Did not matched", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).setNegativeButton("No", null);
        AlertDiolog.show();

    }

    public void addnewhostel(View view) {

        Intent gotoaddhostel = new Intent(getApplicationContext(), AddHostelForm.class);
        startActivity(gotoaddhostel);

       /* if (AppUser.isEmailVerified()) {
            Intent gotoaddhostel = new Intent(getApplicationContext(), AddHostelForm.class);
            startActivity(gotoaddhostel);

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(Show_Hostel_Detail.this);
            alert.setTitle("Not Verified!");
            alert.setMessage("Your email is not verified Please Verify your email");
            alert.setCancelable(true);
            alert.setIcon(R.drawable.warning_icon);
            alert.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
                    if (checkInternetConnection.isConnected(Show_Hostel_Detail.this)) {

                        ProgressDialog progress = new ProgressDialog(Show_Hostel_Detail.this);
                        progress.setTitle("Sending Verification Code");
                        progress.setCancelable(false);
                        progress.setMessage("Please wait... Verification email is sending");
                        progress.show();

                        AppUser.sendEmailVerification().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progress.dismiss();
                                Toast.makeText(Show_Hostel_Detail.this, "Failed to send the verfication email" + "\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progress.dismiss();
                                Toast.makeText(Show_Hostel_Detail.this, "Sent", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), VerificationMessage.class);
                                startActivity(i);
                                finish();
                            }
                        });

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Show_Hostel_Detail.this);
                        builder.setTitle("No Internet Connection")
                                .setCancelable(false)
                                .setMessage("Please turn on your internet connection to add a hostel")
                                .setNegativeButton("Cancel", null)
                                .setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                                    }
                                }).show();
                    }

                }
            }).
                    setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Show_Hostel_Detail.this, "You cannot add hostel without being verified", Toast.LENGTH_SHORT).show();
                            Intent dashbaord = new Intent(getApplicationContext(), UserDashboard.class);
                            startActivity(dashbaord);
                        }
                    });
            alert.show();
        }*/
    }

    public void hahaha() {
        AlertDialog.Builder diolog = new AlertDialog.Builder(this);
        diolog.setTitle("Delete?");
        diolog.setMessage("Do you really want to delete your hostel? No one will be able to see your hostel again.");
        diolog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ProgressDialog dialog1 = new ProgressDialog(Show_Hostel_Detail.this);
                dialog1.setTitle("Deleting");
                dialog1.setMessage("Deleting Your Hostel Information Please wait...");
                dialog1.setCancelable(false);
                dialog1.show();

                //////////// write deleting node code and on completion deletion process just copy the following code and paste it there
                Intent deleted = new Intent(getApplicationContext(), HostelDeleted.class);
                startActivity(deleted);
                finish();
            }
        }).setNegativeButton("No", null);
        diolog.show();
    }


}