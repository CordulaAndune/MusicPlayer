package de.cordulagloge.android.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

/**
 * Song object with title, album, artist and filepath
 * <p>
 * Created by Cordula Gloge on 26/03/2018.
 * code for MetaDataRetriever based on http://mrbool.com/how-to-extract-meta-data-from-media-file-in-android/28130
 * and http://www.jayrambhia.com/blog/android-audio-cursor
 */

public class Song {
    private String mTitle, mAlbum, mArtist, mFilePath, mDuration, mId;
    private Bitmap mAlbumImage;

    public Song(Cursor cursor, Context context) {
        mTitle = cursor.getString(0);
        mArtist = cursor.getString(1);
        mAlbum = cursor.getString(2);
        mDuration = cursor.getString(3);
        mFilePath = cursor.getString(4);
        mId = cursor.getString(5);

        MediaMetadataRetriever metaDataRetriever = new MediaMetadataRetriever();
        metaDataRetriever.setDataSource(mFilePath);

        try {
            byte[] image = metaDataRetriever.getEmbeddedPicture();
            mAlbumImage = BitmapFactory.decodeByteArray(image, 0, image.length);
        } catch (Exception e) {
            mAlbumImage = BitmapFactory.decodeResource(context.getResources(),R.drawable.default_album_image);
        }

    }

    public String getTitle() {
        return mTitle;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public String getArtist() {
        return mArtist;
    }

    public String getDuration() {
        return mDuration;
    }

    public String getId() {
        return mId;
    }

    public Bitmap getAlbumImage() {
        return mAlbumImage;
    }
}
