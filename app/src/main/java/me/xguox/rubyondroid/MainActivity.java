package me.xguox.rubyondroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        setupRecyclerView();

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchTopicsTask().execute();
            }
        });
        new FetchTopicsTask().execute();
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.topics_list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private class FetchTopicsTask extends AsyncTask<Void,Void,List<Topic>> {

        @Override
        protected List<Topic> doInBackground(Void... params) {
            List<Topic> topicList = new TopicsFetchr().getTopics();
            return topicList;
        }

        @Override
        protected void onPostExecute(List<Topic> topicList) {
            progressBar.setVisibility(View.GONE);
            mSwipeContainer.setRefreshing(false);
            mAdapter = new TopicAdapter(MainActivity.this, topicList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
