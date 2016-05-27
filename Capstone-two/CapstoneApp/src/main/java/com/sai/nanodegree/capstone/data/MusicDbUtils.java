package com.sai.nanodegree.capstone.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.sai.nanodegree.capstone.GlobalDataSingleton;
import com.sai.nanodegree.capstone.backend.songCategoryEndPointApi.model.SongCategoryBean;
import com.sai.nanodegree.capstone.backend.songDetailsEndPointApi.model.SongDetailsBean;
import com.sai.nanodegree.capstone.backend.userSongPlayHistoryEndPointApi.model.UserSongPlayHistoryBean;
import com.sai.nanodegree.capstone.cloud.SongCategoryEndpointUtils;
import com.sai.nanodegree.capstone.cloud.SongDetailsEndpointUtils;
import com.sai.nanodegree.capstone.cloud.UserSongPlayHistoryEndpointUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by smajeti on 5/17/16.
 */
public class MusicDbUtils {

    // **********************
    // Sync table related
    // **********************

    public static final String[] SYNC_DETAILS_PROJECTION = new String[]{
            MusicDbContract.SyncDetailsEntry._ID,
            MusicDbContract.SyncDetailsEntry.COLUMN_FROM_SERVER_TIME,
            MusicDbContract.SyncDetailsEntry.COLUMN_TO_SERVER_TIME
    };
    public static final int INDEX_ID = 0;
    public static final int INDEX_FROM_SERVER_TIME = 1;
    public static final int INDEX_TO_SERVER_TIME = 2;

    public static Date getLastSyncedTime(Context context) {

        Cursor syncDetailsCursor = null;
        long lastSyncTime = 0;
        try {
            syncDetailsCursor = getSyncTableCursor(context);
            if (syncDetailsCursor.moveToFirst()) {
                lastSyncTime = syncDetailsCursor.getLong(INDEX_FROM_SERVER_TIME);
            }
        } finally {
            if (syncDetailsCursor != null) {
                syncDetailsCursor.close();
            }
        }
        return new Date(lastSyncTime);
    }

    public static void setLastSyncedTime(Context context, Date fromServerSyncTime, Date toServerSyncTime) {
        String selectionStr = String.format("%s = ?", MusicDbContract.SyncDetailsEntry._ID);
        Cursor syncDetailsCursor = null;
        try {
            syncDetailsCursor = getSyncTableCursor(context);

            ContentValues syncValues = new ContentValues();
            if (fromServerSyncTime != null) {
                syncValues.put(MusicDbContract.SyncDetailsEntry.COLUMN_FROM_SERVER_TIME, fromServerSyncTime.getTime());
            }
            if (toServerSyncTime != null) {
                syncValues.put(MusicDbContract.SyncDetailsEntry.COLUMN_TO_SERVER_TIME, toServerSyncTime.getTime());
            }

            Date now = new Date();
            syncValues.put(MusicDbContract.SyncDetailsEntry.COLUMN_LAST_UPDATED_DATE, now.getTime());

            if (syncDetailsCursor.moveToFirst()) {
                // update
                long syncRowId = syncDetailsCursor.getLong(INDEX_ID);
                context.getContentResolver().update(MusicDbContract.SyncDetailsEntry.CONTENT_URI,
                        syncValues, selectionStr, new String[]{Long.toString(syncRowId)});
            } else {
                // insert
                syncValues.put(MusicDbContract.SyncDetailsEntry.COLUMN_CREATION_DATE, now.getTime());
                context.getContentResolver().insert(MusicDbContract.SyncDetailsEntry.CONTENT_URI,
                        syncValues);
            }
        } finally {
            if (syncDetailsCursor != null) {
                syncDetailsCursor.close();
            }
        }
    }

    public static Cursor getSyncTableCursor(Context context) {
        return context.getContentResolver().query(MusicDbContract.SyncDetailsEntry.CONTENT_URI, SYNC_DETAILS_PROJECTION, null, null, null);
    }

