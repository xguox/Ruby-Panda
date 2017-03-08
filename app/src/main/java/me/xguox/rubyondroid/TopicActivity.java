package me.xguox.rubyondroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import me.xguox.rubyondroid.data.model.Topic;
import me.xguox.rubyondroid.data.model.User;
import me.xguox.rubyondroid.network.TopicResponse;
import me.xguox.rubyondroid.network.TopicService;
import me.xguox.rubyondroid.utils.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicActivity extends AppCompatActivity {
    public static final String TOPIC = "Topic";
    private Topic mTopic;
    private User mAuthor;
    private TextView mTitle, mAuthorName, mRepliesCount, mNodeName, mCreatedAt;
    private ImageView mAvatar;
    private WebView mBodyWebView;
    private TopicService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        mService = ApiUtil.getTopicService();
        mTopic = this.getIntent().getParcelableExtra(TOPIC);

        mTitle = (TextView) findViewById(R.id.title);
        mAuthorName = (TextView) findViewById(R.id.author_name);
        mRepliesCount = (TextView) findViewById(R.id.replies_count);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mNodeName = (TextView) findViewById(R.id.node_name);
        mCreatedAt = (TextView) findViewById(R.id.created_at);
        mBodyWebView = (WebView) findViewById(R.id.topic_body_web_view);

        loadTopic();

        mAuthor  = mTopic.getUser();

        mTitle.setText(mTopic.getTitle());
        mRepliesCount.setText("" + mTopic.getRepliesCount());

        mAuthorName.setText(mAuthor.getLogin() + " • ");
        mNodeName.setText(mTopic.getNodeName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdf.format(mTopic.getCreatedAt());
        mCreatedAt.setText("创建于 • " + dateString);

        Picasso.with(this).load(mAuthor.getAvatarUrl()).into(mAvatar);
    }

    private void loadTopic() {
        Call<TopicResponse> call = mService.getTopic(mTopic.getId());

        call.enqueue(new Callback<TopicResponse>() {
            @Override
            public void onResponse(Call<TopicResponse> call, Response<TopicResponse> response) {
                TopicResponse topicJSONResponse = response.body();
                Topic topic = topicJSONResponse.getTopic();
                String cssForImageTag = "<style> .twemoji{width:20px;} a{color: #e9583f;}" +
                        " img{display: inline;height: auto;max-width: 100%;} " +
                        " *{word-wrap: break-word;}</style>";
                String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"front-ruby-china.css\" />"
                        + cssForImageTag + topic.getBodyHTML();
                mBodyWebView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);
            }

            @Override
            public void onFailure(Call<TopicResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
                Toast.makeText(TopicActivity.this, "无法连接到服务器", Toast.LENGTH_LONG).show();
            }
        });
    }

}
