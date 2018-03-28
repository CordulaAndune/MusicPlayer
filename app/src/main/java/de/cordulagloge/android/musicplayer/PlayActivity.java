package de.cordulagloge.android.musicplayer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import de.cordulagloge.android.musicplayer.databinding.ActivityPlayBinding;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPlayBinding playBinding = DataBindingUtil.setContentView(this,R.layout.activity_play);

        Bundle b = getIntent().getExtras();
        MyParcelable songObject = b.getParcelable("parcel");
        List<Song> songArrayList = songObject.getArrList();
        int currentSongNumber = songObject.getMyInt();
        Song currentSong = songArrayList.get(currentSongNumber);

        playBinding.titleTextview.setText(currentSong.getTitle());
        playBinding.albumTextview.setText(currentSong.getAlbum());
    }
}
