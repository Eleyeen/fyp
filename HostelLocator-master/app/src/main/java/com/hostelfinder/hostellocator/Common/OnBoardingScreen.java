package com.hostelfinder.hostellocator.Common;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hostelfinder.hostellocator.HelperClasses.SliderAdapter;
import com.hostelfinder.hostellocator.R;


public class OnBoardingScreen extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout dots_layout;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    Button getstartedbutton;
    Animation fadin;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding_screen);
        ////hooks
        viewPager = findViewById(R.id.slider);
        dots_layout = findViewById(R.id.dots);
        getstartedbutton = findViewById(R.id.getStartedButton);
        getstartedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotodash = new Intent(getApplicationContext(), The_Dashboard.class);
                startActivity(gotodash);
                finish();
            }
        });

        // call adapter
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

    public void skip(View view) {
        Intent skip = new Intent(getApplicationContext(), The_Dashboard.class);
        startActivity(skip);
        finish();

    }

    public void next(View view) {
        viewPager.setCurrentItem(currentPosition + 1);
    }

    private void addDots(int position) {

        dots = new TextView[4];
        dots_layout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dots_layout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
            addDots(position);
            if (position == 0) {
                getstartedbutton.setVisibility(View.INVISIBLE);
            } else if (position == 1) {
                getstartedbutton.setVisibility(View.INVISIBLE);
            } else if (position == 2) {
                getstartedbutton.setVisibility(View.INVISIBLE);
            } else {
                fadin = AnimationUtils.loadAnimation(OnBoardingScreen.this, R.anim.fadein);
                getstartedbutton.setAnimation(fadin);
                getstartedbutton.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
