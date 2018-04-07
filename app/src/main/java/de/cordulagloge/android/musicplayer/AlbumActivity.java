package de.cordulagloge.android.musicplayer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.cordulagloge.android.musicplayer.databinding.ActivityAlbumBinding;

public class AlbumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ActivityAlbumBinding albumBinding = DataBindingUtil.setContentView(this, R.layout.activity_album);
        Bundle b = getIntent().getExtras();
        MyParcelable songObject = null;
        if (b != null) {
            songObject = b.getParcelable("songObject");
        }
        ArrayList<Song> songArrayList = (ArrayList<Song>)songObject.getArrList();
        Album currentAlbum = songObject.getAlbum();

        albumBinding.albumImage.setImageBitmap(currentAlbum.getAlbumImage(this));
        albumBinding.albumTextview.setText(currentAlbum.getAlbum());
        albumBinding.artistTextview.setText(currentAlbum.getArtist());

        SongAdapter songAdapter = new SongAdapter(this, songArrayList);
        albumBinding.songList.setAdapter(songAdapter);
    }
}
