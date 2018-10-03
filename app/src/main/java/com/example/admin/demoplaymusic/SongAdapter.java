package com.example.admin.demoplaymusic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private Context mContext;
    private List<Song> mSongList;
    private OnItemRecyclerViewClickListener mOnItemRecyclerViewClickListener;

    public SongAdapter(Context context, OnItemRecyclerViewClickListener possitionSong) {
        mContext = context;
        mSongList = new ArrayList<>();
        this.mOnItemRecyclerViewClickListener = possitionSong;
    }

    public void updateData(List<Song> mList) {
        if (mSongList != null) {
            mSongList.clear();
        }
        assert mSongList != null;
        mSongList.addAll(mList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
       viewHolder.bindData();
    }

//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
//        Song song = mSongList.get(i);
//        viewHolder.mTextViewNameSong.setText(song.getName() + ".mp3");
//        viewHolder.mTextViewNameArtist.setText(song.getArtist());
//        Glide.with(mContext)
//                .load(song.getImage())
//                .apply(new RequestOptions().placeholder(
//                        R.drawable.ic_photo_size_select_actual_black_24dp))
//                .into(viewHolder.mImageView);
//    }


    @Override
    public int getItemCount() {
        return mSongList != null ? mSongList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextViewNameSong;
        private TextView mTextViewNameArtist;
        private ImageView mImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewNameSong = itemView.findViewById(R.id.textViewNameSongMini);
            mTextViewNameArtist = itemView.findViewById(R.id.textViewNameArtistMini);
            mImageView = itemView.findViewById(R.id.imageSong);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //            Toast.makeText(mContext, "" + getAdapterPosition(), Toast.LENGTH_SHORT)
            // .show();
            mOnItemRecyclerViewClickListener.onItemClick(getAdapterPosition());
        }

        public void bindData() {
            Song song = mSongList.get(getAdapterPosition());
            mTextViewNameSong.setText(song.getName() + ".mp3");
            mTextViewNameArtist.setText(song.getArtist());
                    Glide.with(mContext)
                            .load(song.getImage())
                            .apply(new RequestOptions().placeholder(
                                    R.drawable.ic_file_mp3))
                            .into(mImageView);

        }
    }
}
