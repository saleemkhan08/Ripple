package co.thnki.ripple.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import static android.provider.Settings.canDrawOverlays;

public class ChatHeadService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;

    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();
        //
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        if(canDrawOverlays(this))
        {
            /*windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            chatHead = new ImageView(this);
            chatHead.setImageResource(R.mipmap.ic_launcher);

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = 0;
            params.y = 100;

            windowManager.addView(chatHead, params);*/
        }
        else
        {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("android.settings.action.MANAGE_OVERLAY_PERMISSION");
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null) windowManager.removeView(chatHead);
    }
}
