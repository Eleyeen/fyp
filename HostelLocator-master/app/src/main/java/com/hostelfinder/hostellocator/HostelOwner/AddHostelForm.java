package com.hostelfinder.hostellocator.HostelOwner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.hostelfinder.hostellocator.HelperClasses.CheckInternetConnection;
import com.hostelfinder.hostellocator.HelperClasses.HolderClass_Hostel_Info;
import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.Nearby_Hostels_List;
import com.hostelfinder.hostellocator.User.UserDashboard;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class AddHostelForm extends AppCompatActivity {
    //////  defining views
    TextInputLayout Hostelname, WardenName, WardenEmail, Password, ConfirmPassword, Hostelcity, WardenPhoneno, Hostel_Description;
    TextView locationInfo;
    ImageView hostelPreviewImage;
    double LocationLatitude;
    double LocationLongitude;
    Bitmap bitmap;
    String imageName;
    Uri ImageUrl;
    ProgressBar progressBar;
    Spinner spinner;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    FirebaseUser PersonLoggedIn;
    String CurrentUserUid;
    String CurrentUserEmail;
    FirebaseAuth firebaseAuth;
    DatabaseReference mainproject;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    CountryCodePicker countryCodePicker;
    //request code to pick images
    private static final int REQUEST_CODE_PLACE = 1;
    private static final int REQUEST_CODE_IMAGEPICK = 4;
    //position of selected Images
    int position = 0;
    String Cityselectedd;
    String LocationFetched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_warden_add_hostel_form);

        firebaseAuth = FirebaseAuth.getInstance();
        PersonLoggedIn = firebaseAuth.getCurrentUser();
        CurrentUserUid = PersonLoggedIn.getUid();
        CurrentUserEmail = PersonLoggedIn.getEmail();
        mainproject = FirebaseDatabase.getInstance().getReference("Hostels");
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        //Hooks
        spinner = (Spinner) findViewById(R.id.addhostelspinner);
        Hostelname = (TextInputLayout) findViewById(R.id.hostelname);
        Hostelcity = (TextInputLayout) findViewById(R.id.hostelcity);
        WardenPhoneno = (TextInputLayout) findViewById(R.id.wardenphoneno);
        Hostel_Description = (TextInputLayout) findViewById(R.id.hostelDescription);
        hostelPreviewImage = (ImageView) findViewById(R.id.hostelpreviewImage);
        locationInfo = (TextView) findViewById(R.id.locationInfo);
        WardenEmail = (TextInputLayout) findViewById(R.id.warden_email);
        WardenName = (TextInputLayout) findViewById(R.id.addHostel_wardenfullname);
        Password = (TextInputLayout) findViewById(R.id.warden_password);
        ConfirmPassword = (TextInputLayout) findViewById(R.id.warden_confirmpassword);

        countryCodePicker = (CountryCodePicker) findViewById(R.id.countryCodePick);
        countryCodePicker.registerCarrierNumberEditText(WardenPhoneno.getEditText());


        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mainproject = FirebaseDatabase.getInstance().getReference("Hostels");

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


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ListOfCities);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cityselectedd = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //////////// adding hostel button
    public void AddHostelBtn(View view) {
        if (!validateHostelName()
                | !validateFullName()
                | !validateEmail()
                | !validatePassword()
                | !validateConfirmPassword()
                | !validateDescription()
                | !validateLatlng()
                | !validateImageview()
                | !validatePhone()) {
            return;
        }
        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        if (checkInternetConnection.isConnected(this)) {
            SignUpUser();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddHostelForm.this);
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
        ////////// calling of sign up function and that also contains the image url and the rest of the hostel informations
    }

    // click handle for backbutton
    public void backButton(View view) {
    }

    //click handle for pick place button
    public void PickYourPlace(View view) {
        //// method called
        CheckLocationPermission();
    }

    //click handle for pick images button
    public void BrowseImages(View view) {
        showGalleryIntent();
        /* RunTimeStoragePermission();*/
    }

    // User Defined Methods
    public void showGalleryIntent() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Hostel Preview Image"), REQUEST_CODE_IMAGEPICK);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        /* Toast.makeText(AddHostelForm.this, "Permission Denied!", Toast.LENGTH_SHORT).show();*/
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public void PickPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddHostelForm.this)
                    , REQUEST_CODE_PLACE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void CheckLocationPermission() {
        Dexter.withContext(AddHostelForm.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        PickPlace();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //////// onActivityResult Code for place Picker
        if (requestCode == REQUEST_CODE_PLACE && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            StringBuilder stringBuilder = new StringBuilder();

            double latitude = place.getLatLng().latitude;
            double longitude = place.getLatLng().longitude;


            /* CharSequence LocationAdress = String.valueOf(place.getAddress());*/

            LocationLatitude = latitude;
            LocationLongitude = longitude;
            /*String address = String.format(String.valueOf(place.getAddress()));*/


            Geocoder geocoder = new Geocoder(AddHostelForm.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);


                if (addresses != null) {
                    LocationFetched = addresses.get(0).getAddressLine(0);
                } else {
                    LocationFetched = "Anonymous address";
                    Toast.makeText(this, "No Specific Name found for the location selected", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }

            stringBuilder.append("Address:");
            stringBuilder.append("\n");
            stringBuilder.append(LocationFetched);

            locationInfo.setText(stringBuilder.toString());
            locationInfo.setVisibility(View.VISIBLE);
        }
        if (requestCode == REQUEST_CODE_IMAGEPICK && resultCode == RESULT_OK) {
            /*assert data != null;*/
            ImageUrl = data.getData();
            imageName = getFileName(ImageUrl);
            try {
                InputStream inputStream = getContentResolver().openInputStream(ImageUrl);
                bitmap = BitmapFactory.decodeStream(inputStream);
                hostelPreviewImage.setImageBitmap(bitmap);
                hostelPreviewImage.setVisibility(View.VISIBLE);
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
                relativeLayout.setVisibility(View.VISIBLE);
            } catch (Exception e) {
            }
        }
    }


    public void ADDHOSTEL() {

        //////// getting text
        String _HostelName = Hostelname.getEditText().getText().toString();
        String _Hostelcity = Hostelcity.getEditText().getText().toString().toUpperCase().trim();
        String _wardenphone = WardenPhoneno.getEditText().getText().toString().trim();
        String _HostelDescription = Hostel_Description.getEditText().getText().toString();
        String _wardenname = WardenName.getEditText().getText().toString();
        String _wardenpassword = Password.getEditText().getText().toString();
        String _wardenEmail = WardenEmail.getEditText().getText().toString();


        ////// dialog box....

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Adding Hostel");
        dialog.setCancelable(false);
        dialog.show();


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ImagesUploader = storage.getReference("HostelImages" + new Random().nextInt(500));  ///// clear the random function and set .chile(getFileName);

    }


    public boolean SignUpUser() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Creating Account");
        dialog.setMessage("Please wait, Dont Quit");
        dialog.setCancelable(false);
        dialog.show();


        ///////////////////////////// code for adding picture

        storageReference = storage.getReference("HostelImages" + new Random().nextInt(500));
        storageReference.putFile(ImageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        ////// get the reference of the first child where to put the data

                        /////// reference of data holder class
                        ///////// sending data to model class constructor
                        HolderClass_Hostel_Info holderClass_hostel_info = new HolderClass_Hostel_Info(
                                Hostelname.getEditText().getText().toString(),
                                Cityselectedd.toLowerCase(),
                                countryCodePicker.getFullNumberWithPlus(),
                                Hostel_Description.getEditText().getText().toString(),
                                LocationLongitude,
                                LocationLatitude,
                                uri.toString(),
                                WardenName.getEditText().getText().toString(),
                                WardenEmail.getEditText().getText().toString(),
                                Password.getEditText().getText().toString(),
                                CurrentUserUid,
                                LocationFetched);

                        mainproject.child(PersonLoggedIn.getUid()).setValue(holderClass_hostel_info)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AddHostelForm.this, "Your Hostel Information is Added", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(AddHostelForm.this, "Sorry, Information Not Saved" + "\n" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddHostelForm.this, "Records Not saved" + "\n" + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        ///////// now make all the fields blank
                        Hostelname.getEditText().setText("");
                        WardenName.getEditText().setText("");
                        WardenEmail.getEditText().setText("");
                        Password.getEditText().setText("");
                        Hostelcity.getEditText().setText("");
                        WardenPhoneno.getEditText().setText("");
                        Hostel_Description.getEditText().setText("");
                        hostelPreviewImage.setImageResource(R.drawable.gallery_icon);
                        hostelPreviewImage.setVisibility(View.GONE);
                        locationInfo.setText("");
                        ConfirmPassword.getEditText().setText("");
                        locationInfo.setVisibility(View.GONE);
                        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
                        relativeLayout.setVisibility(View.GONE);


                        Toast.makeText(AddHostelForm.this, "Process Finished", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Toast.makeText(AddHostelForm.this, "Process Finished \n" + "Hostel Added Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HostelAddedSuccessfully.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                dialog.setMessage("Adding Hostel " + (int) percent + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddHostelForm.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        return true;
    }

    /////////// validation of the fields Hostel name , Hostel city , hostel description
    private boolean validateHostelName() {
        String HostelName = Hostelname.getEditText().getText().toString();
        if (HostelName.isEmpty()) {
            Hostelname.setError("Field cannot be empty!");
            return false;
        } else if (!HostelName.matches("[a-zA-Z ]+")) {
            Hostelname.setError("Enter Alphabetical characters only ");
            return false;

        } else if (!HostelName.contains("Hostel")) {
            Hostelname.setError("This field should must contain the keyword 'Hostel'");
            return false;

        } else if (HostelName.length() > 30) {
            Hostelname.setError("Hostel Name cannot be too large!");
            return false;
        } else {
            Hostelname.setError(null);
            return true;
        }
    }

    private boolean validateCity() {
        String city = Hostelcity.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (city.isEmpty()) {
            Hostelcity.setError("Field cannot be empty!");
            return false;
        } else {
            Hostelcity.setError(null);
            return true;
        }
    }

    private boolean validateDescription() {
        String description = Hostel_Description.getEditText().getText().toString();
        if (description.isEmpty()) {
            Hostel_Description.setError("Please write something about your hostel");
            return false;
        } else if (description.length() < 80) {
            Hostel_Description.setError("Description must be 80 letters long");
            return false;
        } else {
            Hostel_Description.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String phone = WardenPhoneno.getEditText().getText().toString().trim();
        String pattern = "[0-9]";
        if (phone.isEmpty()) {
            WardenPhoneno.setError("Please Enter Phone No");
            return false;
        } else if (phone.length() != 11) {
            WardenPhoneno.setError("Invalid Phone No");
            return false;
        }
         /*else if (!phone.matches(pattern)) {
            WardenPhoneno.setError("Only numbers are allowed ");
            return false;
        }*/
        else {
            WardenPhoneno.setError(null);
            return true;
        }
    }

    private boolean validateLatlng() {
        String latlang = locationInfo.getText().toString();
        if (latlang.isEmpty()) {
            Toast.makeText(this, "Choose Location", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            locationInfo.setError(null);
            return true;
        }
    }

    private boolean validateImageview() {
        if (hostelPreviewImage.getDrawable() == null) {
            Toast.makeText(this, "No Image selected", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateFullName() {
        String FullName = WardenName.getEditText().getText().toString().trim();
        if (FullName.isEmpty()) {
            WardenName.setError("Field cannot be empty");
            return false;
        } else {
            WardenName.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String UserEmail = WardenEmail.getEditText().getText().toString().trim();
        String checkemail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (UserEmail.isEmpty()) {
            WardenEmail.setError("Please Enter an email!");
            return false;
        } else if (!UserEmail.matches(checkemail)) {
            WardenEmail.setError("Invalid email!");
            return false;
        } else {
            WardenEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String UserPassword = Password.getEditText().getText().toString().trim();

        String checkpassword = "^" +
                "(?=.*[a-zA-Z])" +  //// any letter
                /* "(?=.*[@#$%^&+=])" +  //// atleast one special character*/
                "(?=\\S+$)" +        //// no white spaces
                ".{2,}" +            //// atleast 2 character
                "$";

        if (UserPassword.isEmpty()) {
            Password.setError("Please Choose a Password!");
            return false;
        } else if (UserPassword.length() < 6) {
            Password.setError("Password Is too Short");
            return false;
        } else if (!UserPassword.matches(checkpassword)) {
            Password.setError("Password must be contain 2 characters and any letter");
            return false;
        } else {
            Password.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String cnfirmPassword = ConfirmPassword.getEditText().getText().toString().trim();
        String password = Password.getEditText().getText().toString().trim();
        /*  String checkpassword = "^" +
                "(?=.*[a-zA-Z])" +  //// any letter
                "(?=.*[@#$%^&+=])" +  //// atleast one special character
                "(?=\\S+$)" +        //// no white spaces
              ".{2,}" +            //// atleast 2 character
                "$";*/

        if (cnfirmPassword.isEmpty()) {
            ConfirmPassword.setError("Please Re-type Your password!");
            return false;
        } else if (cnfirmPassword.length() < 6) {
            ConfirmPassword.setError("Password too Short");
            return false;
        } else if (!cnfirmPassword.matches(password)) {
            ConfirmPassword.setError("Password doesn't match");
            return false;
        } else {
            ConfirmPassword.setError(null);
            return true;
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    //////////// unused Methodsss /////////////////////////////////////////

    private void RunTimeStoragePermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        /*pickImagesIntent();*/
                        /* UplaodImages();*/

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(AddHostelForm.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void pickImagesIntent() {

        //////// if the app is crashes plz remember to clear the following parameter .. ACTION_GET_CONTENT
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("images/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), REQUEST_CODE_IMAGEPICK);

//////////////////////   Intent for gallery images selection Second Method//////////////////////////
       /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("images/*");
        startActivityForResult(intent, PICK_IMAGES_CODE);*/
////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void UplaodImages() {
        // this is a dilog
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Image(s) Uploading");
        dialog.show();

        //////// the following lines help us to uplaod images for firebase storage
        /// we created a child named HostelImages and there im gonna set images stored in url
        storageReference = storage.getReference().child("HostelImages");
        storageReference.putFile(ImageUrl)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //// show a toast of successfully uploaded
                        Toast.makeText(AddHostelForm.this, "Images Uploaded!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        dialog.setMessage("Adding Your Hostel" + (int) percent + "%");
                        ////////// write any thing you want to show to the user while uplaoding the pictures
                    }
                });
    }

    private String getCompleteAdress(double Lati, double longi) {
        String completeadress = null;
        Geocoder geocoder = new Geocoder(AddHostelForm.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(Lati, longi, 1);
            if (addresses != null) {
                Address returnAddress = addresses.get(0);
                StringBuilder stringBuilderReturnAdress = new StringBuilder(null);
                for (int i = 0; i < returnAddress.getMaxAddressLineIndex(); i++) {
                    stringBuilderReturnAdress.append(returnAddress.getAddressLine(i)).append("\n");
                }

                completeadress = stringBuilderReturnAdress.toString();
            } else {
                Toast.makeText(this, "No Specific Name found for the location selected", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

        return completeadress;
    }
}