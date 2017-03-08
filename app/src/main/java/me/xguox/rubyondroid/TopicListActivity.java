package me.xguox.rubyondroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import me.xguox.rubyondroid.adapter.TopicAdapter;
import me.xguox.rubyondroid.data.model.Topic;
import me.xguox.rubyondroid.network.TopicResponse;
import me.xguox.rubyondroid.network.TopicService;
import me.xguox.rubyondroid.utils.ApiUtil;
import me.xguox.rubyondroid.utils.OnItemClickListener;
import me.xguox.rubyondroid.utils.OnLoadMoreListener;
import retrofit2.Callback;

public class TopicListActivity extends AppCompatActivity {
    private static final String TAG = "TopicListActivity";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private TopicAdapter mAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeContainer;
    private TopicService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        mService = ApiUtil.getTopicService();
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
        mAdapter = new TopicAdapter(TopicListActivity.this, mRecyclerView);
        mAdapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Topic topic = mAdapter.getTopicList().get(position);

                Intent intent = new Intent(TopicListActivity.this, TopicActivity.class);
                intent.putExtra(TopicActivity.TOPIC, topic);
                TopicListActivity.this.startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    public void loadTopics(final boolean loadingMore) {
        int offset = loadingMore ? mLinearLayoutManager.getItemCount() : 0;
        mService.getTopics(offset).enqueue(new Callback<TopicResponse>() {
            @Override
            public void onResponse(retrofit2.Call<TopicResponse> call, retrofit2.Response<TopicResponse> response) {
                if (response.isSuccessful()) {
                    List<Topic> topicList = response.body().getTopics();

                    if (loadingMore) {
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
                Log.d("TopicListActivity", "error loading from API");
            }
        });
    }
}