    // **********************
    // Categories Table related
    // **********************

    public static final String[] CATEGORY_PROJECTION = new String[]{
            MusicDbContract.SongCategoriesEntry._ID,
            MusicDbContract.SongCategoriesEntry.COLUMN_NAME,
            MusicDbContract.SongCategoriesEntry.COLUMN_LEVEL,
    };
    public static final int INDEX_CATEGORY_NAME = 1;
    public static final int INDEX_CATEGORY_LEVEL = 2;

    public static void syncCategories(Context context, Date after) {
        // get categories first
        SongCategoryEndpointUtils.EndpointsAsyncTask songCategoryAsyncTask = new SongCategoryEndpointUtils.EndpointsAsyncTask();
        songCategoryAsyncTask.setCommandType(SongCategoryEndpointUtils.CommandType.GET_CATEGORIES);
        List<SongCategoryBean> categoryBeanList = (List<SongCategoryBean>) songCategoryAsyncTask.doInForeground(context, after.getTime());
        insertOrUpdateCategoryList(context, categoryBeanList);
    }

    public static void insertOrUpdateCategoryList(Context context, List<SongCategoryBean> categoryBeanList) {
        if (categoryBeanList == null) {
            return;
        }

        String selectionStr = String.format("%s = ?", MusicDbContract.SongCategoriesEntry.COLUMN_NAME);
        for (SongCategoryBean songCategoryBean : categoryBeanList) {
            Cursor categoryCursor = null;
            try {
                // first check if this record exists in local DB
                categoryCursor = context.getContentResolver().query(MusicDbContract.SongCategoriesEntry.CONTENT_URI,
                        CATEGORY_PROJECTION, selectionStr, new String[]{songCategoryBean.getName()}, null);

                ContentValues categoryValues = new ContentValues();
                categoryValues.put(MusicDbContract.SongCategoriesEntry.COLUMN_NAME, songCategoryBean.getName());
                categoryValues.put(MusicDbContract.SongCategoriesEntry.COLUMN_LEVEL, songCategoryBean.getLevel());
                Date now = new Date();
                categoryValues.put(MusicDbContract.SongCategoriesEntry.COLUMN_LAST_UPDATED_DATE, now.getTime());

                if (categoryCursor.moveToFirst()) {
                    // this means update
                    context.getContentResolver().update(MusicDbContract.SongCategoriesEntry.CONTENT_URI,
                            categoryValues, selectionStr, new String[]{songCategoryBean.getName()});
                } else {
                    // this category does not exist insert it
                    categoryValues.put(MusicDbContract.SongCategoriesEntry.COLUMN_CREATION_DATE, now.getTime());
                    context.getContentResolver().insert(MusicDbContract.SongCategoriesEntry.CONTENT_URI,
                            categoryValues);
                }
            } finally {
                if (categoryCursor != null) {
                    categoryCursor.close();
                }
            }
        }
    }

    public static HashMap<String, SongCategoryBean> getLocalDbCategoryMap(Context context) {
        // Hasmap - <Category Name, SongCategoryBean> - ID inside SognCategoryBean is specific to local db
        Cursor categoryCursor = null;
        HashMap<String, SongCategoryBean> beanHashMap = new HashMap<String, SongCategoryBean>(20);
        try {
            // first check if this record exists in local DB
            categoryCursor = context.getContentResolver().
                    query(MusicDbContract.SongCategoriesEntry.CONTENT_URI, CATEGORY_PROJECTION, null, null, null);
            if (categoryCursor == null) {
                return beanHashMap;
            }
            while (categoryCursor.moveToNext()) {
                SongCategoryBean bean = new SongCategoryBean();
                bean.setId(categoryCursor.getLong(INDEX_ID));
                bean.setName(categoryCursor.getString(INDEX_CATEGORY_NAME));
                bean.setLevel(categoryCursor.getLong(INDEX_CATEGORY_LEVEL));
                beanHashMap.put(bean.getName(), bean);
            }

        } finally {
            if (categoryCursor != null) {
                categoryCursor.close();
            }
        }

        return beanHashMap;
    }

