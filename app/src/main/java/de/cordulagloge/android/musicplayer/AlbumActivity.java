package de.cordulagloge.android.musicplayer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Comparator;

import de.cordulagloge.android.musicplayer.databinding.ActivityAlbumBinding;

public class AlbumActivity extends AppCompatActivity {

    ArrayList<Song> songArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ActivityAlbumBinding albumBinding = DataBindingUtil.setContentView(this, R.layout.activity_album);
        Bundle bundle = getIntent().getExtras();
        MyParcelable songObject = null;
        if (bundle != null) {
            songObject = bundle.getParcelable("songObject");
        }
        songArrayList = (ArrayList<Song>) songObject.getArrList();
        Album currentAlbum = songObject.getAlbum();
        // Set album header
        albumBinding.albumImage.setImageBitmap(currentAlbum.getAlbumImage(this));
        albumBinding.albumTextview.setText(currentAlbum.getAlbum());
        albumBinding.artistTextview.setText(currentAlbum.getArtist());
        // set songlist and sort by track number
        SongAdapter songAdapter = new SongAdapter(this, songArrayList);
        songAdapter.sort(new Comparator<Song>() {
            @Override
            public int compare(Song song, Song song2) {
                return Integer.valueOf(song.getId()).compareTo(song2.getId());
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

    /**
     * Start PlayActivity and send song Information
     *
     * @param songIndex index of selected song -> will be played first
     */
    private void startPlayActivity(int songIndex) {
        MyParcelable songObject = new MyParcelable();
        songObject.setArrList(songArrayList);
        songObject.setMyInt(songIndex);
        Intent playIntent = new Intent(this, PlayActivity.class);
        playIntent.putExtra("songObject", songObject);
        startActivity(playIntent);
    }
}
