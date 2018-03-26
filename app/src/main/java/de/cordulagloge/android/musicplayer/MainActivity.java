package de.cordulagloge.android.musicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import de.cordulagloge.android.musicplayer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // create ArrayList with all songfile-paths
        final ArrayList<String> audioFilePaths = SongManager.getAudioFiles();

        ArrayList<Song> songList = new ArrayList<>();
        for (String currentPath: audioFilePaths) {
            songList.add(new Song(currentPath));
        }

        ListView musicList = findViewById(R.id.musicList);
        SongAdapter audioFileAdapter = new SongAdapter(this, songList);
        musicList.setAdapter(audioFileAdapter);
    }
}
