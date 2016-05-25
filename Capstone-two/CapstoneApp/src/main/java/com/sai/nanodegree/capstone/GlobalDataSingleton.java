package com.sai.nanodegree.capstone;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.sai.nanodegree.capstone.backend.songCategoryEndPointApi.model.SongCategoryBean;
import com.sai.nanodegree.capstone.backend.userEndPointApi.model.UserBean;
import com.sai.nanodegree.capstone.data.MusicDbUtils;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by smajeti on 5/10/16.
 */
public class GlobalDataSingleton {

    private GoogleSignInAccount userAcctInfo;

    private UserBean userDetails;
    private HashMap<String, SongCategoryBean> localDbCategoryMap;
    private Object categoryLock = new Object();
    private Double watchedSofar;

    private static GlobalDataSingleton singletonInstance;
    private Date lastWatchedTime;

    public static GlobalDataSingleton getSingletonInstance() {
        if (singletonInstance == null) {
            singletonInstance = new GlobalDataSingleton();
        }

        return singletonInstance;
    }

    private GlobalDataSingleton() {
    }

    public GoogleSignInAccount getUserAcctInfo() {
        return userAcctInfo;
    }

    public void setUserAcctInfo(Context context, GoogleSignInAccount acctInfo) {
        this.userAcctInfo = acctInfo;

        // save user info to shared preferences, we need email ID for auto sync
        SharedPreferences sharedPref = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.user_email_id_key), acctInfo.getEmail());
        editor.commit();
    }

    public String getUserEmail(Context context) {
        if (userAcctInfo != null) {
            return userAcctInfo.getEmail();
        }

        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(context.getString(R.string.user_email_id_key), "");
    }

    public SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.shared_pref_file_key), Context.MODE_PRIVATE);
    }

    public UserBean getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserBean userDetails) {
        this.userDetails = userDetails;
    }

    public int getLastSelectedDrawerItem(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getInt(context.getString(R.string.last_selected_drawer_item_id_key), R.id.nav_sarali_varse);
    }

    public void setLastSelectedDrawerItem(Context context, int itemId) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(context.getString(R.string.last_selected_drawer_item_id_key), itemId);
        editor.commit();
    }

    public HashMap<String, SongCategoryBean> getLocalDbCategoryMap(Context context) {
        synchronized (categoryLock) {
            if (localDbCategoryMap == null) {
                localDbCategoryMap = MusicDbUtils.getLocalDbCategoryMap(context);
            }
            return localDbCategoryMap;
        }
    }

    public void setLocalDbCategoryMap(HashMap<String, SongCategoryBean> localDbCategoryMap) {
        synchronized (categoryLock) {
            this.localDbCategoryMap = localDbCategoryMap;
        }
    }

    public Double getWatchedSofar() {
        return watchedSofar;
    }

    public void setWatchedSofar(Double watchedSofar) {
        this.watchedSofar = watchedSofar;
    }

    public Date getLastWatchedTime() {
        return this.lastWatchedTime;
    }

    public void setLastWatchedTime(Date lastWatchedTime) {
        this.lastWatchedTime = lastWatchedTime;
    }
}
