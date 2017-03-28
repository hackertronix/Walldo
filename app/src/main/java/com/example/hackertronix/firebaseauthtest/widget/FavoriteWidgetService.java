package com.example.hackertronix.firebaseauthtest.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.hackertronix.firebaseauthtest.R;
import com.example.hackertronix.firebaseauthtest.database.FavoriteWallpaperContract.FavoriteWallpaperEntry;
import com.example.hackertronix.firebaseauthtest.model.Wallpaper;
import com.example.hackertronix.firebaseauthtest.utils.Utils;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by hackertronix on 20/03/17.
 */

public class FavoriteWidgetService extends RemoteViewsService {

    private Typeface Signalist;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Cursor cursor = this.getContentResolver().query(
             FavoriteWallpaperEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        cursor.close();
        return new WidgetDataProvider(this,intent,cursor);
    }

    public class WidgetDataProvider implements RemoteViewsFactory{

        private Context context;
        private Intent intent;
        private Cursor mCursor = null;


        public WidgetDataProvider(Context context, Intent intent, Cursor mCursor) {
            this.context = context;
            this.intent = intent;
            this.mCursor = mCursor;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

            if (mCursor != null) {
                mCursor.close();
            }

            final long identityToken = Binder.clearCallingIdentity();

            mCursor = FavoriteWidgetService.this.getContentResolver().query(FavoriteWallpaperEntry.CONTENT_URI,
                    null,null,null,null);

            Binder.restoreCallingIdentity(identityToken);


        }

        @Override
        public void onDestroy() {

            if (mCursor != null) {
                mCursor.close();
                mCursor = null;
            }


        }

        @Override
        public int getCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public RemoteViews getViewAt(int position) {

            if (position == AdapterView.INVALID_POSITION ||
                    mCursor == null || !mCursor.moveToPosition(position)) {
                return null;
            }

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_item_widget);
            mCursor.moveToPosition(position);

            remoteViews.setTextViewText(R.id.list_widget_text, mCursor.getString(mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_AUTHOR)));

            int id = mCursor.getInt(mCursor.getColumnIndex(FavoriteWallpaperEntry._ID));
            String State_Null = "null";



            if (Objects.equals(id, State_Null)) {
                Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.error);
                remoteViews.setImageViewBitmap(R.id.list_widget_icon, icon);
            } else {

                try {

                    Bitmap bitmap = Glide.with(context).load(Utils.WIDGET_IMAGE_ENDPOINT+String.valueOf(id))
                            .asBitmap()
                            .into(40,40)
                            .get();

                    remoteViews.setImageViewBitmap(R.id.list_widget_icon, bitmap);

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }



            final Intent fillInIntent = new Intent();

            Wallpaper wallpaper = new Wallpaper();

            int formatIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_FORMAT);
            int filenameIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_FILENAME);
            int authorIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_AUTHOR);
            int author_UrlIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_AUTHOR_URL);
            int post_UrlIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_POST_URL);
            int widthIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_WIDTH);
            int heightIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_HEIGHT);
            int idIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry._ID);

            String format = mCursor.getString(formatIndex);
            String filename = mCursor.getString(filenameIndex);
            String author = mCursor.getString(authorIndex);
            String author_url = mCursor.getString(author_UrlIndex);
            String post_url = mCursor.getString(post_UrlIndex);

            int width= mCursor.getInt(widthIndex);
            int height = mCursor.getInt(heightIndex);
            int wall_id = mCursor.getInt(idIndex);

            wallpaper.setHeight(height);
            wallpaper.setFormat(format);
            wallpaper.setWidth(width);
            wallpaper.setPost_url(post_url);
            wallpaper.setId(wall_id);
            wallpaper.setAuthor_url(author_url);
            wallpaper.setFilename(filename);
            wallpaper.setAuthor(author);

            fillInIntent.putExtra("wallpaper" ,wallpaper);

            remoteViews.setOnClickFillInIntent(R.id.widget_list_item,fillInIntent);

            return remoteViews;




        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
