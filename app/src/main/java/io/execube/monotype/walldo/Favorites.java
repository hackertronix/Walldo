package io.execube.monotype.walldo;

import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.execube.monotype.walldo.adapters.FavoritesWallpaperAdapter;
import io.execube.monotype.walldo.database.FavoriteWallpaperContract;

public class Favorites extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    public static final int TASK_LOADER_ID = 0;

    private FavoritesWallpaperAdapter mAdapter;
    private RecyclerView mFavoritesRecyclerView;
    private TextView toolbar_tv;
    private Typeface SFUI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


        mFavoritesRecyclerView = (RecyclerView) findViewById(R.id.favorites_recyclerview);

        handleOrientation();
        toolbar_tv = (TextView) findViewById(R.id.toolbar_title);


        View view = findViewById(R.id.favorites_container);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);

        }

        SFUI = Typeface.createFromAsset(getAssets(), "fonts/sftext.otf");

        toolbar_tv.setTypeface(SFUI);


        mAdapter = new FavoritesWallpaperAdapter(this);
        mFavoritesRecyclerView.setAdapter(mAdapter);


        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

    }

    private void handleOrientation() {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            mFavoritesRecyclerView.setLayoutManager(new
                    GridLayoutManager(this, 2));
        }
        else{
            mFavoritesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mFavoritesData = null;

            @Override
            protected void onStartLoading() {

                if (mFavoritesData != null) {
                    deliverResult(mFavoritesData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(FavoriteWallpaperContract.FavoriteWallpaperEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            FavoriteWallpaperContract.FavoriteWallpaperEntry._ID);

                } catch (Exception e) {
                    Log.e("TAG", "asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }

}
