package com.example.hackertronix.firebaseauthtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hackertronix.firebaseauthtest.adapters.WallpapersListAdapter;
import com.example.hackertronix.firebaseauthtest.model.Wallpaper;
import com.example.hackertronix.firebaseauthtest.network.JSONParser;
import com.example.hackertronix.firebaseauthtest.network.NetworkUtils;

import com.example.hackertronix.firebaseauthtest.utils.API;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class Browse extends AppCompatActivity  {

    private static final String WALLPAPERS_ARRAY = "wallpapers_array";
    private Typeface SFUI;

    private Toolbar toolbar;
    private TextView toolbar_tv;
    private ProgressDialog progressDialog;

    private RecyclerView wallpapersRecyclerView;

    private WallpapersListAdapter mAdapter;

    private ArrayList<Wallpaper> Wallpapers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        progressDialog = new ProgressDialog(this);
        View view = findViewById(R.id.browse_container);
        toolbar=(Toolbar)findViewById(R.id.toolbar_browse);

        wallpapersRecyclerView=(RecyclerView)findViewById(R.id.wallpaper_recyclerview);

        handleOrientation();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);

        }

        if(savedInstanceState!=null&&savedInstanceState.containsKey(WALLPAPERS_ARRAY))
        {
            Wallpapers=savedInstanceState.getParcelableArrayList(WALLPAPERS_ARRAY);
            mAdapter= new WallpapersListAdapter(Wallpapers,getApplicationContext());
            wallpapersRecyclerView.setAdapter(mAdapter);

            //Toast.makeText(this, "Magic, see? No Network calls", Toast.LENGTH_SHORT).show();
        }
        else {
                callUnsplash();
        }




        toolbar_tv=(TextView)findViewById(R.id.toolbar_title);


        SFUI= Typeface.createFromAsset(getAssets(),"fonts/sftext.otf");

        toolbar_tv.setTypeface(SFUI);




    }

    private void handleOrientation() {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            wallpapersRecyclerView.setLayoutManager(new
                    GridLayoutManager(this, 2));
        }
        else{
            wallpapersRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(WALLPAPERS_ARRAY,Wallpapers);
        super.onSaveInstanceState(outState);
    }

    private void callUnsplash() {

        progressDialog.setMessage("Awesome things are happening, just wait a little while....");
        progressDialog.show();

        new UnsplashQueryTask().execute();

    }



    private class UnsplashQueryTask extends AsyncTask<Void, Void, String>{

        private String response;

        @Override
        protected String doInBackground(Void... params) {


            try {
                 response = NetworkUtils.getResponseFromUnsplash(API.API_ENDPOINT);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            
            if (response != null && !response.equals("")) {

                try {
                    Wallpapers = JSONParser.parseWallpapperData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter= new WallpapersListAdapter(Wallpapers,getApplicationContext());
                wallpapersRecyclerView.setAdapter(mAdapter);

                //Toast.makeText(Browse.this, "Fetched data for "+Wallpapers.size()+" wallpapers", Toast.LENGTH_LONG).show();
                mAdapter.notifyDataSetChanged();

            }
            
            else {
                Toast.makeText(Browse.this, "Error occured", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
