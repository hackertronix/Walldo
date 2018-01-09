package io.execube.monotype.walldo.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hackertronix on 20/03/17.
 */

public class FavoriteWallpaperContract {

    public static final String AUTHORITY = "com.example.hackertronix.firebaseauthtest";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final String PATH_FAVORITES ="favourites";

    public static final class FavoriteWallpaperEntry implements BaseColumns{


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES)
                .build();


        public static final String TABLE_NAME="favourites";

        public static final String COLUMN_FORMAT="format";
        public static final String COLUMN_FILENAME="filename";
        public static final String COLUMN_AUTHOR="author";
        public static final String COLUMN_AUTHOR_URL="author_url";
        public static final String COLUMN_POST_URL="post_url";
        public static final String COLUMN_WIDTH="width";
        public static final String COLUMN_HEIGHT="height";
        //public static final String COLUMN_ID="post_id";



    }
}
