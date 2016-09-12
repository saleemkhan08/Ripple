package co.thnki.ripple.adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;
import java.util.ArrayList;

import co.thnki.ripple.R;
import co.thnki.ripple.SongViewHolder;
import co.thnki.ripple.models.Play;
import co.thnki.ripple.singletons.Otto;

public class SongPathRecyclerAdapter extends RecyclerView.Adapter<SongViewHolder>
{
    ArrayList<String> mSongList;
    Context mContext;
    private MediaMetadataRetriever mMetaDataRetriever;

    public SongPathRecyclerAdapter(Context context)
    {
        mContext = context;
        mMetaDataRetriever = new MediaMetadataRetriever();
    }

    public void setSongList(ArrayList<String> songList)
    {
        mSongList = songList;
        notifyDataSetChanged();
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.song_row, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, final int position)
    {
        String songPath = mSongList.get(position);
        mMetaDataRetriever.setDataSource(songPath);

        String title = mMetaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        if(title == null || title.isEmpty())
        {
            title = mMetaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            File file = new File(songPath);
            title += " - "+file.getName().replace(".mp3", "");
        }

        holder.mTitle.setText(title);
        holder.mArtist.setText(mMetaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

        byte[] art = mMetaDataRetriever.getEmbeddedPicture();
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(holder.mImageView);
        Glide.with(mContext).load(art).placeholder(R.mipmap.sample).crossFade().into(imageViewTarget);

        holder.mItemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Otto.post(new Play(position));
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mSongList.size();
    }
}
