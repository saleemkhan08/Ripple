package co.thnki.ripple.services;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import co.thnki.ripple.models.Song;
import co.thnki.ripple.utils.ConnectivityUtil;

import static co.thnki.ripple.receivers.MusicStateReceiver.CURRENT_SONG;

public class RippleService extends Service
{
    public static final String ACTION = "action";
    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String TRACK = "track";
    public static final String PLAY_STATE = "playstate";
    private static final String ARTIST_RIPPLES = "artistRipples";
    private static final String SONG_ALBUM_ART = "songAlbumArts";
    private static final String SONG_LIST = "songList";
    private Song mSong;

    public RippleService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (ConnectivityUtil.isConnected())
        {
            Log.d("MusicStateReceiver", "Service onStartCommand : " + intent);
            if (intent != null && intent.hasExtra(CURRENT_SONG))
            {
                mSong = intent.getParcelableExtra(CURRENT_SONG);
                final String albumArt = mSong.getImageUri();
                Log.d("MusicStateReceiver", "albumArt : " + albumArt);

                if (albumArt != null && !albumArt.isEmpty())
                {
                    final StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReference().child(SONG_ALBUM_ART).child(mSong.getSongId());

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            Log.d("MusicStateReceiver", "Download URI Available : " + uri);
                            mSong.setImageUri(uri + "");
                            ripple(mSong);
                        }
                    });

                    storageReference.getDownloadUrl().addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            storageReference.putFile(Uri.parse(albumArt))
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                                    {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                        {
                                            Uri uri = taskSnapshot.getDownloadUrl();
                                            Log.d("MusicStateReceiver", "Uploaded to : " + uri);
                                            mSong.setImageUri(uri + "");
                                            ripple(mSong);
                                        }
                                    });
                        }
                    });
                }
            }
            else
            {
                Log.d("RippleService", "ServiceStopped");
                stopSelf();
            }
        }
        else
        {
            Log.d("RippleService", "ServiceStopped");
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    private void ripple(Song mSong)
    {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference songList = root.child(SONG_LIST);
        songList.child(mSong.getSongId()).setValue(mSong);
        DatabaseReference artistRipple = root.child(ARTIST_RIPPLES).child(mSong.getArtistRippleId());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null)
        {
            if (mSong.isPlayState())
            {
                artistRipple.child(user.getUid()).setValue(mSong);
            }
            else
            {
                artistRipple.child(user.getUid()).removeValue();
            }
        }
        Log.d("RippleService", "ServiceStopped");
        stopSelf();
    }
}
