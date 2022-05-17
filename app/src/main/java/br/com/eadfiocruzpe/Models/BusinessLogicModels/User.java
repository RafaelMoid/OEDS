package br.com.eadfiocruzpe.Models.BusinessLogicModels;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user_id")
    private String mUserId;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("location")
    private String mLocation;
    @SerializedName("name")
    private String mName;

    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("updated_at")
    private String mUpdatedAt;


    public User(String userId, String email, String location, String name) {
        setUserId(userId);
        setEmail(email);
        setLocation(location);
        setName(name);
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

}