package ru.iandreyshev.parserrss.ui.activity.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;

import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.Feed;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;
import ru.iandreyshev.parserrss.presentation.view.feed.IFeedView;
import ru.iandreyshev.parserrss.presentation.presenter.feed.FeedPresenter;
import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.ui.activity.BaseActivity;
import ru.iandreyshev.parserrss.ui.activity.article.ArticleActivity;
import ru.iandreyshev.parserrss.ui.activity.settings.SettingsActivity;
import ru.iandreyshev.parserrss.ui.adapter.FeedListAdapter;
import ru.iandreyshev.parserrss.ui.adapter.IOnItemClickListener;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

public class FeedActivity extends BaseActivity implements IFeedView {
    @InjectPresenter
    FeedPresenter feedPresenter;

    @BindView(R.id.feed_name)
    TextView feedName;
    @BindView(R.id.feed_settings_button)
    ImageButton settingsButton;
    @BindView(R.id.feed_items_list)
    RecyclerView itemsList;
    @BindView(R.id.feed_refresh_layout)
    SwipeRefreshLayout swipeLayout;

    FeedListAdapter itemsAdapter;
    FeedListListener feedListener = new FeedListListener();

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, FeedActivity.class);

        return intent;
    }

    @Override
    public void openSettings() {
        final Intent intent = SettingsActivity.getIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
    }

    @Override
    public void openArticle() {
        final Intent intent = ArticleActivity.getIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void updateFeedList(IFeedInfo feed, List<IArticleInfo> newList) {
        itemsAdapter.setItems(newList);
        setRefreshing(false);
    }

    @Override
    public void setRefreshing(boolean isRefresh) {
        swipeLayout.setRefreshing(isRefresh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ButterKnife.bind(this);

        initItemsList();
        initButtonsListeners();
        initRefreshListener();
    }

    private void initItemsList() {
        if (itemsAdapter != null) {
            return;
        }
        itemsAdapter = new FeedListAdapter(this);
        itemsAdapter.setOnItemClickListener(feedListener);
        itemsList.setAdapter(itemsAdapter);
    }

    private void initButtonsListeners() {
        settingsButton.setOnClickListener(view -> {
            feedPresenter.onSettingsButtonClick();
        });
    }

    private void initRefreshListener() {
        swipeLayout.setOnRefreshListener(feedListener);
    }

    private class FeedListListener implements SwipeRefreshLayout.OnRefreshListener, IOnItemClickListener<IArticleInfo> {
        @Override
        public void onRefresh() {
            feedPresenter.onRefresh(new Feed(0, "", ""));
        }

        @Override
        public void onItemClick(View view, IArticleInfo item) {
            feedPresenter.onItemClick(item);
        }
    }
}