    // **********************
    // Song Details Table related
    // **********************

    public static final String[] SONG_DETAILS_PROJECTION = new String[]{
            MusicDbContract.SongDetailsEntry._ID,
            MusicDbContract.SongDetailsEntry.COLUMN_AAROHANA,
            MusicDbContract.SongDetailsEntry.COLUMN_AVAROHANA,
            MusicDbContract.SongDetailsEntry.COLUMN_CATEGORY_KEY,
            MusicDbContract.SongDetailsEntry.COLUMN_DETAILS,
            MusicDbContract.SongDetailsEntry.COLUMN_DURATION,
            MusicDbContract.SongDetailsEntry.COLUMN_PICTURE_URL,
            MusicDbContract.SongDetailsEntry.COLUMN_VIDEO_URL,
            MusicDbContract.SongDetailsEntry.COLUMN_RAGAM,
            MusicDbContract.SongDetailsEntry.COLUMN_TITLE
    };
    public static final int INDEX_AAROHANA = 1;
    public static final int INDEX_AVAROHANA = 2;
    public static final int INDEX_CATEGORY_KEY = 3;
    public static final int INDEX_DETAILS = 4;
    public static final int INDEX_DURATION = 5;
    public static final int INDEX_PICTURE_URL = 6;
    public static final int INDEX_VIDEO_URL = 7;
    public static final int INDEX_RAGAM = 8;
    public static final int INDEX_TITLE = 9;
    public static final String songIdSelectionStr = String.format("%s = ? AND %s = ? AND %s = ?",
            MusicDbContract.SongDetailsEntry.COLUMN_CATEGORY_KEY,
            MusicDbContract.SongDetailsEntry.COLUMN_TITLE,
            MusicDbContract.SongDetailsEntry.COLUMN_RAGAM);


    public static void syncSongDetails(Context context, Date after) throws Exception {
        // get song details
        SongDetailsEndpointUtils.EndpointsAsyncTask songDetailsAsyncTask = new SongDetailsEndpointUtils.EndpointsAsyncTask();
        songDetailsAsyncTask.setCommandType(SongDetailsEndpointUtils.CommandType.GET_ALL_SONG_DETAILS);
        List<SongDetailsBean> songDetailsBeanList = (List<SongDetailsBean>) songDetailsAsyncTask.doInForeground(context, after.getTime());
        insertOrUpdateSongDetailsList(context, songDetailsBeanList);
    }

