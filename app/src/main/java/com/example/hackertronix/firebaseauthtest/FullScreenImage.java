package com.example.hackertronix.firebaseauthtest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.hackertronix.firebaseauthtest.model.Wallpaper;
import com.example.hackertronix.firebaseauthtest.utils.API;

public class FullScreenImage extends AppCompatActivity {

    private TextView artistTextView;
    private ImageView fullImage;
    private ProgressBar mProgressbar;
    private FloatingActionButton favouriteButton;
    private Typeface SFUI;
    
    private String width;
    private String height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);


        artistTextView=(TextView)findViewById(R.id.artist_tv);
        fullImage=(ImageView)findViewById(R.id.full_image_view);
        mProgressbar=(ProgressBar)findViewById(R.id.progressbar);


        //favouriteButton=(FloatingActionButton)findViewById(R.id.favourites_btn);


        SFUI=Typeface.createFromAsset(getAssets(),"fonts/sftext.otf");
        artistTextView.setTypeface(SFUI);

        View view=findViewById(R.id.fullimage_container);

        if(Build.VERSION.SDK_INT>=21) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }

        Wallpaper wallpaper = getIntent().getExtras().getParcelable("PARCEL");

        artistTextView.setText(wallpaper.getAuthor());

        getScreenDimensions();
        
        Glide.with(this).load(API.FULL_RES_IMAGE_ENDPOINT+width+"/"+height+"?image="+String.valueOf(wallpaper.getId()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        mProgressbar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mProgressbar.setVisibility(View.GONE);
                        return false;

                    }
                }).crossFade()

                .into(fullImage);

    }

    private void getScreenDimensions() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width= String.valueOf(size.x);
        height=String.valueOf(size.y);

        //Toast.makeText(this,width+" x "+height,Toast.LENGTH_SHORT).show();
    }


}
