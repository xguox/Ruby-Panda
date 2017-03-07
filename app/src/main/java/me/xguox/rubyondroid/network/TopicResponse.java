package me.xguox.rubyondroid.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.xguox.rubyondroid.data.model.Topic;

/**
 * Created by xguox on 07/03/2017.
 */

public class TopicResponse {

    @SerializedName("topics")
    @Expose
    private List<Topic> topics = null;

    public List<Topic> getTopics() {
        return topics;
    }
}
