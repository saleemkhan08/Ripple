package co.thnki.ripple.fragments;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thnki.ripple.R;
import co.thnki.ripple.adapters.SongRecyclerAdapter;
import co.thnki.ripple.models.Song;

public class SongListFragment extends Fragment
{
    public SongListFragment()
    {
    }

    @Bind(R.id.songList)
    RecyclerView mSongRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View parentView = inflater.inflate(R.layout.fragment_song_list, container, false);
        ButterKnife.bind(this, parentView);

        SongRecyclerAdapter adapter = new SongRecyclerAdapter(getActivity(), getPlayList());
        mSongRecyclerView.setAdapter(adapter);
        mSongRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return parentView;
    }

    public ArrayList<Song> getPlayList()
    {
        ArrayList<Song> list = new ArrayList<>();
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        //iterate over results if valid
        if (musicCursor != null && musicCursor.moveToFirst())
        {
            //get columns
            int titleColumnIndex = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);

            int albumIdIndex = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);

            int pathIndex = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA);

            do
            {
                Song song = new Song();
                song.setTrack(musicCursor.getString(titleColumnIndex));
                long albumId = musicCursor.getLong(albumIdIndex);
                song.setAlbumId(albumId);
                song.setLocalPath(musicCursor.getString(pathIndex));
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                song.setImageUri(ContentUris.withAppendedId(sArtworkUri, Long.valueOf(albumId)).toString());
                list.add(song);
            }
            while (musicCursor.moveToNext());
        }
        return list;
    }

    public void setMetadata(Song song, int id)
    {

    }
/*
String path = new File(new URI(path).getPath()).getCanonicalPath();
Cursor c = context.getContentResolver().query(
    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
    new String[] {
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.YEAR
    },
    MediaStore.Audio.Media.DATA + " = ?",
    new String[] {
            path
    },
    "");

if (null == c) {
    // ERROR
}

while (c.moveToNext()) {
    c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
    c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
    c.getString(c.getColumnIndex(MediaStore.Audio.Media.TRACK));
    c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
    c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
    c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
    c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
    c.getString(c.getColumnIndex(MediaStore.Audio.Media.YEAR));
}
*/
}
