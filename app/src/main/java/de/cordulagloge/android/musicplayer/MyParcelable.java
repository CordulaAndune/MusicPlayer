package de.cordulagloge.android.musicplayer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cordula Gloge on 28/03/2018.
 */

public class MyParcelable implements Parcelable {

    private List<Song> songList = new ArrayList<Song>();
    private List<Album> albumList = new ArrayList<>();
    private int myInt = 0;
    private String str = null;

    public String getStr(String str) {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public List<Song> getArrList() {
        return songList;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    public void setArrList(List<Song> songList) {
        this.songList = songList;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }

    public int getMyInt() {
        return myInt;
    }

    public void setMyInt(int myInt) {
        this.myInt = myInt;
    }

    MyParcelable() {
        songList = new ArrayList<Song>();
        albumList = new ArrayList<Album>();
    }

    public MyParcelable(Parcel in) {
        myInt = in.readInt();
        str = in.readString();
        in.readTypedList(songList, Song.CREATOR);
        in.readTypedList(albumList, Album.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeInt(myInt);
        outParcel.writeString(str);
        outParcel.writeTypedList(songList);
        outParcel.writeTypedList(albumList);
    }

    public static final Parcelable.Creator<MyParcelable> CREATOR = new Parcelable.Creator<MyParcelable>() {
        @Override
        public MyParcelable createFromParcel(Parcel in) {
            return new MyParcelable(in);
        }

        @Override
        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };
}
