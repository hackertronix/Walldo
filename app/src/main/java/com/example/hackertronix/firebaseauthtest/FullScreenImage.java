package com.example.hackertronix.firebaseauthtest;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.hackertronix.firebaseauthtest.model.Wallpaper;
import com.example.hackertronix.firebaseauthtest.utils.API;

import java.io.IOException;

public class FullScreenImage extends AppCompatActivity {

    private TextView artistTextView;
    private ImageView fullImage;
    private ProgressBar mProgressbar;
    private FloatingActionButton favouriteButton;
    private String width;
    private String height;
    private Typeface Signalist;
    private int h;
    private int w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);


        artistTextView=(TextView)findViewById(R.id.artist_tv);
        fullImage=(ImageView)findViewById(R.id.full_image_view);
        mProgressbar=(ProgressBar)findViewById(R.id.progressbar);


        favouriteButton=(FloatingActionButton)findViewById(R.id.fab);


        Signalist=Typeface.createFromAsset(getAssets(),"fonts/Signalist.otf");
        artistTextView.setTypeface(Signalist);

        getScreenDimensions();


        checkFav();


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


        Glide.with(this).load(API.FULL_RES_IMAGE_ENDPOINT+width+"/"+height+"?image="+String.valueOf(wallpaper.getId()))
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mProgressbar.setVisibility(View.GONE);
                        favouriteButton.show();
                        return false;
                    }
                })
                .into(new SimpleTarget<Bitmap>(1080, 1920) {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                // Do something with bitmap here.

                fullImage.setImageBitmap(bitmap);
            }
        });

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageAsWallpaper();
            }
        });

    }

    private void setImageAsWallpaper() {

        WallpaperManager wallpapermanager= WallpaperManager.getInstance(this);
        Bitmap wallpaper= ((BitmapDrawable)fullImage.getDrawable()).getBitmap();

        try {
            wallpapermanager.setBitmap(wallpaper);
            Toast.makeText(this, "Wallpaper Set Successfully!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void checkFav() {
    }

    private void getScreenDimensions() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        w=size.x;
        h=size.y;

        width= String.valueOf(size.x);
        height=String.valueOf(size.y);

        //Toast.makeText(this,width+" x "+height,Toast.LENGTH_SHORT).show();
    }


}


