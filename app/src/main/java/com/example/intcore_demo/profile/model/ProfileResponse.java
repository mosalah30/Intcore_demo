package com.example.intcore_demo.profile.model;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {


    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
