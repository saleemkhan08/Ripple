package co.thnki.ripple.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import co.thnki.ripple.Ripples;

public class ConnectivityUtil
{
    public static boolean isConnected()
    {
        ConnectivityManager connectivity = (ConnectivityManager) Ripples.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                return true;
            }
        }
        return false;
    }
}

