package de.cordulagloge.android.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Comparator;

import de.cordulagloge.android.musicplayer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
        }

        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        final ArrayList<Song> songList = new ArrayList<>();

        // Get Cursor
        Cursor cursor = SongManager.populateQueries(this);
        final ArrayList<Album> albumList = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                Song newSong = new Song(cursor, this);
                String currentAlbum = newSong.getAlbum();
                songList.add(newSong);
                int indexOfAlbum = containsAlbum(albumList, currentAlbum, newSong.getFilePath(), newSong.getArtist());
                if (0 <= indexOfAlbum) {
                    albumList.get(indexOfAlbum).addSong(songList.indexOf(newSong));
                } else {
                    Album newAlbum = new Album(currentAlbum, newSong.getArtist(),
                            songList.indexOf(newSong), newSong.getFilePath());
                    albumList.add(newAlbum);
                }
            }
        }


        AlbumAdapter albumAdapter = new AlbumAdapter(this, albumList);
        // sort Album
        albumAdapter.sort(new Comparator<Album>() {
            @Override
            public int compare(Album album, Album album2) {
                return album.getAlbum().compareTo(album2.getAlbum());
            }
        });

        mainBinding.musicList.setAdapter(albumAdapter);
        mainBinding.musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyParcelable songObject = new MyParcelable();
                ArrayList<Integer> indexSongsArray = albumList.get(i).getSongIndices();
                ArrayList<Song> albumSongs = new ArrayList<>();
                for (Integer songIndex: indexSongsArray) {
                    albumSongs.add(songList.get(songIndex));
                }
                songObject.setArrList(albumSongs);
                songObject.setMyInt(i);
                Intent playIntent = new Intent(MainActivity.this, PlayActivity.class);
                playIntent.putExtra("parcel", songObject);
                startActivity(playIntent);
            }
        });
    }

    /**
     * Check if album is already in ArrayList: same Albumname and same artist or same folder
     *
     * @param albumList ArrayList<album> with all already found albums
     * @param newAlbum  String of albumname which should be checked for
     * @return album is in ArrayList
     */
    public int containsAlbum(ArrayList<Album> albumList, String newAlbum, String filepath, String artist) {
        for (Album currentAlbum : albumList) {
            if (currentAlbum.getAlbum().equals(newAlbum)) {
                int indexFolderName = filepath.lastIndexOf("/");
                if (currentAlbum.getFilePath().startsWith((String) filepath.subSequence(0, indexFolderName))
                        || currentAlbum.getArtist().equals(artist)) {
                    if (!currentAlbum.getArtist().equals(artist)) {
                        currentAlbum.setArtist("Various Artists");
                    }
                    return albumList.indexOf(currentAlbum);
                }
            }
        }
        return -1;
    }
}
