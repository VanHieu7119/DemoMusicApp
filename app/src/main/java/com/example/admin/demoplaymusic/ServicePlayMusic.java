package com.example.admin.demoplaymusic;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;

public class ServicePlayMusic extends Service {

    private IBinder mIBinder = new ServicePlay();
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private List<Song> mSongList;
    private int mPosition;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mIBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class ServicePlay extends Binder {
        public ServicePlayMusic getService() {
            return ServicePlayMusic.this;
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void playMusic() {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mSongList.get(mPosition).getLink());
            mMediaPlayer.prepare();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.start();
            Toast.makeText(getApplicationContext(), mSongList.get(mPosition).getName() + "",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(int position, List<Song> songs) {
        mPosition = position;
        mSongList = songs;

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        playMusic();
    }

    public void next() {
        if (mPosition == mSongList.size() - 1) {
            mPosition = 0;
        } else {
            mPosition++;
        }
        playMusic();
    }

    public void previous() {
        if (mPosition == 0) {
            mPosition = (mSongList.size() - 1);
        } else {
            mPosition--;
        }
        playMusic();
    }

    public boolean checkPlay() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            return true;
        } else {
            mMediaPlayer.start();
            return false;
        }
    }
}
