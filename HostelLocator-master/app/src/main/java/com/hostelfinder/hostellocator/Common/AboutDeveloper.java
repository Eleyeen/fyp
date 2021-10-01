package com.hostelfinder.hostellocator.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.WindowManager;
import android.widget.TextView;

import com.hostelfinder.hostellocator.R;

public class AboutDeveloper extends AppCompatActivity {
    TextView ambition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about_developer);

        ambition = (TextView) findViewById(R.id.desc);
        ambition.setMovementMethod(new ScrollingMovementMethod());
    }
}