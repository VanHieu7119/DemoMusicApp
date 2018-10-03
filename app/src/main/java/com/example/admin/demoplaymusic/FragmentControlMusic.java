package com.example.admin.demoplaymusic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

public class FragmentControlMusic extends Fragment {

    private List<Song> mSongList;
    private int mPosition;
    private TextView mTextViewNameSong;
    private TextView mTextViewNameArtist;
    private ImageButton mButtonNext;
    private ImageButton mButtonPrevious;
    private ImageButton mButtonPlay;
    private CircleImageView mImageMusic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control_music, container, false);
        mButtonNext = view.findViewById(R.id.buttonNextMini);
        mButtonPrevious = view.findViewById(R.id.buttonPreviousMiNi);
        mButtonPlay = view.findViewById(R.id.buttonPlayMini);
        mTextViewNameSong = view.findViewById(R.id.textViewNameSongMini);
        mTextViewNameArtist = view.findViewById(R.id.textViewNameArtistMini);
        mImageMusic = view.findViewById(R.id.imageDiscMini);
        Bundle bundle = getArguments();
        mSongList = bundle.getParcelableArrayList("LIST_SONG");
        mPosition = bundle.getInt("POSITION_SONG");
        return view;
    }

    public static FragmentControlMusic newInstance(int position, ArrayList<Song> songArrayList) {
        FragmentControlMusic fragmentControlMusic = new FragmentControlMusic();
        Bundle args = new Bundle();
        args.putInt("POSITION_SONG", position);
        args.putParcelableArrayList("LIST_SONG", songArrayList);
        fragmentControlMusic.setArguments(args);
        return fragmentControlMusic;
    }

    public void setUI() {
        mTextViewNameSong.setText(mSongList.get(mPosition).getName());
        mTextViewNameArtist.setText(mSongList.get(mPosition).getArtist());
        Glide.with(getActivity())
                .load(mSongList.get(mPosition).getImage())
                .apply(new RequestOptions().placeholder(R.drawable.ic_disk))
                .into(mImageMusic);
    }
}
