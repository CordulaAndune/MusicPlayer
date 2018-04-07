package de.cordulagloge.android.musicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Cordula Gloge on 26/03/2018.
 */

public class SongAdapter extends ArrayAdapter<Song> {

    public SongAdapter(Context context, ArrayList<Song> songList) {
        super(context, 0, songList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Song currentSong = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.music_list_item, parent, false);
        }

        TextView titleText = convertView.findViewById(R.id.title_textview);
        titleText.setText(currentSong.getTitle());

        TextView albumText = convertView.findViewById(R.id.album_textview);
        albumText.setText(currentSong.getArtist() + " - " + currentSong.getAlbum());

        return convertView;
    }
}
