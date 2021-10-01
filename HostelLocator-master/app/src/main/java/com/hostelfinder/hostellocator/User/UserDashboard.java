package com.hostelfinder.hostellocator.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hostelfinder.hostellocator.Common.AboutDeveloper;
import com.hostelfinder.hostellocator.Common.All_Chats;
import com.hostelfinder.hostellocator.Common.LoginSignUp.Login;
import com.hostelfinder.hostellocator.Common.LoginSignUp.User_Startup_Screen;
import com.hostelfinder.hostellocator.Common.The_Dashboard;
import com.hostelfinder.hostellocator.Common.VerificationMessage;
import com.hostelfinder.hostellocator.HelperClasses.CheckInternetConnection;
import com.hostelfinder.hostellocator.HostelOwner.AddHostelForm;
import com.hostelfinder.hostellocator.HostelOwner.Show_Hostel_Detail;
import com.hostelfinder.hostellocator.HostelOwner.Warden_Profile_Info;
import com.hostelfinder.hostellocator.R;

public class UserDashboard extends AppCompatActivity {
    ImageView menuIcon, addhostel, myhostel;
    EditText selectCity;
    FirebaseAuth firebaseAuth;
    MaterialAutoCompleteTextView autoCompleteTextView;
    String CityName;
    FirebaseUser AppUser;


    /// for getting intent
    /*String WardenUsername;
    String Uid;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);
        addhostel = (ImageView) findViewById(R.id.dashbaord_addMissingplace_icon);
        myhostel = (ImageView) findViewById(R.id.myhostel);
        firebaseAuth = FirebaseAuth.getInstance();
        AppUser = firebaseAuth.getCurrentUser();

        if (AppUser != null) {
            addhostel.setVisibility(View.GONE);
            myhostel.setVisibility(View.VISIBLE);
        } else {
            addhostel.setVisibility(View.VISIBLE);
        }
        /*hideKeyboard(this);*/
        ////Hookss////
        String ListOfCities[] = {"Peshawar", "Abbottabad", "Adezai", "Ali Bandar", "Amir Chah", "Attock", "Ayubia", "Bahawalpur", "Baden", "Bagh", "Bahawalnagar", "Burewala", "Banda Daud Shah", "Bannu district|Bannu", "Batagram", "Bazdar", "Bela", "Bellpat", "Bhag", "Bhakkar", "Bhalwal", "Bhimber", "Birote",
                "Buner", "Burj", "Chiniot", "Chachro", "Chagai", "Chah Sandan", "Chailianwala", "Chakdara", "Chakku", "Chakwal", "Chaman", "Charsadda", "Chhatr", "Chichawatni", "Chitral", "Dadu", "Dera Ghazi Khan", "Dera Ismail Khan", "Dalbandin", "Dargai", "Darya Khan", "Daska", "Dera Bugti", "Dhana Sar",
                "Digri", "Dina City", "Dinga", "Diwana", "Dokri", "Drosh", "Duki", "Dushi", "Duzab", "Faisalabad", "Fateh Jang", "Ghotki", "Gwadar", "Gujranwala", "Gujrat", "Gadra", "Gajar", "Gandava", "Garhi Khairo", "Garruck", "Ghakhar Mandi",
                "Ghanian", "Ghauspur", "Ghazluna", "Girdan", "Gulistan", "Gwash", "Hyderabad", "Hala", "Haripur", "Hab Chauki", "Hafizabad", "Hameedabad", "Hangu", "Harnai", "Hasilpur", "Haveli Lakha", "Hinglaj", "Hoshab", "Islamabad", "Islamkot", "Ispikan", "Jacobabad", "Jamshoro", "Jhang", "Jhelum", "Jamesabad",
                "Jampur", "Janghar", "Jati(Mughalbhin)", "Jauharabad", "Jhal", "Jhal Jhao", "Jhatpat", "Jhudo", "Jiwani", "Jungshahi", "Karachi", "Kotri", "Kalam", "Kalandi", "Kalat", "Kamalia", "Kamararod", "Kamber", "Kamokey", "Kanak", "Kandi", "Kandiaro", "Kanpur", "Kapip", "Kappar", "Karak City", "Karodi", "Kashmor",
                "Kasur", "Katuri", "Keti Bandar", "Khairpur", "Khanaspur", "Khanewal", "Kharan", "kharian", "Khokhropur", "Khora", "Khushab", "Khuzdar", "Kikki", "Klupro", "Kohan", "Kohat", "Kohistan", "Kohlu", "Korak", "Korangi", "Kot Sarae", "Kotli", "Lahore", "Larkana", "Lahri", "Lakki Marwat", "Lasbela", "Latamber",
                "Layyah", "Leiah", "Liari", "Lodhran", "Loralai", "Lower Dir", "Shadan Lund", "Multan", "Mandi Bahauddin", "Mansehra", "Mian Chanu", "Mirpur", "Mardan", "Mach", "Madyan", "Malakand", "Mand", "Manguchar", "Mashki Chah", "Maslti", "Mastuj", "Mastung", "Mathi", "Matiari", "Mehar", "Mekhtar", "Merui", "Mianwali",
                "Mianez", "Mirpur Batoro", "Mirpur Khas", "Mirpur Sakro", "Mithi", "Mongora", "Murgha Kibzai", "Muridke", "Musa Khel Bazar", "Muzaffar Garh", "Muzaffarabad", "Nawabshah", "Nazimabad", "Nowshera", "Nagar Parkar", "Nagha Kalat", "Nal", "Naokot", "Nasirabad", "Nauroz Kalat", "Naushara", "Nur Gamma", "Nushki", "Nuttal",
                "Okara", "Ormara", "Panjgur", "Pasni City", "Paharpur", "Palantuk", "Pendoo", "Piharak", "Pirmahal", "Pishin", "Plandri", "Pokran", "Pounch", "Quetta", "Qambar", "Qamruddin Karez", "Qazi Ahmad", "Qila Abdullah", "Qila Ladgasht", "Qila Safed", "Qila Saifullah", "Rawalpindi", "Rabwah", "Rahim Yar Khan", "Rajan Pur",
                "Rakhni", "Ranipur", "Ratodero", "Rawalakot", "Renala Khurd", "Robat Thana", "Rodkhan", "Rohri", "Sialkot", "Sadiqabad", "Safdar Abad- (Dhaban Singh)", "Sahiwal", "Saidu Sharif", "Saindak", "Sakrand", "Sanjawi", "Sargodha", "Saruna", "Shabaz Kalat", "Shadadkhot", "Shahbandar", "Shahpur", "Shahpur Chakar", "Shakargarh",
                "Shangla", "Sharam Jogizai", "Sheikhupura", "Shikarpur", "Shingar", "Shorap", "Sibi", "Sohawa", "Sonmiani", "Sooianwala", "Spezand", "Spintangi", "Sui", "Sujawal", "Sukkur", "Suntsar", "Surab", "Swabi", "Swat", "Tando Adam", "Tando Bago", "Tangi", "Tank City", "Tar Ahamd Rind", "Thalo", "Thatta", "Toba Tek Singh", "Tordher",
                "Tujal", "Tump", "Turbat", "Umarao", "Umarkot", "Upper Dir", "Uthal", "Vehari", "Veirwaro", "Vitakri", "Wadh", "Wah Cantt", "Warah", "Washap", "Wasjuk", "Wazirabad", "Yakmach", "Zhob", "Other"};

