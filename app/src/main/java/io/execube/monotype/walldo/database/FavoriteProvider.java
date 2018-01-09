package io.execube.monotype.walldo.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by hackertronix on 20/03/17.
 */

public class FavoriteProvider extends ContentProvider {

    private FavoriteDbHelper mDbHelper;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public static final int FAVORITES=100;
    public static final int FAVORITES_WITH_ID=101;


    public static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


        uriMatcher.addURI(FavoriteWallpaperContract.AUTHORITY,FavoriteWallpaperContract.PATH_FAVORITES,FAVORITES);

        uriMatcher.addURI(FavoriteWallpaperContract.AUTHORITY,FavoriteWallpaperContract.PATH_FAVORITES+"/#",FAVORITES_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {

        Context context = getContext();
        mDbHelper = new FavoriteDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {


        final SQLiteDatabase db= mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;
        switch (match)
        {

            case FAVORITES :

                retCursor = db.query(FavoriteWallpaperContract.FavoriteWallpaperEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                        );
                break;

            case FAVORITES_WITH_ID :

                String id = uri.getPathSegments().get(1);

                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};

                retCursor=  db.query(FavoriteWallpaperContract.FavoriteWallpaperEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default: throw new UnsupportedOperationException("Unknown uri: "+uri.toString());


        }



        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match)
        {
            case FAVORITES :

                long id = db.insert(FavoriteWallpaperContract.FavoriteWallpaperEntry.TABLE_NAME,null,values);
                if(id > 0)
                {
                    returnUri= ContentUris.withAppendedId(FavoriteWallpaperContract.FavoriteWallpaperEntry.CONTENT_URI,id);
                } else {
                    throw new SQLiteException("Failed to insert row into "+uri);
                }
                break;

            default: throw new UnsupportedOperationException("Unknown uri: "+uri);

        }


         getContext().getContentResolver().notifyChange(uri,null);

         return returnUri;
    }

//    private void updateWidget(Context context) {
//        Intent dbUpdateIntent = new Intent(Utils.ACTION_DATABASE_UPDATED).setPackage(context.getPackageName());
//        context.sendBroadcast(dbUpdateIntent);
//    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match= sUriMatcher.match(uri);

        int favoritesDeleted;
        switch (match)
        {
            case FAVORITES_WITH_ID :
                String id = uri.getPathSegments().get(1);

                favoritesDeleted= db.delete(FavoriteWallpaperContract.FavoriteWallpaperEntry.TABLE_NAME,"_id=?", new String[]{id});
                break;

            default: throw new UnsupportedOperationException("Unknown uri: "+uri.toString());
        }

        if(favoritesDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }



        return favoritesDeleted;



    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
