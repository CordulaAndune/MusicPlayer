package de.cordulagloge.android.musicplayer;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.List;

import de.cordulagloge.android.musicplayer.databinding.ActivityPlayBinding;

public class PlayActivity extends AppCompatActivity {

    MediaPlayer songPlayer;
    ActivityPlayBinding playBinding;
    List<Song> songArrayList;
    int songNumber, numberOfSongs;
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

        if (getActionBar() != null) {
            getActionBar().hide();
        }

        songNumber = 0;

        Bundle b = getIntent().getExtras();
        MyParcelable songObject = null;
        if (b != null) {
            songObject = b.getParcelable("parcel");
        }
        songArrayList = songObject.getArrList();
        Song currentSong = songArrayList.get(songNumber);
        numberOfSongs = songArrayList.size();

        playBinding.titleTextview.setText(currentSong.getTitle());
        playBinding.albumTextview.setText(currentSong.getAlbum());
        playBinding.albumImageview.setImageBitmap(currentSong.getAlbumImage(this));


        // Initialize mediaPlayer
        songPlayer = new MediaPlayer();
        setMediaPlayer(currentSong);

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

        songPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                    setNextSong();
            }
        });

        // set position in song if seekbar is changed
        playBinding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int seekbarPosition, boolean isInput) {
                if (songPlayer != null && isInput){
                    songPlayer.seekTo(seekbarPosition);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        songPlayer.stop();
    }

    /**
     * set next song of playlist or stop playing and set mediaplayer to first song of playlist
     */
    public void setNextSong(){
        if (songNumber < numberOfSongs) {
            songNumber++;
            Song currentSong = songArrayList.get(songNumber);
            setMediaPlayer(currentSong);
        }
        else {
            songNumber = 0;
            Song currentSong = songArrayList.get(songNumber);
            setMediaPlayer(currentSong);
            playBinding.playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }

    /**
     * prepare mediaplayer to play current song
     * @param currentSong Song object with data of the current song
     */
    public void setMediaPlayer(Song currentSong){
        try {
            songPlayer.setDataSource(currentSong.getFilePath());
            songPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
