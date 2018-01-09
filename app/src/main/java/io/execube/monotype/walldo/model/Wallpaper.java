package io.execube.monotype.walldo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hackertronix on 18/03/17.
 */

public class Wallpaper implements Parcelable {

    private String format;
    private String filename;
    private String author;
    private String author_url;
    private String post_url;

    private int width;
    private int height;
    private int id;


    private Wallpaper(Parcel source) {
        format = source.readString();
        filename = source.readString();
        author = source.readString();
        author_url = source.readString();
        post_url = source.readString();

        width = source.readInt();
        height = source.readInt();
        id = source.readInt();
    }




    public Wallpaper() {

    }


    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor_url() {
        return author_url;
    }

    public void setAuthor_url(String author_url) {
        this.author_url = author_url;
    }

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(format);
        dest.writeString(filename);
        dest.writeString(author);
        dest.writeString(author_url);
        dest.writeString(post_url);

        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(id);

    }


    public static final Parcelable.Creator<Wallpaper> CREATOR =  new Parcelable.Creator<Wallpaper>(){

        @Override
        public Wallpaper createFromParcel(Parcel source) {
            return new Wallpaper(source);
        }

        @Override
        public Wallpaper[] newArray(int size) {
            return new Wallpaper[size];
        }
    };
}


