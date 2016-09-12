package co.thnki.ripple.models;

import com.facebook.Profile;

public class Users
{
    private String lastName;
    private String firstName;
    private String photoUrl;
    public String userId;

    public Users(String email, String name, String photoUrl, String userId)
    {
        this.firstName = email;
        this.lastName = name;
        this.photoUrl = photoUrl;
        this.userId = userId;
    }

    public Users(Profile currentProfile)
    {
        this.firstName = currentProfile.getFirstName();
        this.lastName = currentProfile.getLastName();
        this.photoUrl = currentProfile.getProfilePictureUri(200,200)+"";
        this.userId = currentProfile.getId();
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getPhotoUrl()
    {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl)
    {
        this.photoUrl = photoUrl;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
}
