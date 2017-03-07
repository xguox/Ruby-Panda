package me.xguox.rubyondroid;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import me.xguox.rubyondroid.adapter.TopicAdapter;
import me.xguox.rubyondroid.data.model.Topic;
import me.xguox.rubyondroid.network.TopicResponse;
import me.xguox.rubyondroid.network.TopicService;
import me.xguox.rubyondroid.utils.ApiUtil;
import me.xguox.rubyondroid.utils.OnLoadMoreListener;
import retrofit2.Callback;

public class TopicsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private TopicAdapter mAdapter;
    private List<Topic> mTopicList;
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeContainer;
    private TopicService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        mService = ApiUtil.getTopicService();
        mTopicList = new ArrayList<>();
        setupRecyclerView();
        mAdapter.setLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mAdapter.setProgressMore(true);
                loadTopics(true);
            }
        });
        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTopics(false);
            }
        });

        loadTopics(false);
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.topics_list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // mRecyclerView.setHasFixedSize(true);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new TopicAdapter(TopicsActivity.this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void loadTopics(final boolean loadingMore) {
        int offset = mLinearLayoutManager.getItemCount();
        mService.getTopics(offset).enqueue(new Callback<TopicResponse>() {
            @Override
            public void onResponse(retrofit2.Call<TopicResponse> call, retrofit2.Response<TopicResponse> response) {
                if (response.isSuccessful()) {
                    List<Topic> topicList = response.body().getTopics();

                    if (loadingMore) {
                        mTopicList.clear();
                        mAdapter.setProgressMore(false);
                        mAdapter.addItemMore(topicList);
                        mAdapter.setMoreLoading(false);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        mSwipeContainer.setRefreshing(false);
                        mAdapter.addAll(topicList);
                    }
                } else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TopicResponse> call, Throwable t) {
                Log.d("TopicsActivity", "error loading from API");
            }
        });
    }
}