    public static void insertOrUpdateSongDetailsList(Context context, List<SongDetailsBean> songDetailsBeanList) throws Exception {
        if (songDetailsBeanList == null) {
            return;
        }

        HashMap<String, SongCategoryBean> localDbCategoryMap = GlobalDataSingleton.getSingletonInstance().getLocalDbCategoryMap(context);

        for (SongDetailsBean songDetailsBean : songDetailsBeanList) {
            Cursor songDetailsCursor = null;
            try {
                songDetailsCursor = getSongDetailsCursor(context, songDetailsBean.getCategoryName(),
                                                        songDetailsBean.getTitle(),
                                                        songDetailsBean.getRagam(),
                                                        localDbCategoryMap);

                // first check if this record exists in local DB
                long categoryId = -1;
                if (localDbCategoryMap.containsKey(songDetailsBean.getCategoryName())) {
                    categoryId = localDbCategoryMap.get(songDetailsBean.getCategoryName()).getId();
                } else {
                    throw new Exception("Unable to insert Song Details as couldn't find category " + songDetailsBean.getCategoryName());
                }

                ContentValues songDetailsValues = new ContentValues();
                songDetailsValues.put(MusicDbContract.SongDetailsEntry.COLUMN_AAROHANA, songDetailsBean.getAarohana());
                songDetailsValues.put(MusicDbContract.SongDetailsEntry.COLUMN_AVAROHANA, songDetailsBean.getAvarohana());
                songDetailsValues.put(MusicDbContract.SongDetailsEntry.COLUMN_CATEGORY_KEY, categoryId);
                songDetailsValues.put(MusicDbContract.SongDetailsEntry.COLUMN_DETAILS, songDetailsBean.getDetails());
                songDetailsValues.put(MusicDbContract.SongDetailsEntry.COLUMN_DURATION, songDetailsBean.getDuration());
                songDetailsValues.put(MusicDbContract.SongDetailsEntry.COLUMN_PICTURE_URL, songDetailsBean.getPictureUrl());
                songDetailsValues.put(MusicDbContract.SongDetailsEntry.COLUMN_VIDEO_URL, songDetailsBean.getVideoUrl());
                songDetailsValues.put(MusicDbContract.SongDetailsEntry.COLUMN_RAGAM, songDetailsBean.getRagam());
                songDetailsValues.put(MusicDbContract.SongDetailsEntry.COLUMN_TITLE, songDetailsBean.getTitle());
                Date now = new Date();
                songDetailsValues.put(MusicDbContract.SongDetailsEntry.COLUMN_LAST_UPDATED_DATE, now.getTime());

                if (songDetailsCursor.getCount() > 0) {
                    // this means update
                    context.getContentResolver().update(MusicDbContract.SongDetailsEntry.CONTENT_URI,
                            songDetailsValues, songIdSelectionStr, new String[]{Long.toString(categoryId), songDetailsBean.getTitle(), songDetailsBean.getRagam()});
                } else {
                    // this category does not exist insert it
                    songDetailsValues.put(MusicDbContract.SongDetailsEntry.COLUMN_CREATION_DATE, now.getTime());
                    context.getContentResolver().insert(MusicDbContract.SongDetailsEntry.CONTENT_URI,
                            songDetailsValues);
                }
            } finally {
                if (songDetailsCursor != null) {
                    songDetailsCursor.close();
                }
            }
        }

    }

    public static Cursor getSongDetailsCursor(Context context, String categoryName, String songTitle, String raga,
                                              HashMap<String, SongCategoryBean> localDbCategoryMap) {
        // first check if this record exists in local DB
        long categoryId = -1;
        if (localDbCategoryMap.containsKey(categoryName)) {
            categoryId = localDbCategoryMap.get(categoryName).getId();
        }
        return context.getContentResolver().query(MusicDbContract.SongDetailsEntry.CONTENT_URI,
                SONG_DETAILS_PROJECTION, songIdSelectionStr, new String[]{Long.toString(categoryId),
                        songTitle, raga}, null);

    }

    public static long getSongId(Context context, String categoryName, String songTitle, String raga,
                                              HashMap<String, SongCategoryBean> localDbCategoryMap) {
        // first check if this record exists in local DB
        Cursor songDetailCursor = null;
        try {
            songDetailCursor = getSongDetailsCursor(context, categoryName, songTitle, raga, localDbCategoryMap);
            if ((songDetailCursor != null) && songDetailCursor.moveToNext()) {
                return songDetailCursor.getLong(INDEX_ID);
            }
        }
        finally {
            if (songDetailCursor != null) {
                songDetailCursor.close();
            }
        }

        return -1;
    }
    // ************************************************
    // User Song Playing History Details Table related
    // ************************************************

    public static final String[] SONG_PLAY_HISTORY_PROJECTION = new String[]{
            MusicDbContract.UserSongPlayHistoryEntry._ID,
            MusicDbContract.UserSongPlayHistoryEntry.COLUMN_PLAYED_TIME,
            MusicDbContract.UserSongPlayHistoryEntry.COLUMN_SONG_KEY,
            MusicDbContract.UserSongPlayHistoryEntry.COLUMN_LAST_UPDATED_DATE
    };

