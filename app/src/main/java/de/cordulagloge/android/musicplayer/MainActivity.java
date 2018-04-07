package de.cordulagloge.android.musicplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import de.cordulagloge.android.musicplayer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    ArrayList<Album> albumList;
    ArrayList<Song> songList;
    int switcherIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        switcherIndex = 0;
        songList = new ArrayList<>();

        // Get Cursor
        Cursor cursor = SongManager.populateQueries(this);
        albumList = new ArrayList<>();
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
        if (albumList.size() == 0) {
            mainBinding.albumSwitcher.showNext();
            switcherIndex = 1;
        } else {
            AlbumAdapter albumAdapter = new AlbumAdapter(this, albumList);
            // sort Album
            albumAdapter.sort(new Comparator<Album>() {
                @Override
                public int compare(Album album, Album album2) {
                    return album.getAlbum().compareTo(album2.getAlbum());
                }
            });
            LayoutInflater headerInflater = getLayoutInflater();
            View headerView = headerInflater.inflate(R.layout.listview_header, mainBinding.musicList, false);
            mainBinding.musicList.addHeaderView(headerView);
            mainBinding.musicList.setAdapter(albumAdapter);
            mainBinding.musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        Random rand = new Random();
                        i = rand.nextInt(albumList.size() - 1) + 1;
                    }

                    ArrayList<Integer> indexSongsArray = albumList.get(i-1).getSongIndices();
                    startAlbumActivity(indexSongsArray, albumList.get(i-1));

                }
            });
        }
        mainBinding.searchAlbum.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchForAlbum();
                }
                return false;
            }
        });
        mainBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchForAlbum();
            }
        });
    }

    /**
     * start album activity and send songs of selected album
     *
     * @param indexSongsArray song indices of album
     */
    public void startAlbumActivity(ArrayList<Integer> indexSongsArray, Album selectedAlbum) {
        MyParcelable songObject = new MyParcelable();
        ArrayList<Song> albumSongs = new ArrayList<>();
        for (Integer songIndex : indexSongsArray) {
            albumSongs.add(songList.get(songIndex));
        }
        songObject.setArrList(albumSongs);
        songObject.setAlbum(selectedAlbum);
        Intent albumIntent = new Intent(MainActivity.this, AlbumActivity.class);
        albumIntent.putExtra("songObject", songObject);
        startActivity(albumIntent);
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

    /**
     * Set search for albums and display found albums / go to ablum activity if only one is found
     */
    public void searchForAlbum() {
        mainBinding.searchAlbum.clearFocus();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(mainBinding.searchAlbum.getWindowToken(), 0);
        }
        String inputText = mainBinding.searchAlbum.getText().toString().toLowerCase();
        final ArrayList<Album> foundAlbums = new ArrayList<>();
        for (Album currentAlbum : albumList) {
            String albumName = currentAlbum.getAlbum().toLowerCase();
            if (albumName.contains(inputText)) {
                foundAlbums.add(currentAlbum);
            }
        }
        if (foundAlbums.size() == 1) {
            ArrayList<Integer> indexSongsArray = foundAlbums.get(0).getSongIndices();
            startAlbumActivity(indexSongsArray, foundAlbums.get(0));
        } else if (foundAlbums.size() == 0) {
            mainBinding.albumSwitcher.showNext();
            switcherIndex = 1;
        } else {
            AlbumAdapter foundAlbumAdapter = new AlbumAdapter(this, foundAlbums);
            mainBinding.musicList.setAdapter(foundAlbumAdapter);
            mainBinding.musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ArrayList<Integer> indexSongsArray = foundAlbums.get(i).getSongIndices();
                    startAlbumActivity(indexSongsArray, foundAlbums.get(i));
                }
            });
            if (switcherIndex == 1) {
                mainBinding.albumSwitcher.showPrevious();
                switcherIndex = 0;
            }
        }
    }
}
