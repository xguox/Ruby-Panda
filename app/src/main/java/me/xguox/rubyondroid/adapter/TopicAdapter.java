package me.xguox.rubyondroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.xguox.rubyondroid.utils.OnItemClickListener;
import me.xguox.rubyondroid.utils.OnLoadMoreListener;
import me.xguox.rubyondroid.R;
import me.xguox.rubyondroid.data.model.Topic;
import me.xguox.rubyondroid.data.model.User;

/**
 * Created by xguox on 23/02/2017.
 */

public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Topic> mTopicList;
    private Context mContext;
    private OnLoadMoreListener loadMoreListener;
    private OnItemClickListener mClickListener;
    private boolean isMoreLoading = false;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    public final int TYPE_TOPIC = 0;
    public final int TYPE_LOADING = 1;

    public TopicAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mTopicList = new ArrayList<>();
        setRecyclerView(recyclerView);
    }

    public List<Topic> getTopicList() {
        return mTopicList;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setClickListener(OnItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setRecyclerView(RecyclerView view) {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) view.getLayoutManager();

        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isMoreLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    if (loadMoreListener != null) {
                        loadMoreListener.onLoadMore();
                    }
                    isMoreLoading = true;
                }
            }
        });
    }

    public void addAll(List<Topic> list) {
        mTopicList.clear();
        mTopicList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItemMore(List<Topic> list) {
        mTopicList.addAll(list);
        notifyItemRangeChanged(0, mTopicList.size());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mTopicList == null) {
            return 0;
        } else {
            return mTopicList.size();
        }
    }

    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {
            mTopicList.add(null);
            notifyItemInserted(mTopicList.size() - 1);
        } else {
            mTopicList.remove(mTopicList.size() - 1);
            notifyItemRemoved(mTopicList.size());
        }
    }

    public void setMoreLoading(boolean isMoreLoading) {
        this.isMoreLoading = isMoreLoading;
    }

    @Override
    public int getItemViewType(int position) {
        return mTopicList.get(position) == null ? TYPE_LOADING : TYPE_TOPIC;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        if (viewType == TYPE_TOPIC) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.topic_item_card, parent, false);
            // set the view's size, margins, paddings and layout parameters
            return new TopicViewHolder(v);
        } else if (viewType == TYPE_LOADING) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loading_item, parent, false);
            return new LoadingViewHolder(v);
        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopicViewHolder) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            Topic topic = mTopicList.get(position);
            User user = topic.getUser();
            TopicViewHolder topicViewHolder = (TopicViewHolder) holder;
            topicViewHolder.mTitle.setText(topic.getTitle());
            topicViewHolder.mRepliesCount.setText("" + topic.getRepliesCount());
            topicViewHolder.mNodeName.setText(" • " + topic.getNodeName());
            topicViewHolder.mAuthorName.setText(user.getLogin());
            if (topic.getRepliedAt() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = sdf.format(topic.getRepliedAt());
                topicViewHolder.mRepliedAt.setText("最后回复于 • " + dateString);
            }
            Picasso.with(mContext).load(user.getAvatarUrl()).resize(60, 60).into(topicViewHolder.mAvatar);
        }
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle, mRepliesCount, mNodeName, mAuthorName, mRepliedAt;
        private ImageView mAvatar;

        public TopicViewHolder(View v) {
            super(v);
            mRepliesCount = (TextView) itemView.findViewById(R.id.replies_count);
            mRepliesCount.setTextColor(Color.parseColor("#FFFFFF"));
            mRepliesCount.setBackgroundColor(Color.parseColor("#f4a89b"));

            mNodeName = (TextView) itemView.findViewById(R.id.node_name);
            mNodeName.setTextColor(Color.parseColor("#ef806d"));

            mAuthorName = (TextView) itemView.findViewById(R.id.author_name);
            mRepliedAt = (TextView) itemView.findViewById(R.id.replied_at);
            mRepliedAt.setTextColor(Color.parseColor("#aaaaaa"));
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mTitle.setTextColor(Color.parseColor("#111111"));
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mClickListener.onItemClick(position);
                }
            });
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loadingMore);
        }
    }
}
