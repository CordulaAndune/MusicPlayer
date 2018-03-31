package de.cordulagloge.android.musicplayer;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;
import java.util.List;

import de.cordulagloge.android.musicplayer.databinding.ActivityPlayBinding;

public class PlayActivity extends AppCompatActivity {

    MediaPlayer songPlayer;
    ActivityPlayBinding playBinding;
    Handler myHandler = new Handler();

    final Runnable updateSong = new Runnable() {
        @Override
        public void run() {
            double currentTime = songPlayer.getCurrentPosition();
            playBinding.seekbar.setProgress((int) currentTime);
            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playBinding = DataBindingUtil.setContentView(this, R.layout.activity_play);

        Bundle b = getIntent().getExtras();
        MyParcelable songObject = b.getParcelable("parcel");
        List<Song> songArrayList = songObject.getArrList();
        int currentSongNumber = songObject.getMyInt();
        Song currentSong = songArrayList.get(0);

        playBinding.titleTextview.setText(currentSong.getTitle());
        playBinding.albumTextview.setText(currentSong.getAlbum());
        playBinding.albumImageview.setImageBitmap(currentSong.getAlbumImage(this));

        // Initialize mediaPlayer
        songPlayer = new MediaPlayer();
        try {
            songPlayer.setDataSource(currentSong.getFilePath());
            songPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int maxPosition = songPlayer.getDuration();
        playBinding.seekbar.setMax(maxPosition);

        // set onclicklistener for playButton
        playBinding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songPlayer.isPlaying()) {
                    songPlayer.pause();
                    playBinding.playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } else {
                    songPlayer.start();
                    playBinding.playButton.setImageResource(R.drawable.ic_pause_black_24dp);
                    double startTime = songPlayer.getCurrentPosition();
                    playBinding.seekbar.setProgress((int) startTime);
                    myHandler.postDelayed(updateSong, 100);
                }
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        songPlayer.stop();
    }
}
