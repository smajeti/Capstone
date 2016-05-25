package com.sai.nanodegree.capstone.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.sai.nanodegree.capstone.R;
import com.sai.nanodegree.capstone.SongDetailsFragment;
import com.sai.nanodegree.capstone.VideoPlayActivity;
import com.sai.nanodegree.capstone.data.MusicDbContract;
import com.sai.nanodegree.capstone.data.MusicDbUtils;

import java.util.concurrent.ExecutionException;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class WidgetUpdateService extends IntentService {

    private static final String TAG = WidgetUpdateService.class.getSimpleName();

    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, CapstoneWidgetProvider.class));

        Cursor latestSongPlayedCursor = null;
        Cursor songDetailsCursor = null;
        double watchedSofar = 0.0;
        double duration = 0.0;
        String pictureUrl = "";
        String videoUrl = "";
        String title = "";
        long updatedTime = -1;
        long songId = -1;
        try {
            latestSongPlayedCursor = getContentResolver().query(MusicDbContract.UserSongPlayHistoryEntry.CONTENT_URI,
                    MusicDbUtils.SONG_PLAY_HISTORY_PROJECTION, null, null,
                    MusicDbContract.UserSongPlayHistoryEntry.COLUMN_LAST_UPDATED_DATE + " DESC");

            if (latestSongPlayedCursor.moveToFirst()) {
                // pick the last played song
                watchedSofar = latestSongPlayedCursor.getDouble(MusicDbUtils.INDEX_PLAYED_TIME);
                songId = latestSongPlayedCursor.getLong(MusicDbUtils.INDEX_SONG_KEY);
                updatedTime = latestSongPlayedCursor.getLong(MusicDbUtils.INDEX_USPH_LU_DATE);
            }

            if (songId != -1) {
                // this means we  have latest song
                final String selectionStr = String.format("%s = ?", MusicDbContract.SongDetailsEntry._ID);
                songDetailsCursor = getContentResolver().query(MusicDbContract.SongDetailsEntry.CONTENT_URI,
                                    MusicDbUtils.SONG_DETAILS_PROJECTION, selectionStr,
                                    new String[] {Long.toString(songId)}, null);
                if (songDetailsCursor.moveToFirst()) {
                    duration = songDetailsCursor.getDouble(MusicDbUtils.INDEX_DURATION);
                    pictureUrl = songDetailsCursor.getString(MusicDbUtils.INDEX_PICTURE_URL);
                    videoUrl = songDetailsCursor.getString(MusicDbUtils.INDEX_VIDEO_URL);
                    title = songDetailsCursor.getString(MusicDbUtils.INDEX_TITLE);
                }
            }

        } finally {
            if (latestSongPlayedCursor != null) {
                latestSongPlayedCursor.close();
            }

            if (songDetailsCursor != null) {
                songDetailsCursor.close();
            }
        }

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.capstone_widget);

            if (songId != -1) {
                Bitmap widgetImage = null;
                try {
                    widgetImage = Glide.with(this)
                            .load(Uri.parse(pictureUrl))
                            .asBitmap()
                            .override(300, 250)
                            .centerCrop()
                            .into(300, 250).get();
                    views.setImageViewBitmap(R.id.widget_img_view_id, widgetImage);
                    views.setTextViewText(R.id.widget_song_name_txt_view_id, title);
                    views.setTextViewText(R.id.widget_song_view_percent_txt_view_id, (int)((watchedSofar * 100.0) / duration) + "%");

                    // Create an Intent to launch VideoPlayActivity
                    Intent launchIntent = new Intent(this, VideoPlayActivity.class);
                    launchIntent.putExtra(SongDetailsFragment.VIDEO_URL_KEY, videoUrl);
                    launchIntent.putExtra(SongDetailsFragment.SONG_ID_KEY, songId);
                    launchIntent.putExtra(SongDetailsFragment.WATCHED_SO_FAR_KEY, new Double(watchedSofar));
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(R.id.widget_layout_id, pendingIntent);

                    // Tell the AppWidgetManager to perform an update on the current app widget
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                } catch (InterruptedException | ExecutionException e) {
                    Log.e(TAG, "Error retrieving widget image " + pictureUrl, e);
                }
            }
        }
    }
}
