package com.sai.nanodegree.capstone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.sai.nanodegree.capstone.sync.MusicDataSyncAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener, VideoLibraryFragment.VideoLibraryFragmentOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private NavigationView navigationView;
    private boolean twoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.song_description_container_id) != null) {
            twoPaneMode = true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            MenuItem menuItem = navigationView.getMenu().findItem(GlobalDataSingleton.getSingletonInstance().getLastSelectedDrawerItem(this));
            if (menuItem == null) {
                menuItem = navigationView.getMenu().getItem(0);
            }

            menuItem.setChecked(true);
            navigationView.setNavigationItemSelectedListener(this);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestServerAuthCode(this.getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        MusicDataSyncAdapter.initializeSyncAdapter(this);

        if (savedInstanceState == null) {
            int itemId = GlobalDataSingleton.getSingletonInstance().getLastSelectedDrawerItem(this);
            MenuItem menuItem = navigationView.getMenu().findItem(itemId);
            if (menuItem == null) {
                menuItem = navigationView.getMenu().getItem(0);
            }
            replaceSongListFragment(menuItem.getTitle().toString(), itemId);
        }

        View headerView = navigationView.getHeaderView(0);
        ImageView userProfileImgView = (ImageView) headerView.findViewById(R.id.user_profile_img_view_id);
        TextView userEmailView = (TextView) headerView.findViewById(R.id.user_email_txt_id);
        TextView userNameView = (TextView) headerView.findViewById(R.id.user_name_txt_id);
        GoogleSignInAccount userAcctInfo = GlobalDataSingleton.getSingletonInstance().getUserAcctInfo();
        if (userAcctInfo != null) {
            userEmailView.setText(userAcctInfo.getEmail());
            userNameView.setText(userAcctInfo.getDisplayName());
            Uri uri = userAcctInfo.getPhotoUrl();
            if (uri != null) {
                Glide.with(this)
                        .load(uri)
                        .centerCrop()
                        .into(userProfileImgView);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_signout) {
            signOut();
        } else {
            // should be one of category items
            replaceSongListFragment(item.getTitle().toString(), item.getItemId());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(Bundle bundle) {
        if (twoPaneMode) {
            replaceSongDetailsFragment(bundle);
        } else {
            Intent intent = new Intent(this, SongDetailsActivity.class);
            intent.putExtra(SongDetailsActivity.SONG_DETAILS_BUNDLE_KEY, bundle);
            this.startActivity(intent);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (twoPaneMode && (GlobalDataSingleton.getSingletonInstance().getWatchedSofar() != null)) {
            SongDetailsFragment fragment = (SongDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.song_description_container_id);
            fragment.updateWatchedSofarTime(GlobalDataSingleton.getSingletonInstance().getWatchedSofar(),
                    GlobalDataSingleton.getSingletonInstance().getLastWatchedTime().getTime());
            GlobalDataSingleton.getSingletonInstance().setWatchedSofar(null);
            GlobalDataSingleton.getSingletonInstance().setLastWatchedTime(null);
        }
    }

    private void replaceSongListFragment(String categoryName, int itemId) {
        VideoLibraryFragment fragment = new VideoLibraryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VideoLibraryFragment.CATEGORY_NAME_KEY, categoryName);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.song_list_fragment_container_id, fragment).commit();
        GlobalDataSingleton.getSingletonInstance().setLastSelectedDrawerItem(this, itemId);
    }

    private void replaceSongDetailsFragment(Bundle bundle) {
        SongDetailsFragment fragment = new SongDetailsFragment();
        GlobalDataSingleton.getSingletonInstance().setWatchedSofar(null);
        GlobalDataSingleton.getSingletonInstance().setLastWatchedTime(null);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.song_description_container_id, fragment).commit();
    }

    private void signOut() {
        if (!mGoogleApiClient.isConnected()) {
            return;
        }
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainActivity.this.startActivity(intent);
                        finish();

                    }
                });
    }

}
