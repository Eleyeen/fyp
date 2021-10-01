package com.hostelfinder.hostellocator.Common.LoginSignUp;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hostelfinder.hostellocator.R;

import java.util.Calendar;


public class SignUp2ndClass extends AppCompatActivity {

    ImageView backbtn;
    Button next, login;
    TextView titleText;
    RadioGroup radioGroup;
    RadioButton selectedGender;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_sign_up2nd_class);

        //Hooks

        backbtn = findViewById(R.id.signupbackbutton);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);
        radioGroup = findViewById(R.id.radio_group);
        datePicker = findViewById(R.id.age_picker);
    }


    public void call3rdSignUpScreen(View view) {

        if (!validateGender() | !validateAge()){
            return;
        }
        selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());
        String _gender = selectedGender.getText().toString();
        Intent intent = new Intent(getApplicationContext(), SignUp3rdClass.class);

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        String _date = day+"/"+month+"/"+year;
        // add transitions
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(backbtn, "transitionbackbutton");
        pairs[1] = new Pair<View, String>(next, "transitionnextbutton");
        pairs[2] = new Pair<View, String>(login, "transitionLoginButton");
        pairs[3] = new Pair<View, String>(titleText, "transitiontitletext");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp2ndClass.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {  ////// -1 means non of the radio button is selected
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userage = datePicker.getYear();
        int isagevalid = currentYear - userage;

        if (isagevalid < 14) {
            Toast.makeText(this, "You are not eligible", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

}