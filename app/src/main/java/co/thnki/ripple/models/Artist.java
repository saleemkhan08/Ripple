package co.thnki.ripple.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable
{
    private String artistName;
    private String artistPhotoUrl;
    private String artistCountry;
    private String languages;//csv

    public String getArtistName()
    {
        return artistName;
    }

    public void setArtistName(String artistName)
    {
        this.artistName = artistName;
    }

    public String getArtistPhotoUrl()
    {
        return artistPhotoUrl;
    }

    public void setArtistPhotoUrl(String artistPhotoUrl)
    {
        this.artistPhotoUrl = artistPhotoUrl;
    }

    public String getArtistCountry()
    {
        return artistCountry;
    }

    public void setArtistCountry(String artistCountry)
    {
        this.artistCountry = artistCountry;
    }

    public String getLanguages()
    {
        return languages;
    }

    public void setLanguages(String languages)
    {
        this.languages = languages;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.artistName);
        dest.writeString(this.artistPhotoUrl);
        dest.writeString(this.artistCountry);
        dest.writeString(this.languages);
    }

    public Artist()
    {
    }

    protected Artist(Parcel in)
    {
        this.artistName = in.readString();
        this.artistPhotoUrl = in.readString();
        this.artistCountry = in.readString();
        this.languages = in.readString();
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>()
    {
        @Override
        public Artist createFromParcel(Parcel source)
        {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size)
        {
            return new Artist[size];
        }
    };
}
