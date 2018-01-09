package io.execube.monotype.walldo;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import io.execube.monotype.walldo.model.Wallpaper;
import io.execube.monotype.walldo.utils.Utils;
import io.execube.monotype.walldo.widget.FavoriteWidgetProvider;

import java.io.IOException;

import io.execube.monotype.walldo.database.FavoriteWallpaperContract;

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
    private Wallpaper wallpaper;
    public static final int TASK_LOADER_ID = 1;
    private Typeface GothamRounded;

    private Button setWallpaperButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        wallpaper = getIntent().getExtras().getParcelable("PARCEL");


        artistTextView=(TextView)findViewById(R.id.artist_tv);
        fullImage=(ImageView)findViewById(R.id.full_image_view);
        mProgressbar=(ProgressBar)findViewById(R.id.progressbar);
        setWallpaperButton=(Button)findViewById(R.id.set_wallpaper);


        favouriteButton=(FloatingActionButton)findViewById(R.id.fab);


        Signalist=Typeface.createFromAsset(getAssets(),"fonts/Signalist.otf");
        GothamRounded=Typeface.createFromAsset(getAssets(),"fonts/Gotham-Rounded-Medium.ttf");



        artistTextView.setTypeface(Signalist);
        setWallpaperButton.setTypeface(GothamRounded);

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


        artistTextView.setText(wallpaper.getAuthor());
        artistTextView.setContentDescription("This image was photographed by "+wallpaper.getAuthor());


        Glide.with(this).load(Utils.FULL_RES_IMAGE_ENDPOINT+ width+"/"+height+"?image="+String.valueOf(wallpaper.getId()))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mProgressbar.setVisibility(View.GONE);
                        setWallpaperButton.setVisibility(View.VISIBLE);
                        favouriteButton.show();
                        return false;
                    }
                })
                .into(new SimpleTarget<Bitmap>(w, h) {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                // Do something with bitmap here.

                fullImage.setImageBitmap(bitmap);
            }
        });

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFavorite();
            }
        });

        setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageAsWallpaper();
            }
        });

        //getSupportLoaderManager().initLoader(TASK_LOADER_ID,null,this);

    }


    private void checkFav() {



        Uri singleItemUri = ContentUris.withAppendedId(FavoriteWallpaperContract.FavoriteWallpaperEntry.CONTENT_URI, wallpaper.getId());

        Cursor cursor = getContentResolver().query(singleItemUri,null,null,null,null);

        if(cursor.getCount() > 0)
        {
            favouriteButton.setImageResource(R.drawable.ic_favorite_black_24dp);
            cursor.close();
        }

        else{
            favouriteButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            cursor.close();
        }
    }

    private boolean isWallFav(Cursor cursor) {

        if(cursor.getCount()>0)
        {
            return true;
        }
        else {
            return false;
        }
    }


    private void handleFavorite() {

        Uri singleItemUri = ContentUris.withAppendedId(FavoriteWallpaperContract.FavoriteWallpaperEntry.CONTENT_URI, wallpaper.getId());
        Cursor cursor = getContentResolver().query(singleItemUri,null,null,null,null);
        boolean isFav = isWallFav(cursor);
        if(isFav == false)
        {
            ContentValues contentValues = new ContentValues();

            String format = wallpaper.getFormat();
            String filename = wallpaper.getFilename();
            String author = wallpaper.getAuthor();
            String author_url = wallpaper.getAuthor_url();
            String post_url = wallpaper.getPost_url();

            int width = wallpaper.getWidth();
            int height = wallpaper.getHeight();
            int id = wallpaper.getId();

            contentValues.put(FavoriteWallpaperContract.FavoriteWallpaperEntry._ID,id);
            contentValues.put(FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_FORMAT,format);
            contentValues.put(FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_FILENAME,filename);
            contentValues.put(FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_AUTHOR,author);
            contentValues.put(FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_AUTHOR_URL,author_url);
            contentValues.put(FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_POST_URL,post_url);
            contentValues.put(FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_WIDTH,width);
            contentValues.put(FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_HEIGHT,height);


            Uri uri = getContentResolver().insert(FavoriteWallpaperContract.FavoriteWallpaperEntry.CONTENT_URI,contentValues);
            favouriteButton.setImageResource(R.drawable.ic_favorite_black_24dp);

            updateWidgets(getApplicationContext());
        }
        else
        {


            int deleted= getContentResolver().delete(singleItemUri,null,null);

            favouriteButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);

            updateWidgets(this);
        }


    }

    private void updateWidgets(Context context) {



        ComponentName name = new ComponentName(this, FavoriteWidgetProvider.class);
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(name);

        Intent intent = new Intent(this,FavoriteWidgetProvider.class);

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,ids);
        sendBroadcast(intent);


    }

    private void setImageAsWallpaper() {


     new AlertDialog.Builder(this).
             setMessage(R.string.set_wallpaper_confirmation)
             .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                     try{

                         Bitmap bitmap = ((BitmapDrawable)fullImage.getDrawable()).getBitmap();
                         wallpaperManager.setBitmap(bitmap);
                     }catch (IOException e)
                     {
                         e.printStackTrace();
                     }

                     showSuccessDialog();

                 }
             })
             .setNegativeButton("No",null)
             .show();




    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.image_set)
                .setPositiveButton("OK", null)
                .show();
    }


    private void getScreenDimensions() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        w=size.x;
        h=size.y;

        width= String.valueOf(size.x);
        height=String.valueOf(size.y);


    }


}


