package co.thnki.ripple.adapters;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;

import co.thnki.ripple.R;
import co.thnki.ripple.holders.AlbumViewHolder;
import co.thnki.ripple.models.Album;

import static co.thnki.ripple.Ripples.toast;

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumViewHolder>
{
    private final ArrayList<Album> mAlbumList;
    private final Activity mActivity;

    public AlbumRecyclerViewAdapter(ArrayList<Album> albumList, Activity activity)
    {
        mAlbumList = albumList;
        mActivity = activity;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AlbumViewHolder holder, int position)
    {
        final Album album = mAlbumList.get(position);
        Log.d("album", album.getAlbumName() + " : " + album.getAlbumArtist() + " : " + album.getAlbumArtUri());
        Log.d("album", holder + " : " + holder.mAlbumName + " : " + holder.mArtistName);

        holder.mAlbumName.setText(album.getAlbumName());
        holder.mArtistName.setText(album.getAlbumArtist());

        if (album.getAlbumArtUri() != null)
        {
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(holder.mAlbumArt);
            Glide.with(mActivity).load(Uri.parse(album.getAlbumArtUri())).placeholder(R.mipmap.sample).crossFade().into(imageViewTarget);
        }
        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toast(album.getAlbumName());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mAlbumList.size();
    }
}
