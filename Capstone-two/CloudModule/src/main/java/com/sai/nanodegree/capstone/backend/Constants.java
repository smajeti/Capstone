package com.sai.nanodegree.capstone.backend;

/**
 * Created by smajeti on 5/3/16.
 */
public class Constants {

    public static final String WEB_CLIENT_ID = "103513729189-gdi54knimu2uopn1o43felb31omuose3.apps.googleusercontent.com";
    public static final String ANDROID_CLIENT_ID = "103513729189-t42pfsjkb8ch32r21dq6qub4rm0ebocr.apps.googleusercontent.com";
    public static final String IOS_CLIENT_ID = "103513729189-2kniroitccojvas1t4bkv3iparbfgjkv.apps.googleusercontent.com";
    public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;

    public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";

    // DB Related Constants

    public static final String DB_NAME_SPACE = "capstone_ds";
    public static final String DB_KIND_SONG_CATEGORIES = "SongCategories";
    public static final String DB_KIND_SONG_DETAILS = "SongDetails";
    public static final String DB_KIND_SYNC_DETAILS = "SyncDetails";
    public static final String DB_KIND_USER_DETAILS = "UserDetails";
    public static final String DB_KIND_USER_SONG_PLAY_HISTORY = "UserSongPlayHistory";

    // Generic Properties
    public static final String DB_CREATION_DATE_PROP = "creationDate";
    public static final String DB_LAST_UPDATED_DATE_PROP = "lastUpdatedDate";
    public static final String DB_KEY_PROP = "__KEY__";

    // SongCategories - Properties
    public static final String DB_SC_NAME_PROP = "name";
    public static final String DB_SC_LEVEL_PROP = "level";

    //SongDetails - Properties
    public static final String DB_SD_AAROHANA_PROP = "aarohana";
    public static final String DB_SD_AVAROHANA_PROP = "avarohana";
    public static final String DB_SD_CATEGORY_PROP = "category";
    public static final String DB_SD_DETAILS_PROP = "details";
    public static final String DB_SD_DURATION_PROP = "duration";
    public static final String DB_SD_PICTURE_URL_PROP = "pictureUrl";
    public static final String DB_SD_VIDEO_URL_PROP = "videoUrl";
    public static final String DB_SD_RAGAM_PROP = "ragam";
    public static final String DB_SD_TITLE_PROP = "title";

    // SyncDetails - Properties
    public static final String DB_SYD_SYNC_FROM_SERVER_TIME_PROP = "syncFromServerTime";
    public static final String DB_SYD_SYNC_TO_SERVER_TIME_PROP = "syncToServerTime";
    public static final String DB_SYD_SYNC_SUCCESS_PROP = "syncSuccess";
    public static final String DB_SYD_USER_ID_PROP = "userId";

    // UserDetails - Properties
    public static final String DB_UD_EMAIL_PROP = "email";
    public static final String DB_UD_DISPLAY_NAME_PROP = "displayName";
    public static final String DB_UD_SUBSCRIPTION_PAID_PROP = "subscriptionPaid";
    public static final String DB_UD_SUBSCRIPTION_PAID_DATE_PROP = "subscriptionPaidDate";

    // UserSongPlayHistory - Properties
    public static final String DB_USPH_PLAYED_TIME_PROP = "playedTime";
    public static final String DB_USPH_SONG_ID_PROP = "songId";
    public static final String DB_USPH_USER_ID_PROP = "userId";

}
