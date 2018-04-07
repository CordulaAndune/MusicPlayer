package de.cordulagloge.android.musicplayer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.cordulagloge.android.musicplayer.databinding.ActivityAlbumBinding;

public class AlbumActivity extends AppCompatActivity {

    ArrayList<Song> songArrayList;

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
        songArrayList = (ArrayList<Song>) songObject.getArrList();
        Album currentAlbum = songObject.getAlbum();

        albumBinding.albumImage.setImageBitmap(currentAlbum.getAlbumImage(this));
        albumBinding.albumTextview.setText(currentAlbum.getAlbum());
        albumBinding.artistTextview.setText(currentAlbum.getArtist());

        SongAdapter songAdapter = new SongAdapter(this, songArrayList);
        songAdapter.sort(new Comparator<Song>() {
            @Override
            public int compare(Song song, Song song2) {
                return song.getId().compareTo(song2.getId());
            }
        });

        albumBinding.songList.setAdapter(songAdapter);
        albumBinding.songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startPlayActivity(i);
            }
        });
    }

    private void startPlayActivity(int songIndex) {
        MyParcelable songObject = new MyParcelable();
        songObject.setArrList(songArrayList);
        songObject.setMyInt(songIndex);
        Intent playIntent = new Intent(this, PlayActivity.class);
        playIntent.putExtra("songObject", songObject);
        startActivity(playIntent);

    }
}
