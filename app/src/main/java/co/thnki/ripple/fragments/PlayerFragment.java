package co.thnki.ripple.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.thnki.ripple.R;
import co.thnki.ripple.Ripples;
import co.thnki.ripple.models.Play;
import co.thnki.ripple.services.SongPlayerService;
import co.thnki.ripple.singletons.Otto;
import co.thnki.ripple.utils.TimerUtil;

import static co.thnki.ripple.PlayerActivity.SONG_INDEX;
import static co.thnki.ripple.PlayerActivity.SONG_PATH;
import static co.thnki.ripple.R.id.seekBar;
import static co.thnki.ripple.Ripples.toast;
import static co.thnki.ripple.fragments.PlaylistFragment.PLAYLIST;

public class PlayerFragment extends Fragment implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener
{
    @Bind(R.id.albumArt)
    ImageView mAlbumArt;

    @Bind(seekBar)
    SeekBar mSeekBar;

    @Bind(R.id.songTitle)
    TextView mSongTitle;

    @Bind(R.id.artistName)
    TextView mArtistName;

    @Bind(R.id.repeatButton)
    ImageView mRepeatImageView;

    @Bind(R.id.shuffleButton)
    ImageView mShuffleImageView;

    @Bind(R.id.playPauseButton)
    ImageView mPlayPauseImageView;

    @Bind(R.id.songTotalDurationTextView)
    TextView mSongTotalDurationTextView;

    @Bind(R.id.songProgressTextView)
    TextView mSongProgressTextView;

    private static final String REPEAT = "repeat";
    private static final String SHUFFLE = "shuffle";
    private static final int DELTA_SEEK_TIME = 5000; // 5000 milliseconds


    private int mCurrentDuration;
    private int mTotalDuration;
    private MediaPlayer mMediaPlayer;
    private SharedPreferences mPreferences;
    private boolean mIsPlaying = false;

    private TimerUtil mTimerUtil;
    private ArrayList<String> mSongList;
    private MediaMetadataRetriever mMetaDataRetriever;
    private Handler mHandler;
    private int mCurrentSongIndex;
    private int mTotalNumberOfSongs;
    private PlaylistFragment mPlayListFragment;
    private SongPlayerService mSongPlayerService;
    boolean mServiceBound = false;

    public PlayerFragment()
    {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, view);
        Log.d("mCurrentDuration", "onCreateView B : " + mCurrentDuration);
        mPreferences = Ripples.getPreferences();
        mSongList = new ArrayList<>();
        mTimerUtil = new TimerUtil();

        mHandler = new Handler();
        mMetaDataRetriever = new MediaMetadataRetriever();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mPlayListFragment = new PlaylistFragment();
        mPlayListFragment.setSongList(mSongList);

        mSeekBar.setOnSeekBarChangeListener(this);
        Log.d("mCurrentDuration", "onCreateView E : " + mCurrentDuration);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Otto.register(this);

        Intent intent = new Intent(getActivity(), SongPlayerService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        Log.d("mCurrentDuration", "onStart B : " + mCurrentDuration);
        updateRepeatUi(false);
        updateShuffleUi(false);
        mSongList = new ArrayList<>();
        mSongList.addAll(getArguments().getStringArrayList(SONG_PATH));

        if (mSongList != null)
        {
            mTotalNumberOfSongs = mSongList.size();
        }
        mCurrentSongIndex = getArguments().getInt(SONG_INDEX);
        playNewSong();
        Log.d("mCurrentDuration", "onStart E : " + mCurrentSongIndex + " : " + mSongList);
    }

