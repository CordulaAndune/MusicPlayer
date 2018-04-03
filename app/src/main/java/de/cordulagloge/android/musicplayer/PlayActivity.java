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
import java.util.concurrent.TimeUnit;

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
            long currentTime = songPlayer.getCurrentPosition();
            playBinding.seekbar.setProgress((int) currentTime);
            playBinding.songPlayed.setText(convertMilliSecToSec(currentTime));
            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playBinding = DataBindingUtil.setContentView(this, R.layout.activity_play);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
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

        // Initialize mediaPlayer
        songPlayer = new MediaPlayer();
        setMediaPlayer(currentSong);
        setDescription(currentSong);

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
                boolean isCompleted = true;
                    setNextSong(isCompleted);
            }
        });

        playBinding.forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextSong(false);
            }
        });

        playBinding.backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPreviousSong();
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
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
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
    public void setNextSong(boolean isCompletedSong){
        if (songNumber < numberOfSongs-1) {
            songNumber++;
            Song currentSong = songArrayList.get(songNumber);
            setMediaPlayer(currentSong);
            if(isCompletedSong){
                songPlayer.start();
            }
        }
        else {
            songNumber = 0;
            Song currentSong = songArrayList.get(songNumber);
            setMediaPlayer(currentSong);
        }
    }

    /**
     * set previous song of playlist when song is played for less than 20 sec or start song again, when played longer
     */
    public void setPreviousSong(){
        if (songPlayer.getCurrentPosition() > 20000) {
            songPlayer.seekTo(0);
        }
        else {
            if (songNumber > 0) {
                songNumber--;
                Song currentSong = songArrayList.get(songNumber);
                setMediaPlayer(currentSong);
            } else {
                songNumber = numberOfSongs - 1;
            }
            Song currentSong = songArrayList.get(songNumber);
            setMediaPlayer(currentSong);
        }
    }

    /**
     * prepare mediaplayer to play current song
     * @param currentSong Song object with data of the current song
     */
    public void setMediaPlayer(Song currentSong){
        boolean hasPlayed = songPlayer.isPlaying();
        try {
            // update mediaplayer
            songPlayer.stop();
            songPlayer.reset();
            songPlayer.setDataSource(currentSong.getFilePath());
            songPlayer.prepare();
            // update description
            setDescription(currentSong);
            int maxPosition = songPlayer.getDuration();
            playBinding.seekbar.setMax(maxPosition);
            String startTime = convertMilliSecToSec(songPlayer.getCurrentPosition());
            playBinding.songPlayed.setText(startTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (hasPlayed){
            songPlayer.start();
        }
    }

    public void setDescription(Song currentSong){
        playBinding.titleTextview.setText(currentSong.getTitle());
        playBinding.albumTextview.setText(currentSong.getAlbum());
        playBinding.albumImageview.setImageBitmap(currentSong.getAlbumImage(this));
        playBinding.songDuration.setText(convertMilliSecToSec(songPlayer.getDuration()));
    }

    public String convertMilliSecToSec(long time){
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
        );
    }
}
