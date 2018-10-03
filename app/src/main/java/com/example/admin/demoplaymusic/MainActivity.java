package com.example.admin.demoplaymusic;

import android.Manifest;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemRecyclerViewClickListener {

    private static final int REQUEST_PERMISSION_STORAGE = 123;

    private RecyclerView mRecyclerView;
    private SongAdapter mSongAdapter;
    private LoadAllSong mLoadAllSong;
    private List<Song> mSongList;
    public ServicePlayMusic mServicePlayMusic;
    ServiceConnection mServiceConnection;
    private ConstraintLayout mLayoutPlayMusic;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
    }

    //    @Override
    //    protected void onRestart() {
    //        super.onRestart();
    //        mServicePlayMusic.unbindService(mServiceConnection);
    //    }

    public void initUI() {
        mLayoutPlayMusic = findViewById(R.id.frameControlMusic);

        mLoadAllSong = new LoadAllSong(getApplicationContext());
        mSongList = new ArrayList<>();
        mSongList = mLoadAllSong.getSongList();
        mSongAdapter = new SongAdapter(getApplicationContext(), this);
        mSongAdapter.updateData(mSongList);
        mRecyclerView = findViewById(R.id.rv_song);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mSongAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initUI();
                } else {
                    return;
                }
                break;
            default:
                break;
        }
    }

    public void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                        REQUEST_PERMISSION_STORAGE);
            } else {
                initUI();
            }
        } else {
            initUI();
        }
    }

    //override từ interface
    @Override
    public void onItemClick(int i) {
        mPosition = i;
        Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();
        //        mServicePlayMusic.play(i, mSongList);
        //        Intent intent = new Intent(MainActivity.this, FragmentPlayMusic.class);
        //        Bundle bundle = new Bundle();
        //        bundle.putParcelableArrayList("LIST", (ArrayList<? extends Parcelable>)
        // mSongList);
        //        bundle.putInt("POSITION", i);
        //        intent.putExtra("BUNDLE", bundle);
        //        startActivity(intent);
        addFragmentPlayMusic();
        //        addFragmentControlMusic();
    }

    public void addFragmentPlayMusic() {
        FragmentPlayMusic fragmentPlayMusic =
                FragmentPlayMusic.newInstance(mPosition, (ArrayList<Song>) mSongList);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(FragmentPlayMusic.class.getSimpleName());
        fragmentTransaction.replace(R.id.frameControlMusic, fragmentPlayMusic);
        fragmentTransaction.commit();
    }

    //    public void setServiceConnection() {
    //        Intent intent = new Intent(getApplicationContext(), ServicePlayMusic.class);
    //
    //         mServiceConnection = new ServiceConnection() {
    //            @Override
    //            public void onServiceConnected(ComponentName name, IBinder service) {
    //                ServicePlayMusic.ServicePlay binder = (ServicePlayMusic.ServicePlay) service;
    //                mServicePlayMusic = binder.getService();
    //            }
    //
    //            @Override
    //            public void onServiceDisconnected(ComponentName name) {
    //                Toast.makeText(MainActivity.this, "onServiceDisconnected", Toast.LENGTH_SHORT)
    //                        .show();
    //            }
    //        };
    //        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    //    }
}
