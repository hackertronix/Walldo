package com.example.hackertronix.firebaseauthtest.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hackertronix.firebaseauthtest.Browse;
import com.example.hackertronix.firebaseauthtest.FullScreenImage;
import com.example.hackertronix.firebaseauthtest.R;
import com.example.hackertronix.firebaseauthtest.model.Wallpaper;
import com.example.hackertronix.firebaseauthtest.utils.API;

import java.util.ArrayList;

/**
 * Created by hackertronix on 18/03/17.
 */

public class WallpapersListAdapter extends RecyclerView.Adapter<WallpapersListAdapter.WallpaperViewHolder> {

    private int mNumberOfItems;
    private ArrayList<Wallpaper> mWallpapers;
    private Context mContext;
    private Wallpaper tempWall;


    @Override
    public WallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext=parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.wallpaper_list_item,parent,false);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WallpaperViewHolder holder, int position) {

        Wallpaper wallpaper= mWallpapers.get(position);
        tempWall=mWallpapers.get(position);
        holder.bind(wallpaper);
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }

    public WallpapersListAdapter(ArrayList<Wallpaper> wallpapers, Context context) {
        mNumberOfItems=wallpapers.size();
        //mContext=context;
        mWallpapers=wallpapers;

    }

    public class WallpaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView wallpaperPreview;
        private Wallpaper mWallpaper;

        public WallpaperViewHolder(View itemView) {
            super(itemView);
            wallpaperPreview= (ImageView)itemView.findViewById(R.id.wallpaper_preview_small);
            itemView.setOnClickListener(this);
        }

        public void bind(Wallpaper wallpaper)
        {

            //TODO ADD 500 and 750 to LIST_IMAGE_ENDPOINT

            mWallpaper=wallpaper;
            Glide.with(mContext).load(API.LIST_IMAGE_ENDPOINT+String.valueOf(mWallpaper.getId()))
                    .error(R.drawable.error)
                    .placeholder(R.drawable.placeholder)
                    .crossFade()
                    .into(wallpaperPreview);


        }

        @Override
        public void onClick(View v) {

            //Toast.makeText(v.getContext(), mWallpaper.getId()+" was clicked", Toast.LENGTH_SHORT).show();

                Intent intent= new Intent(v.getContext(),FullScreenImage.class);
                intent.putExtra("PARCEL",mWallpaper);
                v.getContext().startActivity(intent);


        }
    }


//    public interface openFullScreenListener{
//        void openDetails(Wallpaper wallpaper);
//    }
}
