package com.example.hackertronix.firebaseauthtest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.hackertronix.firebaseauthtest.database.FavoriteWallpaperContract.FavoriteWallpaperEntry;

/**
 * Created by hackertronix on 20/03/17.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="wallpapers.db";

    public static final int DATABSE_VERSION=1;


    public FavoriteDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVOURITES_TABLE= "CREATE TABLE "+
                FavoriteWallpaperEntry.TABLE_NAME+" ( "+
                FavoriteWallpaperEntry._ID + " INTEGER PRIMARY KEY, "+
                FavoriteWallpaperEntry.COLUMN_FORMAT + " TEXT NOT NULL, "+
                FavoriteWallpaperEntry.COLUMN_FILENAME + " TEXT NOT NULL, "+
                FavoriteWallpaperEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "+
                FavoriteWallpaperEntry.COLUMN_AUTHOR_URL + " TEXT NOT NULL, "+
                FavoriteWallpaperEntry.COLUMN_POST_URL + "  TEXT NOT NULL, "+
                FavoriteWallpaperEntry.COLUMN_WIDTH + " INTEGER NOT NULL, " +
                FavoriteWallpaperEntry.COLUMN_HEIGHT + " INTEGER NOT NULL "+
                ");";

        db.execSQL(SQL_CREATE_FAVOURITES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ FavoriteWallpaperEntry.TABLE_NAME);
        onCreate(db);
    }
}
