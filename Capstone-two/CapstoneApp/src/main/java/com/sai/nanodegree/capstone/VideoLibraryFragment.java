package com.sai.nanodegree.capstone;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sai.nanodegree.capstone.backend.songCategoryEndPointApi.model.SongCategoryBean;
import com.sai.nanodegree.capstone.data.MusicDbContract;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoLibraryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView musicLibRecyclerView;
    private MusicLibraryAdapter musicLibraryAdapter;
    private String categoryName;
    private String songCategoryId;
    private TextView emptyTxtView;

    public static final int SONG_DETAILS_LOADER = 0;
    public static final String CATEGORY_NAME_KEY = "CategoryName";

    public static final String[] SONG_DETAILS_WITH_CATEGORY_COLUMNS = new String[]{
            MusicDbContract.SongDetailsEntry.TABLE_NAME + "." + MusicDbContract.SongDetailsEntry._ID,
            MusicDbContract.SongDetailsEntry.TABLE_NAME + "." + MusicDbContract.SongDetailsEntry.COLUMN_AAROHANA,
            MusicDbContract.SongDetailsEntry.TABLE_NAME + "." + MusicDbContract.SongDetailsEntry.COLUMN_AVAROHANA,
            MusicDbContract.SongDetailsEntry.TABLE_NAME + "." + MusicDbContract.SongDetailsEntry.COLUMN_CATEGORY_KEY,
            MusicDbContract.SongDetailsEntry.TABLE_NAME + "." + MusicDbContract.SongDetailsEntry.COLUMN_DETAILS,
            MusicDbContract.SongDetailsEntry.TABLE_NAME + "." + MusicDbContract.SongDetailsEntry.COLUMN_DURATION,
            MusicDbContract.SongDetailsEntry.TABLE_NAME + "." + MusicDbContract.SongDetailsEntry.COLUMN_PICTURE_URL,
            MusicDbContract.SongDetailsEntry.TABLE_NAME + "." + MusicDbContract.SongDetailsEntry.COLUMN_VIDEO_URL,
            MusicDbContract.SongDetailsEntry.TABLE_NAME + "." + MusicDbContract.SongDetailsEntry.COLUMN_RAGAM,
            MusicDbContract.SongDetailsEntry.TABLE_NAME + "." + MusicDbContract.SongDetailsEntry.COLUMN_TITLE,
            MusicDbContract.SongDetailsEntry.TABLE_NAME + "." + MusicDbContract.SongDetailsEntry.COLUMN_LAST_UPDATED_DATE,
            MusicDbContract.UserSongPlayHistoryEntry.TABLE_NAME + "." + MusicDbContract.UserSongPlayHistoryEntry._ID,
            MusicDbContract.UserSongPlayHistoryEntry.TABLE_NAME + "." + MusicDbContract.UserSongPlayHistoryEntry.COLUMN_PLAYED_TIME,
            MusicDbContract.UserSongPlayHistoryEntry.TABLE_NAME + "." + MusicDbContract.UserSongPlayHistoryEntry.COLUMN_LAST_UPDATED_DATE
    };

    public static final int INDEX_SD_ID = 0;
    public static final int INDEX_COLUMN_AAROHANA = 1;
    public static final int INDEX_COLUMN_AVAROHANA = 2;
    public static final int INDEX_COLUMN_CATEGORY_KEY = 3;
    public static final int INDEX_COLUMN_DETAILS = 4;
    public static final int INDEX_COLUMN_DURATION = 5;
    public static final int INDEX_COLUMN_PICTURE_URL = 6;
    public static final int INDEX_COLUMN_VIDEO_URL = 7;
    public static final int INDEX_COLUMN_RAGAM = 8;
    public static final int INDEX_COLUMN_TITLE = 9;
    public static final int INDEX_SD_COLUMN_LAST_UPDATED_DATE = 10;
    public static final int INDEX_USPH_ID = 11;
    public static final int INDEX_COLUMN_PLAYED_TIME = 12;
    public static final int INDEX_USPH_COLUMN_LAST_UPDATED_DATE = 13;

    public interface VideoLibraryFragmentOnClickHandler {
        void onClick(Bundle bundle);
    }

    public VideoLibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if ((arguments != null) && arguments.containsKey(CATEGORY_NAME_KEY)) {
            categoryName = arguments.getString(CATEGORY_NAME_KEY);
            HashMap<String, SongCategoryBean> localDbCategoryMap = GlobalDataSingleton.getSingletonInstance().getLocalDbCategoryMap(getActivity());
            if ((localDbCategoryMap != null) && (localDbCategoryMap.containsKey(categoryName))) {
                songCategoryId = Long.toString(localDbCategoryMap.get(categoryName).getId());
            }
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_music_library, container, false);
        musicLibRecyclerView = (RecyclerView) rootView.findViewById(R.id.video_lib_recyler_view_id);
        musicLibRecyclerView.setLayoutManager(new GridLayoutManager(musicLibRecyclerView.getContext(), 2));
        musicLibraryAdapter = new MusicLibraryAdapter(new MusicLibraryAdapter.MusicLibraryAdapterOnClickHandler() {
            @Override
            public void onClick(Bundle bundle) {
                ((VideoLibraryFragmentOnClickHandler)getActivity()).onClick(bundle);
            }
        });
        musicLibRecyclerView.setAdapter(musicLibraryAdapter);

        emptyTxtView = (TextView) rootView.findViewById(R.id.empty_txt_view_id);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(SONG_DETAILS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String sortOrder = MusicDbContract.SongDetailsEntry.COLUMN_TITLE + " ASC";
        return new CursorLoader(getActivity(),
                MusicDbContract.SongDetailsEntry.buildSongDetailsWithCategory(songCategoryId),
                SONG_DETAILS_WITH_CATEGORY_COLUMNS, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        musicLibraryAdapter.swapCursor(data);
        updateEmptyView();
        // @todo This is not the perfect solution modify this using CursorLoader for Categories
        if (songCategoryId == null) {
            HashMap<String, SongCategoryBean> localDbCategoryMap = GlobalDataSingleton.getSingletonInstance().getLocalDbCategoryMap(getActivity());
            if ((localDbCategoryMap != null) && (localDbCategoryMap.containsKey(categoryName))) {
                songCategoryId = Long.toString(localDbCategoryMap.get(categoryName).getId());
            }

            if (songCategoryId != null) {
                getLoaderManager().restartLoader(SONG_DETAILS_LOADER, null, this);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        musicLibraryAdapter.swapCursor(null);
    }

    private void updateEmptyView() {
        if (musicLibraryAdapter.getItemCount() > 0) {
            emptyTxtView.setVisibility(View.INVISIBLE);
        } else {
            if (!Utility.isNetworkAvailable(getContext())) {
                emptyTxtView.setText(R.string.no_network_connectoin_msg);
            }
            else if (songCategoryId == null) {
                emptyTxtView.setText(R.string.device_fetching_data_from_cloud);
            } else {
                emptyTxtView.setText(R.string.unknown_error_msg);
            }

            emptyTxtView.setVisibility(View.VISIBLE);
        }
    }
}
