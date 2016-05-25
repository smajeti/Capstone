package com.sai.nanodegree.capstone.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by smajeti on 5/16/16.
 */
public class MusicDataSyncService extends Service {

    private static final Object syncAdapterLock = new Object();
    private static MusicDataSyncAdapter musicDataSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (syncAdapterLock) {
            if (musicDataSyncAdapter == null) {
                musicDataSyncAdapter = new MusicDataSyncAdapter(getApplicationContext(), true);
            }

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicDataSyncAdapter.getSyncAdapterBinder();
    }
}