    public static final String playedHistorySelectionStr = String.format("%s = ?", MusicDbContract.UserSongPlayHistoryEntry.COLUMN_SONG_KEY);

    public static final int INDEX_PLAYED_TIME = 1;
    public static final int INDEX_SONG_KEY = 2;
    public static final int INDEX_USPH_LU_DATE = 3;

    public static void syncSongPlayHistory(Context context, Date after) {
        // get categories first
        UserSongPlayHistoryEndpointUtils.EndpointsAsyncTask songHistoryAsyncTask = new UserSongPlayHistoryEndpointUtils.EndpointsAsyncTask();
        songHistoryAsyncTask.setCommandType(UserSongPlayHistoryEndpointUtils.CommandType.GET_UPDATED_HISTORY);
        List<UserSongPlayHistoryBean> playHistoryBeanList = (List<UserSongPlayHistoryBean>)
                songHistoryAsyncTask.doInForegournd(context, after.getTime(),
                        GlobalDataSingleton.getSingletonInstance().getUserEmail(context));
        insertOrUpdatePlayHistoryList(context, playHistoryBeanList);
    }

    private static void insertOrUpdatePlayHistoryList(Context context, List<UserSongPlayHistoryBean> playHistoryBeanList) {
        if (playHistoryBeanList == null) {
            return;
        }

        HashMap<String, SongCategoryBean> localDbCategoryMap = GlobalDataSingleton.getSingletonInstance().getLocalDbCategoryMap(context);

        for (UserSongPlayHistoryBean playHistoryBean : playHistoryBeanList) {
            Cursor songPlayedCursor = null;
            try {
                // first check if this record exists in local DB
                long songId = getSongId(context, playHistoryBean.getCategoryName(),
                                                        playHistoryBean.getTitle(),
                                                        playHistoryBean.getRagam(),
                                                        localDbCategoryMap);

                songPlayedCursor = getPlayHistoryCursor(context, songId);
                insertOrUpdatePlayHistory(context, songPlayedCursor, songId, playHistoryBean.getPalyedTime(), playHistoryBean.getUpdatedDate().getValue());

            } finally {
                if (songPlayedCursor != null) {
                    songPlayedCursor.close();
                }
            }
        }
    }

    public static Cursor getPlayHistoryCursor(Context context, long songId) {
        return context.getContentResolver().query(MusicDbContract.UserSongPlayHistoryEntry.CONTENT_URI,
                SONG_PLAY_HISTORY_PROJECTION, playedHistorySelectionStr,
                new String[]{Long.toString(songId)}, null);

    }

    public static void insertOrUpdatePlayHistory(Context context, Cursor songPlayedCursor, long songId, Double watchedSofar, Long lastUpdateTime) {
        ContentValues categoryValues = new ContentValues();
        categoryValues.put(MusicDbContract.UserSongPlayHistoryEntry.COLUMN_SONG_KEY, songId);
        categoryValues.put(MusicDbContract.UserSongPlayHistoryEntry.COLUMN_PLAYED_TIME, watchedSofar);
        Date now = new Date();
        categoryValues.put(MusicDbContract.UserSongPlayHistoryEntry.COLUMN_LAST_UPDATED_DATE, now.getTime());

        if (songPlayedCursor.moveToFirst() && (songPlayedCursor.getLong(INDEX_USPH_LU_DATE) < lastUpdateTime)) {
            // this means update
            context.getContentResolver().update(MusicDbContract.UserSongPlayHistoryEntry.CONTENT_URI,
                    categoryValues, playedHistorySelectionStr, new String[]{Long.toString(songId)});
        } else {
            // this category does not exist insert it
            categoryValues.put(MusicDbContract.UserSongPlayHistoryEntry.COLUMN_CREATION_DATE, now.getTime());
            context.getContentResolver().insert(MusicDbContract.UserSongPlayHistoryEntry.CONTENT_URI,
                    categoryValues);
        }
    }
}
