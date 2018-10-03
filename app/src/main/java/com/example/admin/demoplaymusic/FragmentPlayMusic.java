package com.example.admin.demoplaymusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import static android.content.Context.BIND_AUTO_CREATE;

public class FragmentPlayMusic extends Fragment
        implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    private static final float ROTATE_FROM = 30.0f;
    private static final float ROTATE_TO = 360.0f;

    private ImageButton mButtonBack;
    private ImageButton mButtonNext;
    private ImageButton mButtonPrevious;
    private ImageButton mButtonDownload;
    private ImageButton mButtonPlay;
    private TextView mTextViewNameSong;
    private TextView mTextViewNameArtist;
    private TextView mTextViewTimeCurrent;
    private TextView mTextViewTimeFinal;
    private CircleImageView mImageMusic;
    private SeekBar mSeekBar;

    private TextView mTextViewNameSongMini;
    private TextView mTextViewNameArtistMini;
    private ImageButton mButtonNextMini;
    private ImageButton mButtonPreviousMini;
    private ImageButton mButtonPlayMini;
    private CircleImageView mImageMusicMini;
    private List<Song> mSongList;
    private int mPosition;
    private static ServicePlayMusic mServicePlayMusic;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    Animation mAnimationAlpha;
    Animation rotateAnimation;
    private boolean isBoundService = false;
    private View view;
    private View viewMain;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServicePlayMusic.ServicePlay binder = (ServicePlayMusic.ServicePlay) service;
            mServicePlayMusic = binder.getService();
            isBoundService = true;
            setServiceConnection();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBoundService = false;
        }
    };

    public static FragmentPlayMusic newInstance(int position, ArrayList<Song> songList) {
        FragmentPlayMusic fragmentPlayMusic = new FragmentPlayMusic();
        Bundle args = new Bundle();
        args.putInt("POSITION_SONG", position);
        args.putParcelableArrayList("LIST_SONG", songList);
        fragmentPlayMusic.setArguments(args);
        return fragmentPlayMusic;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play_music, container, false);
        viewMain = inflater.inflate(R.layout.activity_main,container,false);

        mButtonBack = view.findViewById(R.id.buttonBack);
        mButtonDownload = view.findViewById(R.id.buttonDownload);
        mButtonNext = view.findViewById(R.id.buttonNext);
        mButtonPrevious = view.findViewById(R.id.buttonPrevious);
        mButtonPlay = view.findViewById(R.id.buttonPlay);

        view.findViewById(R.id.layout_bottom).setVisibility(View.GONE); // An layout control
        viewMain.findViewById(R.id.rv_song).setVisibility(View.GONE);  // An list recycle view
        mTextViewNameSong = view.findViewById(R.id.textViewNameSong);
        mTextViewNameArtist = view.findViewById(R.id.textViewNameArtist);
        mTextViewTimeCurrent = view.findViewById(R.id.textViewTimeCurrent);
        mTextViewTimeFinal = view.findViewById(R.id.textViewTimeFinal);

        mImageMusic = view.findViewById(R.id.imageCD);
        mSeekBar = view.findViewById(R.id.seekBar);

