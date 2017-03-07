package me.xguox.rubyondroid.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by xguox on 07/03/2017.
 */

public interface TopicService {
    @GET("topics.json?limit=35")
    Call<TopicResponse> getTopics(@Query("offset") int offset);
}
