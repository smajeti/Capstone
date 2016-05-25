package com.sai.nanodegree.capstone;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by smajeti on 5/21/16.
 */
public class MusicLibraryAdapter extends RecyclerView.Adapter<MusicLibraryAdapter.ViewHolder> {

    private Cursor songDataCursor;
    private MusicLibraryAdapterOnClickHandler clickHandler;

    public interface MusicLibraryAdapterOnClickHandler {
        void onClick(Bundle bundle);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View itemView;
        public final View parentLayout;
        public final TextView songNameTxtView;
        public final TextView watchedPercentTxtView;
        public final ImageView songImgView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.parentLayout = itemView.findViewById(R.id.video_lib_recyler_item_layout);
            this.songNameTxtView = (TextView) itemView.findViewById(R.id.song_name_txt_view_id);
            this.watchedPercentTxtView = (TextView) itemView.findViewById(R.id.song_view_percent_txt_view_id);
            this.songImgView = (ImageView) itemView.findViewById(R.id.song_img_view_id);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            songDataCursor.moveToPosition(adapterPosition);
            Bundle bundle = getCurrentItemData();
            clickHandler.onClick(bundle);
        }

        private Bundle getCurrentItemData() {
            String songTitle = songDataCursor.getString(VideoLibraryFragment.INDEX_COLUMN_TITLE);
            double totalSongDuration = songDataCursor.getDouble(VideoLibraryFragment.INDEX_COLUMN_DURATION);
            double watchedSoFar = songDataCursor.getDouble(VideoLibraryFragment.INDEX_COLUMN_PLAYED_TIME);
            String imgUrl = songDataCursor.getString(VideoLibraryFragment.INDEX_COLUMN_PICTURE_URL);
            String videoUrl = songDataCursor.getString(VideoLibraryFragment.INDEX_COLUMN_VIDEO_URL);
            String ragam = songDataCursor.getString(VideoLibraryFragment.INDEX_COLUMN_RAGAM);
            String aarohana = songDataCursor.getString(VideoLibraryFragment.INDEX_COLUMN_AAROHANA);
            String avarohana = songDataCursor.getString(VideoLibraryFragment.INDEX_COLUMN_AVAROHANA);
            long lastViewedDate = songDataCursor.getLong(VideoLibraryFragment.INDEX_USPH_COLUMN_LAST_UPDATED_DATE);
            String notes = songDataCursor.getString(VideoLibraryFragment.INDEX_COLUMN_DETAILS);
            long songId = songDataCursor.getLong(VideoLibraryFragment.INDEX_SD_ID);

            Bundle bundle = new Bundle();
            bundle.putString(SongDetailsFragment.TITLE_KEY , songTitle);
            bundle.putString(SongDetailsFragment.IMAGE_URL_KEY , imgUrl);
            bundle.putString(SongDetailsFragment.VIDEO_URL_KEY , videoUrl);
            bundle.putString(SongDetailsFragment.RAGAM_KEY , ragam);
            bundle.putString(SongDetailsFragment.AAROHANA_KEY , aarohana);
            bundle.putString(SongDetailsFragment.AVAROHANA_KEY , avarohana);
            bundle.putLong(SongDetailsFragment.LAST_VIEWED_KEY , lastViewedDate);
            bundle.putString(SongDetailsFragment.NOTES_KEY , notes);
            bundle.putDouble(SongDetailsFragment.WATCHED_SO_FAR_KEY , watchedSoFar);
            bundle.putDouble(SongDetailsFragment.TOTAL_DURATION_KEY , totalSongDuration);
            bundle.putLong(SongDetailsFragment.SONG_ID_KEY, songId);

            return bundle;
        }
    }

    public MusicLibraryAdapter(MusicLibraryAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_lib_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicLibraryAdapter.ViewHolder holder, int position) {
        songDataCursor.moveToPosition(position);

        String songTitle = songDataCursor.getString(VideoLibraryFragment.INDEX_COLUMN_TITLE);
        double totalSongDuration = songDataCursor.getDouble(VideoLibraryFragment.INDEX_COLUMN_DURATION);
        double watchedSoFar = songDataCursor.getDouble(VideoLibraryFragment.INDEX_COLUMN_PLAYED_TIME);
        String imgUrl = songDataCursor.getString(VideoLibraryFragment.INDEX_COLUMN_PICTURE_URL);

        holder.songNameTxtView.setText(songTitle);
        int watchedPercent = (int) ((watchedSoFar * 100.0) / totalSongDuration);
        holder.watchedPercentTxtView.setText(Integer.toString(watchedPercent) + "%");
        Context ctx = holder.parentLayout.getContext();

        Glide.with(ctx)
                .load(Uri.parse(imgUrl))
                .centerCrop()
                .into(holder.songImgView);
    }

    @Override
    public int getItemCount() {
        //return libItemsList.size();
        if (songDataCursor == null) {
            return 0;
        }
        return songDataCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        songDataCursor = newCursor;
        notifyDataSetChanged();
        // @todo mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public Cursor getCursor() {
        return songDataCursor;
    }

}
