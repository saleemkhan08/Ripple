package co.thnki.ripple.utils;

import android.media.MediaMetadata;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class MediaMetadataWrapper
{
    MediaMetadata mMediaMetadata;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MediaMetadataWrapper()
    {
        mMediaMetadata = new MediaMetadata.Builder().build();

    }
}


