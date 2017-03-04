package me.xguox.rubyondroid;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xguox on 04/03/2017.
 */

public class User {
    private int id;
    private String name;
    private String login;

    @SerializedName("avatar_url")
    private String avatarUrl;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
