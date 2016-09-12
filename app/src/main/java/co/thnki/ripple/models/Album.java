package co.thnki.ripple.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable
{
    private String albumArtUri;
    private String albumName;
    private String albumArtist;
    private String albumYear;
    private String albumLanguage;
    private long albumId;


    public String getAlbumArtUri()
    {
        return albumArtUri;
    }

    public void setAlbumArtUri(String albumArtUri)
    {
        this.albumArtUri = albumArtUri;
    }

    public String getAlbumName()
    {
        return albumName+"";
    }

    public void setAlbumName(String albumName)
    {
        this.albumName = albumName;
    }

    public String getAlbumArtist()
    {
        return albumArtist+"";
    }

    public void setAlbumArtist(String albumArtist)
    {
        this.albumArtist = albumArtist;
    }

    public String getAlbumYear()
    {
        return albumYear;
    }

    public void setAlbumYear(String albumYear)
    {
        this.albumYear = albumYear;
    }

    public String getAlbumLanguage()
    {
        return albumLanguage;
    }

    public void setAlbumLanguage(String albumLanguage)
    {
        this.albumLanguage = albumLanguage;
    }

    public long getAlbumId()
    {
        return albumId;
    }

    public void setAlbumId(long albumId)
    {
        this.albumId = albumId;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.albumArtUri);
        dest.writeString(this.albumName);
        dest.writeString(this.albumArtist);
        dest.writeString(this.albumYear);
        dest.writeString(this.albumLanguage);
        dest.writeLong(this.albumId);
    }

    public Album()
    {
    }

    protected Album(Parcel in)
    {
        this.albumArtUri = in.readString();
        this.albumName = in.readString();
        this.albumArtist = in.readString();
        this.albumYear = in.readString();
        this.albumLanguage = in.readString();
        this.albumId = in.readLong();
    }

    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>()
    {
        @Override
        public Album createFromParcel(Parcel source)
        {
            return new Album(source);
        }

        @Override
        public Album[] newArray(int size)
        {
            return new Album[size];
        }
    };
}