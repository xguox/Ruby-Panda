package me.xguox.rubyondroid;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by xguox on 23/02/2017.
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder>  {
    private List<Topic> mTopicList;
    private Context mContext;

    public TopicAdapter(Context context, List<Topic> topicList) {
        mContext = context;
        mTopicList = topicList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_item_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Topic topic = mTopicList.get(position);
        User user = topic.getUser();
        ViewHolder topicViewHolder = holder;
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mTopicList == null) {
            return 0;
        } else {
            return mTopicList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle, mRepliesCount, mNodeName, mAuthorName, mRepliedAt;
        private ImageView mAvatar;

        public ViewHolder(View v) {
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
        }
    }
}
