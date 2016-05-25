package com.sai.nanodegree.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SongDetailsActivity extends AppCompatActivity {

    public static final String SONG_DETAILS_BUNDLE_KEY = "SongDetailsBunbleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_details);
        Intent intent = getIntent();
        if ((intent != null) && (savedInstanceState == null)) {
            replaceFragment(intent.getBundleExtra(SONG_DETAILS_BUNDLE_KEY));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GlobalDataSingleton.getSingletonInstance().getWatchedSofar() != null) {
            SongDetailsFragment fragment = (SongDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.song_description_container_id);
            fragment.updateWatchedSofarTime(GlobalDataSingleton.getSingletonInstance().getWatchedSofar(),
                                            GlobalDataSingleton.getSingletonInstance().getLastWatchedTime().getTime());
            GlobalDataSingleton.getSingletonInstance().setWatchedSofar(null);
            GlobalDataSingleton.getSingletonInstance().setLastWatchedTime(null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void replaceFragment(Bundle bundle) {
        SongDetailsFragment fragment = new SongDetailsFragment();
        GlobalDataSingleton.getSingletonInstance().setWatchedSofar(null);
        GlobalDataSingleton.getSingletonInstance().setLastWatchedTime(null);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.song_description_container_id, fragment).commit();
    }

}
