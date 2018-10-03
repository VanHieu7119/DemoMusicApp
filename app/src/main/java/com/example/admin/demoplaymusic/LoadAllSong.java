package com.example.admin.demoplaymusic;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class LoadAllSong {
    private Context mContext;
    private List<Song> mSongList;

    public LoadAllSong(Context context) {
        mContext = context;
        mSongList = new ArrayList<>();
    }

    public List<Song> getSongList() {
        Cursor cursor = mContext.getApplicationContext()
                .getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int songId = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songName = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLink = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songImage = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int songDuration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);


            do {
                int currentId = cursor.getInt(songId);
                String currentName = cursor.getString(songName);
                String currentArtist = cursor.getString(songArtist);
                String currentLink = cursor.getString(songLink);
                String currentImage = String.valueOf(ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"),
                        cursor.getInt(songImage)));
                String currentDuration = cursor.getString(songDuration);
                mSongList.add(
                        new Song(currentId, currentName, currentArtist, currentLink, currentImage,currentDuration));
                //                Log.d("ALBUM_ID","album id: "+ songImage);
                //                Log.d("currentImage","image string: "+ currentImage);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return mSongList;
    }
}