        /* autoCompleteTextView = findViewById(R.id.autocompttext);*/
        Spinner spinner = (Spinner) findViewById(R.id.citiesspinner);
        menuIcon = (ImageView) findViewById(R.id.drawer_icon);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ListOfCities);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CityName = spinner.getSelectedItem().toString().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        /*if (mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            gettingIntentFromSignInScreen();

        }*/


        ///function calls  ////


    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void callStartUpScreen(View view) {
        if (AppUser != null) {
            Intent ShowHostelDetial = new Intent(getApplicationContext(), Show_Hostel_Detail.class);
            startActivity(ShowHostelDetial);
        } else {
            Intent callStartUpScreen = new Intent(getApplicationContext(), User_Startup_Screen.class);
            startActivity(callStartUpScreen);
        }
    }


    public void showMeMap(View view) {

        if (CityName.isEmpty()) {
            Toast.makeText(this, "Please Select a city", Toast.LENGTH_SHORT).show();
        }
        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        if (checkInternetConnection.isConnected(this)) {
            Intent PermissionScreen = new Intent(getApplicationContext(), Nearby_Hostels_List.class);
            PermissionScreen.putExtra("Cityselected", CityName);
            startActivity(PermissionScreen);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserDashboard.this);
            builder.setTitle("No Internet Connection")
                    .setCancelable(false)
                    .setMessage("Please turn on your internet connection to see the list of hostels")
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

    public void goback(View view) {
        Intent goback = new Intent(getApplicationContext(), The_Dashboard.class);
        startActivity(goback);
        finish();
    }
}