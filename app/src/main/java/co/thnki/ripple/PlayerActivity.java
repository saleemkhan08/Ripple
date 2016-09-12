package co.thnki.ripple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import co.thnki.ripple.fragments.PlayerFragment;

public class PlayerActivity extends AppCompatActivity
{
    public static final java.lang.String SONG_PATH = "songPath";
    public static final java.lang.String SONG_INDEX = "songIndex";
    private static final String PLAYER_FRAGMENT = "playerFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        PlayerFragment fragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        ArrayList<String> songPathList = new ArrayList<>();
        for (int i = 1; i < 7; i++)
        {
            songPathList.add("/mnt/sdcard/songs/sample" + i + ".mp3");
        }
        bundle.putStringArrayList(SONG_PATH, songPathList);
        bundle.putInt(SONG_INDEX, 3);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_player, fragment, PLAYER_FRAGMENT)
                .commit();
    }
}
