package com.example.admin.demoplaymusic;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private int mId;
    private String mName;
    private String mArtist;
    private String mLink;
    private String mImage;
    private String mDuration;

    public Song(int id, String name, String artist, String link, String image, String duration) {
        mId = id;
        mName = name;
        mArtist = artist;
        mLink = link;
        mImage = image;
        mDuration = duration;
    }

    public Song() {
    }

    protected Song(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mArtist = in.readString();
        mLink = in.readString();
        mImage = in.readString();
        mDuration = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mArtist);
        dest.writeString(mLink);
        dest.writeString(mImage);
        dest.writeString(mDuration);
    }
}
