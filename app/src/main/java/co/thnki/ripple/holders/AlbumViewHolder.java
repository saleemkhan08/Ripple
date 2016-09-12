package co.thnki.ripple.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thnki.ripple.R;

public class AlbumViewHolder extends RecyclerView.ViewHolder
{
    public View mView;
    @Bind(R.id.albumArt)
    public ImageView mAlbumArt;

    @Bind(R.id.albumName)
    public TextView mAlbumName;

    @Bind(R.id.artistName)
    public TextView mArtistName;

    public AlbumViewHolder(View view)
    {
        super(view);
        mView = view;
        ButterKnife.bind(this, view);
    }
}
