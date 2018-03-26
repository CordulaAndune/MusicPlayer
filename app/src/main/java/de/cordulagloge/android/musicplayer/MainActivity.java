package de.cordulagloge.android.musicplayer;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import de.cordulagloge.android.musicplayer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ArrayList<Song> songList = new ArrayList<>();

        // Get Cursor
        Cursor cursor = SongManager.populateQueries(this);
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                songList.add(new Song(cursor,this));
            }
        }


        ListView musicList = findViewById(R.id.musicList);
        SongAdapter audioFileAdapter = new SongAdapter(this, songList);
        musicList.setAdapter(audioFileAdapter);
    }



}
