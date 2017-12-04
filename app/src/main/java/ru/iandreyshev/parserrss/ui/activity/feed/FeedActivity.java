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
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;
import ru.iandreyshev.parserrss.presentation.view.feed.FeedView;
import ru.iandreyshev.parserrss.presentation.presenter.feed.FeedPresenter;
import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.ui.activity.article.ArticleActivity;
import ru.iandreyshev.parserrss.ui.activity.settings.SettingsActivity;
import ru.iandreyshev.parserrss.ui.adapter.FeedListAdapter;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

public class FeedActivity extends MvpAppCompatActivity implements FeedView, SwipeRefreshLayout.OnRefreshListener {
    @InjectPresenter
    FeedPresenter feedPresenter;
    @BindView(R.id.feed_name)
    TextView feedName;
    @BindView(R.id.feed_favorites_button)
    ImageButton favoritesButton;
    @BindView(R.id.feed_settings_button)
    ImageButton settingsButton;
    @BindView(R.id.feed_items_list)
    RecyclerView itemsList;
    @BindView(R.id.feed_refresh_layout)
    SwipeRefreshLayout swipeLayout;
    FeedListAdapter itemsAdapter;

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
    public void addArticle(IArticleInfo item) {
        itemsAdapter.add(item);
    }

    @Override
    public void addFeed(IFeedInfo feed) {

    }

    @Override
    public void clearFeed() {
        itemsAdapter.clear();
    }

    @Override
    public void onRefresh() {
        feedPresenter.onRefresh();
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
        itemsAdapter = new FeedListAdapter(this);
        itemsAdapter.setOnItemClickListener((View view, IArticleInfo item) -> {
            feedPresenter.onItemClick(item);
        });
        itemsList.setAdapter(itemsAdapter);
    }

    private void initButtonsListeners() {
        settingsButton.setOnClickListener(view -> {
            feedPresenter.onSettingsButtonClick();
        });
    }

    private void initRefreshListener() {
        swipeLayout.setOnRefreshListener(this);
    }
}
