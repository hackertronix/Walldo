package io.execube.monotype.walldo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hackertronix on 20/03/17.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="wallpapers.db";

    public static final int DATABASE_VERSION =1;


    public FavoriteDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVOURITES_TABLE= "CREATE TABLE "+
                FavoriteWallpaperContract.FavoriteWallpaperEntry.TABLE_NAME+" ( "+
                FavoriteWallpaperContract.FavoriteWallpaperEntry._ID + " INTEGER PRIMARY KEY, "+
                FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_FORMAT + " TEXT NOT NULL, "+
                FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_FILENAME + " TEXT NOT NULL, "+
                FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "+
                FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_AUTHOR_URL + " TEXT NOT NULL, "+
                FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_POST_URL + "  TEXT NOT NULL, "+
                FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_WIDTH + " INTEGER NOT NULL, " +
                FavoriteWallpaperContract.FavoriteWallpaperEntry.COLUMN_HEIGHT + " INTEGER NOT NULL "+
                ");";

        db.execSQL(SQL_CREATE_FAVOURITES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ FavoriteWallpaperContract.FavoriteWallpaperEntry.TABLE_NAME);
        onCreate(db);
    }
}
