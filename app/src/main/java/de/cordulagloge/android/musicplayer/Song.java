package de.cordulagloge.android.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Song object with title, album, artist and filepath
 * <p>
 * Created by Cordula Gloge on 26/03/2018.
 * code for MetaDataRetriever based on http://mrbool.com/how-to-extract-meta-data-from-media-file-in-android/28130
 * and http://www.jayrambhia.com/blog/android-audio-cursor
 */

public class Song implements Parcelable {
    private String mTitle, mAlbum, mArtist, mFilePath, mDuration, mId;

    public Song(Cursor cursor, Context context) {
        this.mTitle = cursor.getString(0);
        this.mArtist = cursor.getString(1);
        this.mAlbum = cursor.getString(2);
        this.mDuration = cursor.getString(3);
        this.mFilePath = cursor.getString(4);
        this.mId = cursor.getString(5);
    }

    public Song(Parcel read) {
        String[] allSongData = new String[6];
        read.readStringArray(allSongData);
        this.mTitle = allSongData[0];
        this.mAlbum = allSongData[1];
        this.mArtist = allSongData[2];
        this.mFilePath = allSongData[3];
        this.mDuration = allSongData[4];
        this.mId = allSongData[5];
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

    public Bitmap getAlbumImage(Context context) {
        MediaMetadataRetriever metaDataRetriever = new MediaMetadataRetriever();
        try {
            metaDataRetriever.setDataSource(mFilePath);
            byte[] albumByte = metaDataRetriever.getEmbeddedPicture();
            return BitmapFactory.decodeByteArray(albumByte, 0, albumByte.length);
        } catch (Exception e) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.default_album_image);
        }
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeStringArray(new String[]{this.mTitle,
                this.mAlbum, this.mArtist, this.mFilePath, this.mDuration, this.mId});
    }
}
