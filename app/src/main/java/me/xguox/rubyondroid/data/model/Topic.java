package me.xguox.rubyondroid.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by xguox on 01/03/2017.
 */

public class Topic {

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
}

