package co.thnki.ripple.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class SongPlayerService extends Service
{
    private IBinder mBinder = new SongPlayerServiceBinder();

    public SongPlayerService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    public int getInt()
    {
        return 156;
    }
    public class SongPlayerServiceBinder extends Binder
    {
        public SongPlayerService getService()
        {
            return SongPlayerService.this;
        }
    }

}
