package co.thnki.ripple.fragments;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.thnki.ripple.R;
import co.thnki.ripple.adapters.AlbumRecyclerViewAdapter;
import co.thnki.ripple.models.Album;

public class ArtistFragment extends Fragment
{
    public ArtistFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_album_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView)
        {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            recyclerView.setAdapter(new AlbumRecyclerViewAdapter(getAlbumList(), getActivity()));
        }
        return view;
    }

    private ArrayList<Album> getAlbumList()
    {
        ArrayList<Album> albumList = new ArrayList<>();
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor albumCursor = musicResolver.query(albumUri, null, null, null, null);
        if (albumCursor != null && albumCursor.moveToFirst())
        {
            //get columns
            int titleColumnIndex = albumCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM);
            int artistColumnIndex = albumCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ARTIST);
            int yearColumnIndex = albumCursor.getColumnIndex
                    (MediaStore.Audio.Albums.FIRST_YEAR);
            int albumIdIndex = albumCursor.getColumnIndex
                    (MediaStore.Audio.Albums._ID);

            int albumArtIndex = albumCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM_ART);

            Log.d("AlbumUri", titleColumnIndex +" : "+artistColumnIndex +" : "+albumArtIndex+" : "+albumIdIndex+" : "+yearColumnIndex);
            do
            {
                Album album = new Album();
                album.setAlbumName(albumCursor.getString(titleColumnIndex));
                album.setAlbumArtist(albumCursor.getString(artistColumnIndex));
                album.setAlbumYear(albumCursor.getString(yearColumnIndex));
                album.setAlbumId(albumCursor.getLong(albumIdIndex));

                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                album.setAlbumArtUri( ContentUris.withAppendedId(sArtworkUri, album.getAlbumId()).toString()+"");
                albumList.add(album);
            }
            while (albumCursor.moveToNext());
        }
        return albumList;
    }
}