////////////////////////////////////////////////////////////////////////////

        mTextViewNameSongMini = view.findViewById(R.id.textViewNameSongMini);
        mTextViewNameArtistMini = view.findViewById(R.id.textViewNameArtistMini);
        mButtonNextMini = view.findViewById(R.id.buttonNextMini);
        mButtonPreviousMini = view.findViewById(R.id.buttonPreviousMiNi);
        mButtonPlayMini = view.findViewById(R.id.buttonPlayMini);
        mImageMusicMini = view.findViewById(R.id.imageDiscMini);

        mButtonBack.setOnClickListener(this);
        mButtonDownload.setOnClickListener(this);
        mButtonPrevious.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mSongList = new ArrayList<>();
        mAnimationAlpha = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_alpha);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                mTextViewTimeCurrent.setText(
                        parseDurationToStringTime(mMediaPlayer.getCurrentPosition()));
                mHandler.postDelayed(this, 0);
            }
        };

        Bundle bundle = getArguments();
        if (bundle != null) {
            mSongList = bundle.getParcelableArrayList("LIST_SONG");
            mPosition = bundle.getInt("POSITION_SONG");
        }
        for (int i = 0; i < mSongList.size(); i++) {
            Log.d("List", mSongList.get(i).getName());
        }
        Log.d("SIZE", mSongList.size() + "");
        Log.d("POSITION", mPosition + "");
        Log.d("Time Duration", mSongList.get(mPosition).getDuration());

        setUISongMini();

        Intent intent = new Intent(getActivity(), ServicePlayMusic.class);
        getActivity().bindService(intent, mServiceConnection,BIND_AUTO_CREATE);

        return view;
    }



    private void setServiceConnection() {
        if (isBoundService) {
            setUISong();
            mServicePlayMusic.play(mPosition, mSongList);
            mButtonPlay.setImageResource(R.drawable.ic_pause_black_24dp);
        }
        mHandler.postDelayed(mRunnable, 0);

        isBoundService = true;
    }

    public void setUISong() {
        mMediaPlayer = mServicePlayMusic.getMediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        Glide.with(this)
                .load(mSongList.get(mPosition).getImage())
                .apply(new RequestOptions().placeholder(R.drawable.ic_disk))
                .into(mImageMusic);
        mTextViewNameSong.setText(mSongList.get(mPosition).getName());
        mTextViewNameArtist.setText(mSongList.get(mPosition).getArtist());
        mTextViewTimeFinal.setText(parseDurationToStringTime(mMediaPlayer.getDuration()));

        mSeekBar.setMax(mMediaPlayer.getDuration());

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if (input) {
                    mMediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Quay Disc Image
        // rotateDisc();
        rotateAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_anim);
        mImageMusic.startAnimation(rotateAnimation);
    }

    public void setUISongMini() {
        mTextViewNameSongMini.setText(mSongList.get(mPosition).getName());
        mTextViewNameArtistMini.setText(mSongList.get(mPosition).getArtist());
        Glide.with(getActivity())
                .load(mSongList.get(mPosition).getImage())
                .apply(new RequestOptions().placeholder(R.drawable.ic_disk))
                .into(mImageMusicMini);
    }

    private String parseDurationToStringTime(long duration) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void rotateDisc() {
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                mImageMusic.animate()
                        .rotationBy(360)
                        .withEndAction(this)
                        .setDuration(3000)
                        .setInterpolator(new LinearInterpolator())
                        .start();
            }
        };

        mImageMusic.animate()
                .rotationBy(360)
                .withEndAction(runnable)
                .setDuration(3000)
                .setInterpolator(new LinearInterpolator())
                .start();
    }


    @Override
    public void onClick(View v) {
        v.startAnimation(mAnimationAlpha);
        switch (v.getId()) {
            case R.id.buttonBack:
                view.findViewById(R.id.layout_top).setVisibility(View.GONE);
                view.findViewById(R.id.layout_bottom).setVisibility(View.VISIBLE);
//                viewMain.findViewById(R.id.rv_song).setVisibility(View.VISIBLE);

                break;
            case R.id.buttonNext:
                mButtonPlay.setImageResource(R.drawable.ic_pause_black_24dp);

                if (mPosition == mSongList.size() - 1) {
                    mPosition = 0;
                } else {
                    mPosition++;
                }
                mServicePlayMusic.next();
                setUISong();
                break;

            case R.id.buttonPlay:
                if (mServicePlayMusic.checkPlay()) {
                    mButtonPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    rotateAnimation.setRepeatCount(0);
                    mImageMusic.startAnimation(rotateAnimation);
                } else {
                    mButtonPlay.setImageResource(R.drawable.ic_pause_black_24dp);
                    rotateAnimation.setRepeatCount(Animation.INFINITE);
                    mImageMusic.startAnimation(rotateAnimation);
                }

                break;
            case R.id.buttonPrevious:
                mButtonPlay.setImageResource(R.drawable.ic_pause_black_24dp);
                if (mPosition == 0) {
                    mPosition = mSongList.size() - 1;
                } else {
                    mPosition--;
                }
                mServicePlayMusic.previous();
                setUISong();
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mPosition == mSongList.size() - 1) {
            mPosition = 0;
        } else {
            mPosition++;
        }
        mServicePlayMusic.next();
        setUISong();
    }
}
