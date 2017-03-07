package me.xguox.rubyondroid.utils;

import me.xguox.rubyondroid.network.TopicService;
import me.xguox.rubyondroid.network.remote.RetrofitClient;

/**
 * Created by xguox on 07/03/2017.
 */

public class ApiUtil {
    public static final String BASE_URL = "https://ruby-china.org/api/v3/";

    public static TopicService getTopicService() {
        return RetrofitClient.getClient(BASE_URL).create(TopicService.class);
    }

}
