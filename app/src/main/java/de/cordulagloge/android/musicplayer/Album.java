package de.cordulagloge.android.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Album implements Parcelable {
    private String mAlbum, mArtist;
    private String mFirstSongPath;
    private long mDuration;
    private ArrayList<Integer> mIndexOfTitles;


    public Album(String album, String artist, int indexFirstSong, String filePath) {
        this.mArtist = artist;
        this.mAlbum = album;
        this.mDuration = 0;
        this.mIndexOfTitles = new ArrayList<>();
        this.mIndexOfTitles.add(indexFirstSong);
        this.mFirstSongPath = filePath;
    }

    public Album(Parcel read) {
        String[] allSongData = new String[3];
        read.readStringArray(allSongData);
        this.mAlbum = allSongData[0];
        this.mArtist = allSongData[1];
        this.mFirstSongPath = allSongData[2];
        this.mDuration = read.readLong();
        this.mIndexOfTitles = read.readArrayList(Integer.class.getClassLoader());
    }

    public void addSong(int index) {
        this.mIndexOfTitles.add(index);
    }

    public String getAlbum() {
        return mAlbum;
    }

    public void setArtist(String artist) {
        this.mArtist = artist;
    }

    public String getFilePath() {
        return mFirstSongPath;
    }

    public String getArtist() {
        return mArtist;
    }

    public ArrayList<Integer> getSongIndices() {
        return this.mIndexOfTitles;
    }

    public Bitmap getAlbumImage(Context context) {
        MediaMetadataRetriever metaDataRetriever = new MediaMetadataRetriever();
        try {
            metaDataRetriever.setDataSource(mFirstSongPath);
            byte[] albumByte = metaDataRetriever.getEmbeddedPicture();
            return BitmapFactory.decodeByteArray(albumByte, 0, albumByte.length);
        } catch (Exception e) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.default_album_image);
        }
    }

    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeStringArray(new String[]{this.mAlbum, this.mArtist, this.mFirstSongPath});
        arg0.writeLong(this.mDuration);
        arg0.writeList(this.mIndexOfTitles);
    }
}
