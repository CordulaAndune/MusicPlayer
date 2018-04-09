package de.cordulagloge.android.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

/**
 * Find all MP3-Files on sdCard
 *
 * Created by Cordula Gloge on 26/03/2018.
 * based on http://www.jayrambhia.com/blog/android-audio-cursor
 */

public class SongManager {
    public static Cursor populateQueries(Context context) {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TRACK
        };
        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Audio.Media.TITLE);
    }
}
