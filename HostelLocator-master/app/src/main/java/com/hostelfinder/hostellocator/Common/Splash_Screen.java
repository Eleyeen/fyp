package com.hostelfinder.hostellocator.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.UserDashboard;

public class Splash_Screen extends AppCompatActivity {

    public static int SPLASH_TIMER = 1200;
    ImageView splashimage;
    TextView splashpoweredtext;
    Animation fadin;
    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        /////Hookss
        splashimage = findViewById(R.id.splash_image);
        splashpoweredtext = findViewById(R.id.splash_powerd_text);
        fadin = AnimationUtils.loadAnimation(this, R.anim.fadein);
        ///set Animations

        splashpoweredtext.setAnimation(fadin);
        splashimage.setAnimation(fadin);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);
                if (isFirstTime) {
                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime", false);
                    editor.apply();

                    Intent i = new Intent(getApplicationContext(), OnBoardingScreen.class);
                    startActivity(i);
                    finish();

                } else {
                    Intent i = new Intent(getApplicationContext(), The_Dashboard.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIMER);
    }
}