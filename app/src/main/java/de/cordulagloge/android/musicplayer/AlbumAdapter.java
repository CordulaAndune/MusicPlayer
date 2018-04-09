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

public class AlbumAdapter extends ArrayAdapter<Album> {

    public AlbumAdapter(Context context, ArrayList<Album> albumList) {
        super(context, 0, albumList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Album currentAlbum = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.music_list_item, parent, false);
        }
        ImageView albumImage = convertView.findViewById(R.id.album_image);
        albumImage.setImageBitmap(currentAlbum.getAlbumImage(getContext()));
        TextView albumText = convertView.findViewById(R.id.title_textview);
        albumText.setText(currentAlbum.getAlbum());
        TextView artistText = convertView.findViewById(R.id.album_textview);
        artistText.setText(currentAlbum.getArtist());
        return convertView;
    }
}
