package de.cordulagloge.android.musicplayer;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cordula Gloge on 28/03/2018.
 */

public class MyParcelable implements Parcelable {

    private List<Song> songList = new ArrayList<Song>();
    private int myInt = 0;
    private String str = null;

    public String getStr(String str){
        return str;
    }

    public void setStr(String str){
        this.str = str;
    }

    public List<Song> getArrList(){
        return songList;
    }

    public void setArrList(List<Song> songList){
        this.songList = songList;
    }

    public int getMyInt(){
        return myInt;
    }

    public void setMyInt(int myInt){
        this.myInt = myInt;
    }

    MyParcelable(){
        songList = new ArrayList<Song>();
    }

    public MyParcelable(Parcel in){
        myInt = in.readInt();
        str = in.readString();
        Log.i("MyParcelable","createFromParcel before List");
        in.readTypedList(songList, Song.CREATOR);
        Log.i("MyParcelable","createFromParcel before List");
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags){
        outParcel.writeInt(myInt);
        outParcel.writeString(str);
        outParcel.writeTypedList(songList);
    }

    public static final Parcelable.Creator<MyParcelable> CREATOR = new Parcelable.Creator<MyParcelable>(){
        @Override
        public MyParcelable createFromParcel(Parcel in){
            return new MyParcelable(in);
        }

        @Override
        public MyParcelable[] newArray(int size){
            return new MyParcelable[size];
        }
    };
}
