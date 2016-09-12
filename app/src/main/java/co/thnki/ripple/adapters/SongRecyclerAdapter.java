package co.thnki.ripple.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;

import co.thnki.ripple.R;
import co.thnki.ripple.SongViewHolder;
import co.thnki.ripple.models.Song;

public class SongRecyclerAdapter extends RecyclerView.Adapter<SongViewHolder>
{
    ArrayList<Song> mSongList;
    Context mContext;
    public SongRecyclerAdapter(Context context, ArrayList<Song> songList)
    {
        mSongList = songList;
        mContext = context;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.song_row,parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position)
    {
        Song song = mSongList.get(position);
        holder.mTitle.setText(song.getTrack());
        holder.mArtist.setText(song.getArtist());
        if (song.getImageUri() != null)
        {
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(holder.mImageView);
            Glide.with(mContext).load(Uri.parse(song.getImageUri())).placeholder(R.mipmap.sample).crossFade().into(imageViewTarget);
        }
        //holder.mImageView.setImageURI(Uri.parse(song.getImageUri()));
    }

    @Override
    public int getItemCount()
    {
        return mSongList.size();
    }
}
