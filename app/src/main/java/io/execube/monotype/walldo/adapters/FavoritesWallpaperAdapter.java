package io.execube.monotype.walldo.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import io.execube.monotype.walldo.FullScreenImage;
import io.execube.monotype.walldo.R;
import io.execube.monotype.walldo.model.Wallpaper;
import io.execube.monotype.walldo.database.FavoriteWallpaperContract.FavoriteWallpaperEntry;
import io.execube.monotype.walldo.utils.Utils;

/**
 * Created by hackertronix on 20/03/17.
 */

public class FavoritesWallpaperAdapter extends RecyclerView.Adapter<FavoritesWallpaperAdapter.FavoritesViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public FavoritesWallpaperAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.wallpaper_list_item,parent,false);



        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {

        Wallpaper wallpaper = new Wallpaper();

        int idIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry._ID);
        int formatIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_FORMAT);
        int filenameIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_FILENAME);
        int authorIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_AUTHOR);
        int authorUrlIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_AUTHOR_URL);
        int postUrlIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_POST_URL);
        int widthIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_WIDTH);
        int heightIndex = mCursor.getColumnIndex(FavoriteWallpaperEntry.COLUMN_HEIGHT);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        final String format = mCursor.getString(formatIndex);
        final String filename= mCursor.getString(filenameIndex);
        final String author = mCursor.getString(authorIndex);
        final String author_url = mCursor.getString(authorUrlIndex);
        final String post_url = mCursor.getString(postUrlIndex);
        final int width= mCursor.getInt(widthIndex);
        final int height = mCursor.getInt(heightIndex);

        wallpaper.setId(id);
        wallpaper.setFormat(format);
        wallpaper.setFilename(filename);
        wallpaper.setAuthor(author);
        wallpaper.setAuthor_url(author_url);
        wallpaper.setPost_url(post_url);
        wallpaper.setWidth(width);
        wallpaper.setHeight(height);

        holder.bind(wallpaper);
    }

    @Override
    public int getItemCount() {
        if(mCursor==null)
        {
            return 0;
        }

        return mCursor.getCount();
    }


    public Cursor swapCursor(Cursor c)
    {
        if(mCursor == c)
        {
            return null;
        }

        Cursor temp = mCursor;
        this.mCursor = c;

        if( c != null)
        {
            this.notifyDataSetChanged();
        }

        return temp;
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private ImageView wallpaperPreview;
        private Wallpaper mWallpaper;

        public FavoritesViewHolder(View itemView) {
            super(itemView);

            wallpaperPreview = (ImageView)itemView.findViewById(R.id.wallpaper_preview_small);
            itemView.setOnClickListener(this);
        }

        public void bind(Wallpaper wallpaper) {

           mWallpaper = wallpaper;
            Glide.with(mContext).load(Utils.LIST_IMAGE_ENDPOINT+String.valueOf(mWallpaper.getId()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.error)
                    .placeholder(R.drawable.placeholder)
                    .crossFade()
                    .into(wallpaperPreview);

        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(), FullScreenImage.class);

            intent.putExtra("PARCEL",mWallpaper);
            v.getContext().startActivity(intent);

        }
    }
}
