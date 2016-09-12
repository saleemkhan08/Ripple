package co.thnki.ripple;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SongViewHolder extends RecyclerView.ViewHolder
{
    @Bind(R.id.trackTitle)
    public TextView mTitle;

    @Bind(R.id.artist)
    public TextView mArtist;

    @Bind(R.id.albumArt)
    public ImageView mImageView;

    public View mItemView;
    public SongViewHolder(View itemView)
    {
        super(itemView);
        mItemView = itemView;
        ButterKnife.bind(this, itemView);
    }
}
