package com.sai.nanodegree.capstone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sai.nanodegree.capstone.data.MusicDbContract.SongCategoriesEntry;
import com.sai.nanodegree.capstone.data.MusicDbContract.SongDetailsEntry;
import com.sai.nanodegree.capstone.data.MusicDbContract.SyncDetailsEntry;
import com.sai.nanodegree.capstone.data.MusicDbContract.UserSongPlayHistoryEntry;

/**
 * Created by smajeti on 5/12/16.
 */
public class MusicDbHelper extends SQLiteOpenHelper {

    private static final String TAG = MusicDbHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "music.db";

    public MusicDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder strb = new StringBuilder(2048);

        strb.append("CREATE TABLE ").append(SongCategoriesEntry.TABLE_NAME).append(" (").
                append(SongCategoriesEntry._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ").
                append(SongCategoriesEntry.COLUMN_NAME).append(" TEXT UNIQUE NOT NULL, ").
                append(SongCategoriesEntry.COLUMN_LEVEL).append(" INTEGER NOT NULL, ").
                append(SongCategoriesEntry.COLUMN_CREATION_DATE).append(" INTEGER NOT NULL, ").
                append(SongCategoriesEntry.COLUMN_LAST_UPDATED_DATE).append(" INTEGER NOT NULL ").
                append(" );");

        final String SQL_CREATE_SONG_CATEGORIES_TABLE = strb.toString();
        Log.d(TAG, SQL_CREATE_SONG_CATEGORIES_TABLE);

        strb = new StringBuilder(2048);

        strb.append("CREATE TABLE ").append(SongDetailsEntry.TABLE_NAME).append(" (").
                append(SongDetailsEntry._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ").
                append(SongDetailsEntry.COLUMN_AAROHANA).append(" TEXT NOT NULL, ").
                append(SongDetailsEntry.COLUMN_AVAROHANA).append(" TEXT NOT NULL, ").
                append(SongDetailsEntry.COLUMN_CATEGORY_KEY).append(" INTEGER NOT NULL, ").
                append(SongDetailsEntry.COLUMN_DETAILS).append(" TEXT NOT NULL, ").
                append(SongDetailsEntry.COLUMN_DURATION).append(" REAL NOT NULL, ").
                append(SongDetailsEntry.COLUMN_PICTURE_URL).append(" TEXT NOT NULL, ").
                append(SongDetailsEntry.COLUMN_VIDEO_URL).append(" TEXT NOT NULL, ").
                append(SongDetailsEntry.COLUMN_RAGAM).append(" TEXT NOT NULL, ").
                append(SongDetailsEntry.COLUMN_TITLE).append(" TEXT NOT NULL, ").
                append(SongDetailsEntry.COLUMN_CREATION_DATE).append(" INTEGER NOT NULL, ").
                append(SongDetailsEntry.COLUMN_LAST_UPDATED_DATE).append(" INTEGER NOT NULL, ").

                append(" FOREIGN KEY (").append(SongDetailsEntry.COLUMN_CATEGORY_KEY).append(") REFERENCES ").
                append(SongCategoriesEntry.TABLE_NAME).append(" (").append(SongCategoriesEntry._ID).append("), ").

                append(" UNIQUE (").append(SongDetailsEntry.COLUMN_TITLE).append(", ").
                append(SongDetailsEntry.COLUMN_RAGAM).append(", ").append(SongDetailsEntry.COLUMN_CATEGORY_KEY).
                append(") ON CONFLICT REPLACE ").

                append(" );");

        final String SQL_CREATE_SONG_DETAILS_TABLE = strb.toString();
        Log.d(TAG, SQL_CREATE_SONG_DETAILS_TABLE);

        strb = new StringBuilder(2048);

        strb.append("CREATE TABLE ").append(SyncDetailsEntry.TABLE_NAME).append(" (").
                append(SyncDetailsEntry._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ").
                append(SyncDetailsEntry.COLUMN_FROM_SERVER_TIME).append(" INTEGER DEFAULT 0, ").
                append(SyncDetailsEntry.COLUMN_TO_SERVER_TIME).append(" INTEGER DEFAULT 0, ").
                append(SyncDetailsEntry.COLUMN_SUCCESS).append(" INTEGER DEFAULT 0, ").

                append(SyncDetailsEntry.COLUMN_CREATION_DATE).append(" INTEGER NOT NULL, ").
                append(SyncDetailsEntry.COLUMN_LAST_UPDATED_DATE).append(" INTEGER NOT NULL ").
                append(" );");

        final String SQL_CREATE_SYNC_DETAILS_TABLE = strb.toString();
        Log.d(TAG, SQL_CREATE_SYNC_DETAILS_TABLE);

        strb = new StringBuilder(2048);

        strb.append("CREATE TABLE ").append(UserSongPlayHistoryEntry.TABLE_NAME).append(" (").
                append(UserSongPlayHistoryEntry._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ").
                append(UserSongPlayHistoryEntry.COLUMN_PLAYED_TIME).append(" REAL DEFAULT 0, ").
                append(UserSongPlayHistoryEntry.COLUMN_SONG_KEY).append(" INTEGER NOT NULL, ").
                append(UserSongPlayHistoryEntry.COLUMN_CREATION_DATE).append(" INTEGER NOT NULL, ").
                append(UserSongPlayHistoryEntry.COLUMN_LAST_UPDATED_DATE).append(" INTEGER NOT NULL, ").

                append(" FOREIGN KEY (").append(UserSongPlayHistoryEntry.COLUMN_SONG_KEY).append(") REFERENCES ").
                append(SongDetailsEntry.TABLE_NAME).append(" (").append(SongDetailsEntry._ID).append("), ").

                append(" UNIQUE (").append(UserSongPlayHistoryEntry.COLUMN_SONG_KEY).append(") ON CONFLICT REPLACE ").

                append(" );");

        final String SQL_CREATE_SONG_PLAY_HISTORY_TABLE = strb.toString();
        Log.d(TAG, SQL_CREATE_SONG_PLAY_HISTORY_TABLE);


        sqLiteDatabase.execSQL(SQL_CREATE_SONG_CATEGORIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SONG_DETAILS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SYNC_DETAILS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SONG_PLAY_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // @todo not supporting database upgrade yet, we delete all the content and recreate the
        // new versions or old versoins
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SongCategoriesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SongDetailsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SyncDetailsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserSongPlayHistoryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
