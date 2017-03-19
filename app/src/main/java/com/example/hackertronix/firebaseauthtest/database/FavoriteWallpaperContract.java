package com.example.hackertronix.firebaseauthtest.database;

import android.provider.BaseColumns;

/**
 * Created by hackertronix on 20/03/17.
 */

public class FavoriteWallpaperContract {


    public static final class FavoriteWallpaperEntry implements BaseColumns{


        public static final String TABLE_NAME="favourite";

        public static final String COLUMN_FORMAT="format";
        public static final String COLUMN_FILENAME="filename";
        public static final String COLUMN_AUTHOR="author";
        public static final String COLUMN_AUTHOR_URL="author_url";
        public static final String COLUMN_POST_URL="post_url";
        public static final String COLUMN_WIDTH="width";
        public static final String COLUMN_HEIGHT="height";
        public static final String COLUMN_ID="post_id";

    }
}
