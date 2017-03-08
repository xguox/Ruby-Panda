package me.xguox.rubyondroid.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xguox on 04/03/2017.
 */

public class User implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.login);
        dest.writeString(this.avatarUrl);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.login = in.readString();
        this.avatarUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
