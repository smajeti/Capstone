package com.sai.nanodegree.capstone.data;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;

import com.sai.nanodegree.capstone.R;
import com.sai.nanodegree.capstone.SongDetailsFragment;

import java.util.Date;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class PlayHistoryUpdateService extends IntentService {

    private static final String TAG = PlayHistoryUpdateService.class.getSimpleName();

    public static final String ACTION_IN_UP_PLAY_HISTORY = "com.sai.nanodegree.capstone.data.action.IN_UP_PLAY_HISTORY";

    public static final String EXTRA_PARAM1 = "com.sai.nanodegree.capstone.data.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.sai.nanodegree.capstone.data.extra.PARAM2";

    public PlayHistoryUpdateService() {
        super("PlayHistoryUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_IN_UP_PLAY_HISTORY.equals(action)) {
                final long songId = intent.getLongExtra(SongDetailsFragment.SONG_ID_KEY, -1);
                final double watchedSofar = intent.getDoubleExtra(SongDetailsFragment.WATCHED_SO_FAR_KEY, 0.0);
                insertOrUpdateSongPlayHistory(songId, watchedSofar);
            }
        }
    }

    private void insertOrUpdateSongPlayHistory(long songId, double watchedSofar) {
        Cursor songPlayedCursor = null;
        try {
            songPlayedCursor = MusicDbUtils.getPlayHistoryCursor(getApplicationContext(), songId);
            Date now = new Date();
            MusicDbUtils.insertOrUpdatePlayHistory(getApplicationContext(), songPlayedCursor, songId, watchedSofar, now.getTime());
            Intent dataUpdatedIntent = new Intent(this.getString(R.string.widget_data_updated_action));
            this.sendBroadcast(dataUpdatedIntent);

        } finally {
            if (songPlayedCursor != null) {
                songPlayedCursor.close();
            }
        }
    }
}