    private void playNewSong()
    {
        Log.d("mCurrentSongIndex", "playNewSong B : " + mCurrentSongIndex);
        if (mCurrentSongIndex < mTotalNumberOfSongs)
        {
            try
            {
                String songPath = mSongList.get(mCurrentSongIndex);
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(songPath);
                mMediaPlayer.prepare();
                mMetaDataRetriever.setDataSource(songPath);
                mCurrentDuration = 0;
                mTotalDuration = mMediaPlayer.getDuration();
                mSongTotalDurationTextView.setText(mTimerUtil.milliSecondsToTimer(mTotalDuration));

                String title = mMetaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                if (title == null || title.isEmpty())
                {
                    title = mMetaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                    File file = new File(songPath);
                    title += " - " + file.getName().replace(".mp3", "");
                }

                mSongTitle.setText(title);
                mArtistName.setText(mMetaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

                byte[] art = mMetaDataRetriever.getEmbeddedPicture();
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(mAlbumArt);
                Glide.with(getActivity()).load(art).into(imageViewTarget);

                playPauseSong(false);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            mCurrentSongIndex = mTotalNumberOfSongs - 1;
            mPlayPauseImageView.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            mIsPlaying = false;
        }
        Log.d("mCurrentSongIndex", "playNewSong E : " + mCurrentSongIndex);
    }


    @OnClick({R.id.fastForwardButton,
            R.id.fastRewindButton,
            R.id.nextButton,
            R.id.previousButton})
    public void onClick(ImageView view)
    {
        Log.d("mCurrentSongIndex", "onClick B : " + mCurrentSongIndex);
        switch (view.getId())
        {
            case R.id.fastForwardButton:
                if (mCurrentDuration + DELTA_SEEK_TIME < mTotalDuration)
                {
                    mMediaPlayer.seekTo(mCurrentDuration + DELTA_SEEK_TIME);
                }
                else
                {
                    mMediaPlayer.seekTo(mTotalDuration);
                }
                break;
            case R.id.fastRewindButton:
                if (mCurrentDuration - DELTA_SEEK_TIME > 0)
                {
                    mMediaPlayer.seekTo(mCurrentDuration - DELTA_SEEK_TIME);
                }
                else
                {
                    mMediaPlayer.seekTo(0);
                }
                break;
            case R.id.nextButton:
                mCurrentSongIndex++;
                mCurrentSongIndex = mCurrentSongIndex % mTotalNumberOfSongs;
                Log.d("mCurrentSongIndex", "onClick E : " + mCurrentSongIndex);
                playNewSong();
                break;
            case R.id.previousButton:
                if (mCurrentDuration < 5000)
                {
                    if (mCurrentSongIndex > 0)
                    {
                        mCurrentSongIndex--;
                    }
                    else
                    {
                        mCurrentSongIndex = mTotalNumberOfSongs - 1;
                    }
                }
                Log.d("mCurrentSongIndex", "onClick E : " + mCurrentSongIndex);
                playNewSong();
                break;
        }
    }

    @OnClick(R.id.repeatButton)
    public void updateRepeatUi()
    {
        updateRepeatUi(true);
    }

    public void updateRepeatUi(boolean modify)
    {
        int value = mPreferences.getInt(REPEAT, 0);
        if (modify)
        {
            value = (value + 1) % 3;
            mPreferences.edit().putInt(REPEAT, value).apply();
        }
        switch (value)
        {
            case 0:
                mRepeatImageView.setImageResource(R.drawable.ic_repeat_white_36dp);
                break;
            case 1:
                mRepeatImageView.setImageResource(R.drawable.ic_repeat_accent_36dp);
                break;
            case 2:
                mRepeatImageView.setImageResource(R.drawable.ic_repeat_one_accent_36dp);
                break;
        }
    }

    @OnClick(R.id.shuffleButton)
    public void updateShuffleUi()
    {
        updateShuffleUi(true);
    }

    public void updateShuffleUi(Boolean modify)
    {
        boolean value = mPreferences.getBoolean(SHUFFLE, true);
        if (modify)
        {
            value = !value;
            mPreferences.edit().putBoolean(SHUFFLE, value).apply();
        }

        if (value)
        {
            mShuffleImageView.setImageResource(R.drawable.ic_shuffle_accent_36dp);
            Collections.shuffle(mSongList);
        }
        else
        {
            mShuffleImageView.setImageResource(R.drawable.ic_shuffle_white_36dp);
            mSongList = new ArrayList<>();
            mSongList.addAll(getArguments().getStringArrayList(SONG_PATH));
        }
        mPlayListFragment.setSongList(mSongList);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer)
    {
        Log.d("mCurrentSongIndex", "onCompletion B : " + mCurrentSongIndex);
        switch (mPreferences.getInt(REPEAT, 0))
        {
            case 0:
                if (mCurrentSongIndex < mTotalNumberOfSongs)
                {
                    mCurrentSongIndex++;
                }
                break;
            case 1:
                mCurrentSongIndex++;
                mCurrentSongIndex = mCurrentSongIndex % mTotalNumberOfSongs;
                break;
        }
        Log.d("mCurrentSongIndex", "onCompletion E : " + mCurrentSongIndex);
        playNewSong();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
    {

    }

    public void updateProgressBar()
    {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable()
    {
        public void run()
        {
            mCurrentDuration = mMediaPlayer.getCurrentPosition();
            mSongProgressTextView.setText(mTimerUtil.milliSecondsToTimer(mCurrentDuration));
            int progress = (mTimerUtil.getProgressPercentage(mCurrentDuration, mTotalDuration));
            mSeekBar.setProgress(progress);
            if (mIsPlaying)
            {
                mHandler.postDelayed(this, 100);
            }
            Log.d("mCurrentDuration", "mUpdateTimeTask E : " + mCurrentDuration);
        }
    };

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mMediaPlayer.getDuration();
        int currentPosition = mTimerUtil.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mMediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    @OnClick(R.id.playPauseButton)
    public void updatePlayPauseUi()
    {
        toast(mSongPlayerService.getInt()+"");
        playPauseSong(true);
    }

    public void playPauseSong(boolean toggle)
    {
        Log.d("mCurrentDuration", "playPauseSong B : " + mCurrentDuration);
        if (toggle)
        {
            mIsPlaying = !mIsPlaying;
        }
        else
        {
            mIsPlaying = true;
        }
        if (mIsPlaying)
        {
            mMediaPlayer.start();
            mPlayPauseImageView.setImageResource(R.drawable.ic_pause_white_48dp);
        }
        else
        {
            mMediaPlayer.pause();
            mPlayPauseImageView.setImageResource(R.drawable.ic_play_arrow_white_48dp);
        }
        updateProgressBar();
        Log.d("mCurrentDuration", "playPauseSong B : " + mCurrentDuration);
    }

    @OnClick(R.id.playList)
    public void showPlaylist()
    {
        mPlayListFragment.setSongList(mSongList);
        mPlayListFragment.show(getFragmentManager(), PLAYLIST);
    }

    @Subscribe
    public void play(Play play)
    {
        mCurrentSongIndex = play.getPosition();
        playNewSong();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        Otto.unregister(this);
        if (mServiceBound)
        {
            getActivity().unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            SongPlayerService.SongPlayerServiceBinder myBinder = (SongPlayerService.SongPlayerServiceBinder) service;
            mSongPlayerService = myBinder.getService();
            mServiceBound = true;
        }
    };
}
