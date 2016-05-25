package com.sai.nanodegree.capstone.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.sai.nanodegree.capstone.R;

import org.w3c.dom.Text;

public class MusicContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int CATEGORIES_CODE = 100;
    private static final int SONG_DETAILS_CODE = 101;
    private static final int SONG_DETAILS_WITH_CATEGORY_CODE = 102;
    private static final int SYNC_DETAILS_CODE = 103;
    private static final int USER_SONG_PLAY_HISTORY_CODE = 104;

    private MusicDbHelper musicDbHelper;

    private static final SQLiteQueryBuilder songDetailsWithCategoryAndPlayedTimeQueryBuilder;

    static {
        songDetailsWithCategoryAndPlayedTimeQueryBuilder = new SQLiteQueryBuilder();
        songDetailsWithCategoryAndPlayedTimeQueryBuilder.setTables(MusicDbContract.SongDetailsEntry.TABLE_NAME + " LEFT OUTER JOIN " +
                        MusicDbContract.UserSongPlayHistoryEntry.TABLE_NAME + " ON " +
                        MusicDbContract.SongDetailsEntry.TABLE_NAME + "." + MusicDbContract.SongDetailsEntry._ID + " = " +
                        MusicDbContract.UserSongPlayHistoryEntry.TABLE_NAME + "." + MusicDbContract.UserSongPlayHistoryEntry.COLUMN_SONG_KEY);
    }

    private static final String categorySelection = MusicDbContract.SongDetailsEntry.TABLE_NAME + "." +
                                                    MusicDbContract.SongDetailsEntry.COLUMN_CATEGORY_KEY + " = ?";

    public MusicContentProvider() {
    }

    @Override
    public boolean onCreate() {
        musicDbHelper = new MusicDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case CATEGORIES_CODE:
                return MusicDbContract.SongCategoriesEntry.CONTENT_TYPE;
            case SONG_DETAILS_CODE:
                return MusicDbContract.SongDetailsEntry.CONTENT_TYPE;
            case SONG_DETAILS_WITH_CATEGORY_CODE:
                return MusicDbContract.SongDetailsEntry.CONTENT_TYPE;
            case SYNC_DETAILS_CODE:
                return MusicDbContract.SyncDetailsEntry.CONTENT_TYPE;
            case USER_SONG_PLAY_HISTORY_CODE:
                return MusicDbContract.UserSongPlayHistoryEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri_err_str) + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor retCursor = null;
        switch (sUriMatcher.match(uri)) {
            case CATEGORIES_CODE:
                retCursor = musicDbHelper.getReadableDatabase().query(
                        MusicDbContract.SongCategoriesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SONG_DETAILS_CODE:
                retCursor = musicDbHelper.getReadableDatabase().query(
                        MusicDbContract.SongDetailsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SONG_DETAILS_WITH_CATEGORY_CODE:
                retCursor = getSongDetailsWithCategory(uri, projection, sortOrder);
                break;
            case SYNC_DETAILS_CODE:
                retCursor = musicDbHelper.getReadableDatabase().query(
                        MusicDbContract.SyncDetailsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case USER_SONG_PLAY_HISTORY_CODE:
                retCursor = musicDbHelper.getReadableDatabase().query(
                        MusicDbContract.UserSongPlayHistoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri_err_str) + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getSongDetailsWithCategory(Uri uri, String[] projection, String sortOrder) {
        String categoryId = MusicDbContract.SongDetailsEntry.getCategoryIdFromUri(uri);
        return songDetailsWithCategoryAndPlayedTimeQueryBuilder.query(musicDbHelper.getReadableDatabase(),
                                projection, categorySelection, new String[] {categoryId}, null, null, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = musicDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        long _id = 0;
        switch (match) {
            case CATEGORIES_CODE:
                _id = db.insert(MusicDbContract.SongCategoriesEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MusicDbContract.SongCategoriesEntry.buildCategoriesUri(_id);
                }
                break;
            case SONG_DETAILS_CODE:
                _id = db.insert(MusicDbContract.SongDetailsEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MusicDbContract.SongDetailsEntry.buildSongDetailsUri(_id);
                }
                break;
            case SYNC_DETAILS_CODE:
                _id = db.insert(MusicDbContract.SyncDetailsEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MusicDbContract.SyncDetailsEntry.buildSyncDetailsUri(_id);
                }
                break;
            case USER_SONG_PLAY_HISTORY_CODE:
                _id = db.insert(MusicDbContract.UserSongPlayHistoryEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MusicDbContract.UserSongPlayHistoryEntry.buildUserSongPlayHistoryUri(_id);
                }
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri_err_str) + uri);
        }
        if (_id <= 0) {
            throw new android.database.SQLException(getContext().getString(R.string.insertion_failed_err_str) + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = musicDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String tableName = null;
        switch (match) {
            case CATEGORIES_CODE:
                tableName = MusicDbContract.SongCategoriesEntry.TABLE_NAME;
                break;
            case SONG_DETAILS_CODE:
                tableName = MusicDbContract.SongDetailsEntry.TABLE_NAME;
                break;
            case SYNC_DETAILS_CODE:
                tableName = MusicDbContract.SyncDetailsEntry.TABLE_NAME;
                break;
            case USER_SONG_PLAY_HISTORY_CODE:
                tableName = MusicDbContract.UserSongPlayHistoryEntry.TABLE_NAME;
                break;
            default:
                return super.bulkInsert(uri, values);
        }

        int returnCount = 0;
        if (!TextUtils.isEmpty(tableName)) {
            db.beginTransaction();
            try {
                for (ContentValues value : values) {
                    long _id = db.insert(tableName, null, value);
                    if (_id != -1) {
                        returnCount++;
                    }
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return returnCount;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = musicDbHelper.getWritableDatabase();
        int rowsUpdated = 0;

        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case CATEGORIES_CODE:
                tableName = MusicDbContract.SongCategoriesEntry.TABLE_NAME;
                break;
            case SONG_DETAILS_CODE:
                tableName = MusicDbContract.SongDetailsEntry.TABLE_NAME;
                break;
            case SYNC_DETAILS_CODE:
                tableName = MusicDbContract.SyncDetailsEntry.TABLE_NAME;
                break;
            case USER_SONG_PLAY_HISTORY_CODE:
                tableName = MusicDbContract.UserSongPlayHistoryEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri_err_str) + uri);
        }

        if (!TextUtils.isEmpty(tableName)) {
            rowsUpdated = db.update(tableName, values, selection, selectionArgs);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = musicDbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        String tableName = null;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case CATEGORIES_CODE:
                tableName = MusicDbContract.SongCategoriesEntry.TABLE_NAME;
                break;
            case SONG_DETAILS_CODE:
                tableName = MusicDbContract.SongDetailsEntry.TABLE_NAME;
                break;
            case SYNC_DETAILS_CODE:
                tableName = MusicDbContract.SyncDetailsEntry.TABLE_NAME;
                break;
            case USER_SONG_PLAY_HISTORY_CODE:
                tableName = MusicDbContract.UserSongPlayHistoryEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri_err_str) + uri);
        }

        if (!TextUtils.isEmpty(tableName)) {
            rowsDeleted = db.delete(tableName, selection, selectionArgs);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }


    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MusicDbContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MusicDbContract.PATH_SONG_CATEGORIES, CATEGORIES_CODE);
        matcher.addURI(authority, MusicDbContract.PATH_SONG_DETAILS, SONG_DETAILS_CODE);
        matcher.addURI(authority, MusicDbContract.PATH_SONG_DETAILS + "/" +
                MusicDbContract.SongDetailsEntry.COLUMN_CATEGORY_KEY + "/*", SONG_DETAILS_WITH_CATEGORY_CODE);
        matcher.addURI(authority, MusicDbContract.PATH_SYNC_DETAILS, SYNC_DETAILS_CODE);
        matcher.addURI(authority, MusicDbContract.PATH_USER_SONG_PLAY_HISTORY, USER_SONG_PLAY_HISTORY_CODE);

        return matcher;
    }
}
