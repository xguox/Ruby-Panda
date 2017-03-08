package me.xguox.rubyondroid.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by xguox on 01/03/2017.
 */

public class Topic implements Parcelable {

    private int id;
    private String title;
    @SerializedName("node_name")
    private String nodeName;
    @SerializedName("replies_count")
    private int repliesCount;
    @SerializedName("body_html")
    private String bodyHTML;
    @SerializedName("replied_at")
    private Date repliedAt;
    @SerializedName("created_at")
    private Date createdAt;
    private User user;

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNodeName() {
        return nodeName;
    }

    public int getRepliesCount() {
        return repliesCount;
    }

    public String getBodyHTML() {
        return bodyHTML;
    }

    public Date getRepliedAt() {
        return repliedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.nodeName);
        dest.writeInt(this.repliesCount);
        dest.writeString(this.bodyHTML);
        dest.writeLong(this.repliedAt != null ? this.repliedAt.getTime() : -1);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeParcelable(this.user, flags);
    }

    public Topic() {
    }

    protected Topic(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.nodeName = in.readString();
        this.repliesCount = in.readInt();
        this.bodyHTML = in.readString();
        long tmpRepliedAt = in.readLong();
        this.repliedAt = tmpRepliedAt == -1 ? null : new Date(tmpRepliedAt);
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel source) {
            return new Topic(source);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };
}

