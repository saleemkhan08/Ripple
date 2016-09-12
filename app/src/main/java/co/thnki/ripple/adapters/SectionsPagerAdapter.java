package co.thnki.ripple.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import co.thnki.ripple.fragments.AlbumFragment;
import co.thnki.ripple.fragments.ArtistFragment;
import co.thnki.ripple.fragments.SongListFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter
{

    private AlbumFragment mAlbumFragment;
    private SongListFragment mSongFragment;
    private ArtistFragment mArtistFragment;

    public SectionsPagerAdapter(FragmentManager fm)
    {
        super(fm);
        mAlbumFragment = new AlbumFragment();
        mSongFragment = new SongListFragment();
        mArtistFragment = new ArtistFragment();
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0 : return mSongFragment;
            case 1 : return mArtistFragment;
            default: return mAlbumFragment;
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "Songs";
            case 1:
                return "Artists";
            case 2:
                return "Albums";
        }
        return null;
    }
}
