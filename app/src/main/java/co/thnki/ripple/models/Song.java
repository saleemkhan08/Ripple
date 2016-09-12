package co.thnki.ripple.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable
{
    private String track;
    private String artist;
    private String album;
    private String year;
    private String genre;
    private String tags;
    private int rippleCount;
    private long albumId;
    private String imageUri;
    private boolean playState;
    private String localPath;

    public String getTrack()
    {
        return track;
    }

    public void setTrack(String track)
    {
        this.track = track;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public String getAlbum()
    {
        return album;
    }

    public void setAlbum(String album)
    {
        this.album = album;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    public String getSongId()
    {
         String trackName = track.replaceAll("[^A-Za-z0-9]", "-");
        String artistName = artist.replaceAll("[^A-Za-z0-9]", "-");
        String songId = trackName + "_" + artistName;
        songId = songId.replaceAll("--+", "-");
        return songId.toUpperCase();
    }

    public String getAlbumRippleId()
    {
        String trackName = album.replaceAll("[^A-Za-z0-9]", "-");
        String artistName = artist.replaceAll("[^A-Za-z0-9]", "-");
        String songId = trackName + "_" + artistName;
        songId = songId.replaceAll("--+", "-");
        return songId.toUpperCase();
    }

    public String getArtistRippleId()
    {
        String artistName = artist.replaceAll("[^A-Za-z0-9]", "-");
        artistName = artistName.replaceAll("--+", "-");
        return artistName.toUpperCase();
    }

    public int getRippleCount()
    {
        return rippleCount;
    }

    public void setRippleCount(int rippleCount)
    {
        this.rippleCount = rippleCount;
    }

    public long getAlbumId()
    {
        return albumId;
    }

    public void setAlbumId(long albumId)
    {
        this.albumId = albumId;
    }

    public String getImageUri()
    {
        return imageUri;
    }

    public void setImageUri(String imageUri)
    {
        this.imageUri = imageUri;
    }

    public boolean isPlayState()
    {
        return playState;
    }

    public void setPlayState(boolean playState)
    {
        this.playState = playState;
    }

    public String getLocalPath()
    {
        return localPath;
    }

    public void setLocalPath(String localPath)
    {
        this.localPath = localPath;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.track);
        dest.writeString(this.artist);
        dest.writeString(this.album);
        dest.writeString(this.year);
        dest.writeString(this.genre);
        dest.writeString(this.tags);
        dest.writeInt(this.rippleCount);
        dest.writeLong(this.albumId);
        dest.writeString(this.imageUri);
        dest.writeString(this.localPath);
        dest.writeByte(playState ? (byte) 1 : (byte) 0);
    }

    public Song()
    {
    }

    protected Song(Parcel in)
    {
        this.track = in.readString();
        this.artist = in.readString();
        this.album = in.readString();
        this.year = in.readString();
        this.genre = in.readString();
        this.tags = in.readString();
        this.rippleCount = in.readInt();
        this.albumId = in.readLong();
        this.imageUri = in.readString();
        this.localPath = in.readString();
        this.playState = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>()
    {
        @Override
        public Song createFromParcel(Parcel source)
        {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size)
        {
            return new Song[size];
        }
    };
}
