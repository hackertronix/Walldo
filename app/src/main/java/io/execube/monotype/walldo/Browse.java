package io.execube.monotype.walldo;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.execube.monotype.walldo.adapters.WallpapersListAdapter;
import io.execube.monotype.walldo.model.Wallpaper;

import io.execube.monotype.walldo.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        Wallpapers= new ArrayList<>();
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
        outState.putParcelableArrayList(WALLPAPERS_ARRAY, Wallpapers);
        super.onSaveInstanceState(outState);
    }

    private void callUnsplash() {

        progressDialog.setMessage(getRandomMessage());
        progressDialog.show();

        //TODO 1 Add Retrofit service
        //TODO 2 Add Retrofit call

//        UnsplashService unsplashService = UnsplashService.retrofit.create(UnsplashService.class);
//        Call<List<Wallpaper>> call = unsplashService.getWallpaperData();
//
//        call.enqueue(new Callback<List<Wallpaper>>() {
//            @Override
//            public void onResponse(Call<List<Wallpaper>> call, Response<List<Wallpaper>> response) {
//
//                Log.d("TAG",response.body().toString());
//                Wallpapers = (ArrayList<Wallpaper>) response.body();
//                handleRecyclerView(Wallpapers);
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<List<Wallpaper>> call, Throwable throwable) {
//                Toast.makeText(Browse.this, "Failed", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void handleRecyclerView(ArrayList<Wallpaper> wallpapers) {
        mAdapter = new WallpapersListAdapter(wallpapers,this);
        wallpapersRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private String getRandomMessage() {
        Random random= new Random();

        int index = random.nextInt(Utils.PROGRESS_DIALOG_MESSAGES.length);

        return Utils.PROGRESS_DIALOG_MESSAGES[index];
    }


}
