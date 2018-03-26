package de.cordulagloge.android.musicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

/**
 * Song object with title, album, artist and filepath
 * <p>
 * Created by Cordula Gloge on 26/03/2018.
 * code for MetaDataRetriever based on http://mrbool.com/how-to-extract-meta-data-from-media-file-in-android/28130
 * and http://www.jayrambhia.com/blog/android-audio-cursor
 */

public class Song {
    private String mTitle, mAlbum, mArtist, mFilePath;
    private Bitmap mAlbumImage;

    public Song(String filePath) {
        MediaMetadataRetriever metaDataRetriever = new MediaMetadataRetriever();
        metaDataRetriever.setDataSource(filePath);
        try {

            mTitle = metaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            mArtist = metaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            mAlbum = metaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            mFilePath = filePath;
            if (mTitle == null) {
                mTitle = MediaStore.Audio.Media.TITLE;
                mArtist = MediaStore.Audio.Media.ARTIST;
                mAlbum = MediaStore.Audio.Media.ALBUM;
            }
        } catch (Exception e) {

            mTitle = "Unknown Title";
            mAlbum = "Unknown Album";
            mArtist = "Unknown Artist";
            mFilePath = filePath;
        }

        try {
            byte[] image = metaDataRetriever.getEmbeddedPicture();
            mAlbumImage = BitmapFactory.decodeByteArray(image, 0, image.length);
        } catch (Exception e) {
            mAlbumImage = null;
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

    public Bitmap getAlbumImage() {
        return mAlbumImage;
    }
}
