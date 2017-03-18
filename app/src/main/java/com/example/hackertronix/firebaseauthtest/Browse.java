package com.example.hackertronix.firebaseauthtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.widget.TextView;
import com.example.hackertronix.firebaseauthtest.network.NetworkUtils;

import com.example.hackertronix.firebaseauthtest.network.NetworkUtils;
import com.example.hackertronix.firebaseauthtest.utils.API;

import java.io.IOException;

public class Browse extends AppCompatActivity {

    private Typeface SFUI;

    private Toolbar toolbar;
    private TextView toolbar_tv,response_tv;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        progressDialog = new ProgressDialog(this);
        View view = findViewById(R.id.browse_container);
        toolbar=(Toolbar)findViewById(R.id.toolbar_browse);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);

        }


        toolbar_tv=(TextView)findViewById(R.id.toolbar_title);
        response_tv=(TextView)findViewById(R.id.respinse_textView);


        SFUI= Typeface.createFromAsset(getAssets(),"fonts/sftext.otf");

        toolbar_tv.setTypeface(SFUI);

        callUnsplash();


    }

    private void callUnsplash() {

        progressDialog.setMessage("Getting data from Unsplash...");
        progressDialog.show();

        new UnsplashQueryTask().execute();

    }


    private class UnsplashQueryTask extends AsyncTask<Void, Void, String>{

        private String reposnse;

        @Override
        protected String doInBackground(Void... params) {


            try {
                 reposnse = NetworkUtils.getResponseFromUnsplash(API.API_ENDPOINT);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return reposnse;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            response_tv.setText(reposnse);
        }
    }
}
