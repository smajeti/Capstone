package com.sai.nanodegree.capstone.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.List;

/**
 * Created by smajeti on 5/11/16.
 */
public class MusicDbContract {

    public static final String CONTENT_AUTHORITY = "com.sai.nanodegree.capstone";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SONG_CATEGORIES = "SongCategories";
    public static final String PATH_SONG_DETAILS = "SongDetails";
    public static final String PATH_SYNC_DETAILS = "SyncDetails";
    public static final String PATH_USER_SONG_PLAY_HISTORY = "UserSongPlayHistory";

    public interface MusicBaseColumns extends BaseColumns {
        // common columns
        public static final String COLUMN_CREATION_DATE = "creation_date";
        public static final String COLUMN_LAST_UPDATED_DATE = "last_updated_date";
    }

    public static final class SongCategoriesEntry implements MusicBaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SONG_CATEGORIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SONG_CATEGORIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SONG_CATEGORIES;

        // Table name
        public static final String TABLE_NAME = "song_categories";

        // column names
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LEVEL = "level";

        public static Uri buildCategoriesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class SongDetailsEntry implements MusicBaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SONG_DETAILS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SONG_DETAILS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SONG_DETAILS;

        // Table name
        public static final String TABLE_NAME = "song_details";

        // column names
        public static final String COLUMN_AAROHANA = "aarohana";
        public static final String COLUMN_AVAROHANA = "avarohana";
        public static final String COLUMN_CATEGORY_KEY = "category_key";
        public static final String COLUMN_DETAILS = "details";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_PICTURE_URL = "picture_url";
        public static final String COLUMN_VIDEO_URL = "video_url";
        public static final String COLUMN_RAGAM = "ragam";
        public static final String COLUMN_TITLE = "title";

        public static Uri buildSongDetailsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildSongDetailsWithCategory(String categoryId) {
            return CONTENT_URI.buildUpon().appendPath(COLUMN_CATEGORY_KEY).appendPath(categoryId).build();
        }

        public static String getCategoryIdFromUri(Uri uri) {
            List<String> pathSegments = uri.getPathSegments();
            return pathSegments.get(2); // it's of format category_key/number
        }
    }

    public static final class SyncDetailsEntry implements MusicBaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SYNC_DETAILS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SYNC_DETAILS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SYNC_DETAILS;

        // Table name
        public static final String TABLE_NAME = "sync_details";

        // column names
        public static final String COLUMN_FROM_SERVER_TIME = "from_server_time";
        public static final String COLUMN_TO_SERVER_TIME = "to_server_time";
        public static final String COLUMN_SUCCESS = "success";

        public static Uri buildSyncDetailsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class UserSongPlayHistoryEntry implements MusicBaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_SONG_PLAY_HISTORY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER_SONG_PLAY_HISTORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER_SONG_PLAY_HISTORY;

        // Table name
        public static final String TABLE_NAME = "user_song_play_history";

        // column names
        public static final String COLUMN_PLAYED_TIME = "played_time";
        public static final String COLUMN_SONG_KEY = "song_id";

        public static Uri buildUserSongPlayHistoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
