package com.example.hackertronix.firebaseauthtest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.widget.TextView;

public class Browse extends AppCompatActivity {

    private Typeface SFUI;

    private Toolbar toolbar;
    private TextView toolbar_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        View view = findViewById(R.id.browse_container);
        toolbar=(Toolbar)findViewById(R.id.toolbar_browse);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);

        }


        toolbar_tv=(TextView)findViewById(R.id.toolbar_title);
        SFUI= Typeface.createFromAsset(getAssets(),"fonts/sftext.otf");

        toolbar_tv.setTypeface(SFUI);

    }
}
