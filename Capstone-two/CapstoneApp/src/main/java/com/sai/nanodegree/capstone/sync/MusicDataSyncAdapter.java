package com.sai.nanodegree.capstone.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.sai.nanodegree.capstone.GlobalDataSingleton;
import com.sai.nanodegree.capstone.R;
import com.sai.nanodegree.capstone.data.MusicDbUtils;

import java.util.Date;

/**
 * Created by smajeti on 5/13/16.
 */
public class MusicDataSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = MusicDataSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 10 = 10 mins
    public static final int SYNC_INTERVAL = 60 * 10;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public MusicDataSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public MusicDataSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {

        try {
            Date lastSyncedTime = MusicDbUtils.getLastSyncedTime(getContext());
            Date syncFromServerStartTime = new Date();
            MusicDbUtils.syncCategories(getContext(), lastSyncedTime);
            // cache the latest category map so that everyone can use it
            GlobalDataSingleton.getSingletonInstance().setLocalDbCategoryMap(MusicDbUtils.getLocalDbCategoryMap(getContext()));
            MusicDbUtils.syncSongDetails(getContext(), lastSyncedTime);
            MusicDbUtils.syncSongPlayHistory(getContext(), lastSyncedTime);
            MusicDbUtils.setLastSyncedTime(getContext(), syncFromServerStartTime, null);
            updateWidget();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Sync Failed " + e.getMessage());
        }
    }

    private void updateWidget() {
        Context context = getContext();
        Intent dataUpdatedIntent = new Intent(context.getString(R.string.widget_data_updated_action));
//                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(context.getString(R.string.app_name),
                context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }

        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        MusicDataSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

}
