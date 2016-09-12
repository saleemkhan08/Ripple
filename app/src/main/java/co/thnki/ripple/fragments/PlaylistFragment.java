package co.thnki.ripple.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.thnki.ripple.R;
import co.thnki.ripple.adapters.SongPathRecyclerAdapter;
import co.thnki.ripple.models.Play;
import co.thnki.ripple.singletons.Otto;

public class PlaylistFragment extends DialogFragment
{
    public static final String PLAYLIST = "playList";
    @Bind(R.id.playListRecyclerView)
    RecyclerView mPlayListRecyclerView;

    private SongPathRecyclerAdapter mAdapter;
    private ArrayList<String> mSongList;

    public PlaylistFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View parentView = inflater.inflate(R.layout.fragment_playlist, container, false);
        ButterKnife.bind(this, parentView);
        Otto.register(this);

        mAdapter = new SongPathRecyclerAdapter(getActivity());
        mAdapter.setSongList(mSongList);
        mPlayListRecyclerView.setAdapter(mAdapter);
        mPlayListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return parentView;
    }

    @OnClick(R.id.closeDialog)
    public void close()
    {
        dismiss();
    }

    @Subscribe
    public void play(Play play)
    {
        close();
    }

    public void setSongList(ArrayList<String> songList)
    {
        mSongList = songList;
        if (mAdapter != null)
        {
            mAdapter.setSongList(mSongList);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);
    }
}
