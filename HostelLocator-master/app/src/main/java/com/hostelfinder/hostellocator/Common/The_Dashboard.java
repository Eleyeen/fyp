package com.hostelfinder.hostellocator.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hostelfinder.hostellocator.Common.LoginSignUp.Login;
import com.hostelfinder.hostellocator.Common.LoginSignUp.User_Startup_Screen;
import com.hostelfinder.hostellocator.HelperClasses.CheckInternetConnection;
import com.hostelfinder.hostellocator.HelperClasses.HolderClass_Hostel_Info;
import com.hostelfinder.hostellocator.HelperClasses.User_Model;
import com.hostelfinder.hostellocator.HostelOwner.AddHostelForm;
import com.hostelfinder.hostellocator.HostelOwner.Show_Hostel_Detail;
import com.hostelfinder.hostellocator.HostelOwner.Warden_Profile_Info;
import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.DetailedHostel;
import com.hostelfinder.hostellocator.User.UserDashboard;

import java.util.HashMap;

public class The_Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    ImageView menuIcon;
    FirebaseAuth firebaseAuth;
    FirebaseUser AppUser;
    String currentUseremail, currentusername;
    TextView useremail, welcome, nametext;
    String uid;
    CardView logout, login;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_the_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        AppUser = FirebaseAuth.getInstance().getCurrentUser();

        login = findViewById(R.id.cardviewfour);
        logout = findViewById(R.id.cardlogout);

        nametext = findViewById(R.id.nametext);
        reference = FirebaseDatabase.getInstance().getReference();
        bottomNavigationView = findViewById(R.id.bottomNavigationVIew);
        welcome = (TextView) findViewById(R.id.welcometextid);
        useremail = (TextView) findViewById(R.id.currentuseremail);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuIcon = (ImageView) findViewById(R.id.menuicon);
        navigationDrawer();

        if (AppUser != null) {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            login.setVisibility(View.INVISIBLE);
            logout.setVisibility(View.VISIBLE);
            useremail.setText(AppUser.getEmail());
            reference.child("AllUsers").child(AppUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User_Model user_model = snapshot.getValue(User_Model.class);
                    if (user_model != null) {
                        String userloggedin = user_model.getFullname();
                        nametext.setText(userloggedin);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.INVISIBLE);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }
    }

    @Override
    public boolean isFinishing() {
        return super.isFinishing();
    }

    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_chats:
                if (AppUser != null) {
                    Intent gotochatsss = new Intent(getApplicationContext(), All_Chats.class);
                    startActivity(gotochatsss);
                } else {
                    Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_aboutDeveloper:
                Intent showaboutdev = new Intent(getApplicationContext(), AboutDeveloper.class);
                startActivity(showaboutdev);
                break;
            case R.id.nav_home:
                Intent home = new Intent(getApplicationContext(), UserDashboard.class);
                startActivity(home);
                break;
            case R.id.nav_login:
                Intent login = new Intent(getApplicationContext(), Login.class);
                startActivity(login);

                break;
            case R.id.nav_my_Hostel:
                if (AppUser != null) {
                    Intent showmemyhostel = new Intent(getApplicationContext(), Show_Hostel_Detail.class);
                    startActivity(showmemyhostel);
                } else {
                    Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_add_missing_place:
                if (AppUser != null) {
                    if (AppUser.isEmailVerified()) {
                        if (AppUser != null) {
                            Intent addmissingplace = new Intent(getApplicationContext(), AddHostelForm.class);
                            startActivity(addmissingplace);
                            finish();
                        } else {
                            Intent takemetostartupscreen = new Intent(getApplicationContext(), User_Startup_Screen.class);
                            startActivity(takemetostartupscreen);
                        }
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(The_Dashboard.this);
                        alert.setTitle("Not Verified!");
                        alert.setMessage("Your email is not verified Please Verify your email");
                        alert.setCancelable(true);
                        alert.setIcon(R.drawable.warning_icon);
                        alert.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
                                if (checkInternetConnection.isConnected(The_Dashboard.this)) {

                                    ProgressDialog progress = new ProgressDialog(The_Dashboard.this);
                                    progress.setTitle("Sending Verification Code");
                                    progress.setCancelable(false);
                                    progress.setMessage("Please wait... Verification email is sending");
                                    progress.show();

                                    AppUser.sendEmailVerification().addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progress.dismiss();
                                            Toast.makeText(The_Dashboard.this, "Failed to send the verfication email" + "\n" + e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progress.dismiss();
                                                Toast.makeText(The_Dashboard.this, "Sent", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(getApplicationContext(), VerificationMessage.class);
                                                startActivity(i);
                                                finish();
                                            } else {
                                                progress.dismiss();
                                                Toast.makeText(The_Dashboard.this, "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(The_Dashboard.this);
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
                                        Toast.makeText(The_Dashboard.this, "You cannot add hostel without being verified", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                        alert.show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(The_Dashboard.this);
                    builder.setTitle("Login Requird")
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
                break;

            case R.id.nav_profile:
                Intent gotoWardenProfileInfo = new Intent(getApplicationContext(), Warden_Profile_Info.class);
                startActivity(gotoWardenProfileInfo);

                /*gotoWardenProfileInfo.putExtra("username", WardenUsername);
                gotoWardenProfileInfo.putExtra("uid", Uid);*/

                ///////// send the data of the warden from this dashboard screen to warden profile info
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builderLogout = new AlertDialog.Builder(The_Dashboard.this);
                builderLogout
                        .setCancelable(true)
                        .setIcon(R.drawable.warning_icon)
                        .setMessage("Do you want to logout?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (AppUser != null) {
                                    finish();
                                    firebaseAuth.signOut();
                                    Intent i = new Intent(getApplicationContext(), The_Dashboard.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }).show();

                /*if (AppUser != null) {
                    firebaseAuth.signOut();
                    Toast.makeText(this, "Logout Complete", Toast.LENGTH_SHORT).show();
                    Intent dashboard = new Intent(getApplicationContext(), The_Dashboard.class);
                    startActivity(dashboard);

                } else {
                    Toast.makeText(this, "You are not Logged In", Toast.LENGTH_SHORT).show();
                }*/
                break;
            /////////// bottom navigation item selected listenersss
            case R.id.navigationaddHostel:
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent gotoaddhostelpage = new Intent(getApplicationContext(), AddHostelForm.class);
                    startActivity(gotoaddhostelpage);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(The_Dashboard.this);
                    builder.setTitle("Login Requird")
                            .setCancelable(false)
                            .setIcon(R.drawable.warning_icon)
                            .setMessage("Please login to continue...")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent logmein = new Intent(getApplicationContext(), User_Startup_Screen.class);
                                    startActivity(logmein);
                                }
                            }).show();
                }
                break;
        }

        return true;
    }

    public void TakeMeToUserDashbaord(View view) {

        Intent gotouserdash = new Intent(getApplicationContext(), UserDashboard.class);
        startActivity(gotouserdash);
    }

    public void TakeMeToHostelDetails(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent gotouserdash = new Intent(getApplicationContext(), Show_Hostel_Detail.class);
            startActivity(gotouserdash);
        } else {
            Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
        }
    }

    public void TakeMeToChats(View view) {
        if (AppUser != null) {

            Intent gotouserdash = new Intent(getApplicationContext(), All_Chats.class);
            startActivity(gotouserdash);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(The_Dashboard.this);
            builder.setTitle("Login Requird")
                    .setCancelable(false)
                    .setIcon(R.drawable.warning_icon)
                    .setMessage("Please login to continue...")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent logmein = new Intent(getApplicationContext(), User_Startup_Screen.class);
                            startActivity(logmein);
                            finish();
                        }
                    }).show();
        }
    }

    public void TakeMeToLogin(View view) {
        Intent gotouserdash = new Intent(getApplicationContext(), User_Startup_Screen.class);
        startActivity(gotouserdash);
    }

    public void LogMeOutMaindashbord(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(The_Dashboard.this);
        builder
                .setCancelable(true)
                .setIcon(R.drawable.warning_icon)
                .setMessage("Do you want to logout?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (AppUser != null) {
                            finish();
                            firebaseAuth.signOut();
                            Intent i = getIntent();
                            startActivity(i);
                        }
                    }
                }).show();


    }

    public void Addnewhostell(View view) {

        if (firebaseAuth.getCurrentUser() != null) {
            Intent gotoaddhostelpage = new Intent(getApplicationContext(), AddHostelForm.class);
            startActivity(gotoaddhostelpage);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(The_Dashboard.this);
            builder.setTitle("Login Requird")
                    .setCancelable(false)
                    .setTitle("Oops, No Internet")
                    .setIcon(R.drawable.warning_icon)
                    .setMessage("Please login to continue...")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent logmein = new Intent(getApplicationContext(), User_Startup_Screen.class);
                            startActivity(logmein);
                        }
                    }).show();
        }
    }


}