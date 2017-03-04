package me.xguox.rubyondroid;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xguox on 03/03/2017.
 */

public class TopicsFetchr {
    private List<Topic> mTopicList;

    public List<Topic> getTopics() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://ruby-china.org/api/v3/topics.json").build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            parseTopicsJSON(responseData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mTopicList;
    }

    private void parseTopicsJSON(final String jsonData) {
        JsonObject jsonObject = new JsonParser().parse(jsonData).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("topics");
        Type listType = new TypeToken<List<Topic>>() {}.getType();
        mTopicList = new Gson().fromJson(jsonArray, listType);
    }

}
