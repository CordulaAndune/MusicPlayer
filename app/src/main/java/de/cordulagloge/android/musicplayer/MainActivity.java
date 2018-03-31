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
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                songList.add(new Song(cursor,this));
            }
        }

        SongAdapter audioFileAdapter = new SongAdapter(this, songList);
        mainBinding.musicList.setAdapter(audioFileAdapter);
        mainBinding.musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyParcelable songObject = new MyParcelable();
                songObject.setArrList(songList.subList(i,i+1));
                songObject.setMyInt(i);
                Intent playIntent = new Intent(MainActivity.this, PlayActivity.class);
                playIntent.putExtra("parcel",songObject);
                startActivity(playIntent);
            }
        });
    }
}
