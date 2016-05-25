package com.sai.nanodegree.capstone;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongDetailsFragment extends Fragment {

    public static final String TITLE_KEY = "title_key";
    public static final String IMAGE_URL_KEY = "image_url_key";
    public static final String VIDEO_URL_KEY = "video_url_key";
    public static final String RAGAM_KEY = "ragamkey";
    public static final String AAROHANA_KEY = "aarohana_key";
    public static final String AVAROHANA_KEY = "avarohana_key";
    public static final String LAST_VIEWED_KEY = "last_viewed_key";
    public static final String NOTES_KEY = "notes_key";
    public static final String WATCHED_SO_FAR_KEY = "watched_so_far_key";
    public static final String TOTAL_DURATION_KEY = "total_duration_key";
    public static final String SONG_ID_KEY = "song_id_key";

    private double watchedSofar;
    private double totalDuration;
    private TextView lastAccessedTxtView;

    public SongDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_song_description, container, false);

        Bundle arguments = getArguments();
        if (arguments == null) {
            return rootView;
        }

        TextView txtView = (TextView) rootView.findViewById(R.id.song_title_txt_view_id);
        txtView.setText(arguments.getString(SongDetailsFragment.TITLE_KEY));

        txtView = (TextView) rootView.findViewById(R.id.song_ragam_name_txt_view_id);
        txtView.setText(arguments.getString(SongDetailsFragment.RAGAM_KEY));

        txtView = (TextView) rootView.findViewById(R.id.ragam_aarohana_name_txt_view_id);
        txtView.setText(arguments.getString(SongDetailsFragment.AAROHANA_KEY));

        txtView = (TextView) rootView.findViewById(R.id.ragam_ava_rohana_name_txt_view_id);
        txtView.setText(arguments.getString(SongDetailsFragment.AVAROHANA_KEY));

        lastAccessedTxtView = (TextView) rootView.findViewById(R.id.last_accessed_txt_view_id);
        final long lastViewedTime = arguments.getLong(SongDetailsFragment.LAST_VIEWED_KEY);
        final double watchedSofar = arguments.getDouble(SongDetailsFragment.WATCHED_SO_FAR_KEY);
        totalDuration = arguments.getDouble(SongDetailsFragment.TOTAL_DURATION_KEY);
        updateWatchedSofarTime(watchedSofar, lastViewedTime);

        txtView = (TextView) rootView.findViewById(R.id.notes_txt_view_id);
        txtView.setText(arguments.getString(SongDetailsFragment.NOTES_KEY));

        ImageView imgView = (ImageView) rootView.findViewById(R.id.song_img_view_id);
        String imgUrl = arguments.getString(SongDetailsFragment.IMAGE_URL_KEY);
        Glide.with(getActivity())
                .load(Uri.parse(imgUrl))
                .centerCrop()
                .into(imgView);

        final long songId = arguments.getLong(SongDetailsFragment.SONG_ID_KEY);
        final String videoUrl = arguments.getString(SongDetailsFragment.VIDEO_URL_KEY);
        ImageButton imgBtn = (ImageButton) rootView.findViewById(R.id.video_play_img_btn_id);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // play video
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                intent.putExtra(SongDetailsFragment.VIDEO_URL_KEY, videoUrl);
                intent.putExtra(SongDetailsFragment.SONG_ID_KEY, songId);
                intent.putExtra(SongDetailsFragment.WATCHED_SO_FAR_KEY, watchedSofar);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

    public void updateWatchedSofarTime(double newWatchedSofar, long lastWatchedTime) {
        watchedSofar = newWatchedSofar;
        String lastViewedDate = "n/a";
        if (lastWatchedTime != 0) {
            DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
            lastViewedDate = dateFormat.format(new Date(lastWatchedTime));
        }

        lastAccessedTxtView.setText(lastViewedDate + "\nViewed " +
                String.format("%.2f", watchedSofar) + " of " + totalDuration + " (mins)");

    }

}
