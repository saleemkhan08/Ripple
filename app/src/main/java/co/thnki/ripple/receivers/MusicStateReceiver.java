package co.thnki.ripple.receivers;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import co.thnki.ripple.models.Song;
import co.thnki.ripple.services.RippleService;

import static co.thnki.ripple.services.RippleService.ALBUM;
import static co.thnki.ripple.services.RippleService.ARTIST;
import static co.thnki.ripple.services.RippleService.PLAY_STATE;
import static co.thnki.ripple.services.RippleService.TRACK;

public class MusicStateReceiver extends BroadcastReceiver
{
    public static final String CURRENT_SONG = "currentSong";

    public MusicStateReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("MusicStateReceiver", "onReceive : " + intent.getExtras());
        try
        {
            Song song = new Song();
            Intent serviceIntent = new Intent(context, RippleService.class);

            song.setTrack(intent.getStringExtra(TRACK));
            song.setAlbum(intent.getStringExtra(ALBUM));
            song.setArtist(intent.getStringExtra(ARTIST));
            song.setPlayState(intent.getBooleanExtra(PLAY_STATE, false));
            song.setImageUri(getImage(context, song));
            Log.d("MusicStateReceiver", "album art uri  : " + song.getImageUri());
            serviceIntent.putExtra(CURRENT_SONG, song);
            context.startService(serviceIntent);
            Log.d("MusicStateReceiver", "Service started");
        }
        catch (Exception e)
        {
            Log.d("MusicStateReceiver", "Exception : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getImage(Context context, Song song)
    {
        ContentResolver musicResolver = context.getContentResolver();

        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, new String[]{MediaStore.Audio.Media.ALBUM_ID},
                MediaStore.Audio.Media.TITLE + " = ? AND " +
                        MediaStore.Audio.Media.ARTIST + " = ? AND " +
                        MediaStore.Audio.Media.ALBUM + " = ?",

                new String[]{song.getTrack(), song.getArtist(), song.getAlbum()}, null);

        long albumId = 0;
        if (musicCursor != null && musicCursor.moveToFirst())
        {
            int albumIdIndex = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            albumId = musicCursor.getLong(albumIdIndex);
        }
        if(musicCursor != null)
        {
            musicCursor.close();
        }

        Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
        return ContentUris.withAppendedId(albumArtUri, albumId)+"";
    }
}
