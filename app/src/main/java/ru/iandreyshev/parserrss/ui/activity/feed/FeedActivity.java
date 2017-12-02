package ru.iandreyshev.parserrss.ui.activity.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;

import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.models.feed.Article;
import ru.iandreyshev.parserrss.models.feed.IFeedItem;
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
    ListView itemsList;
    @BindView(R.id.feed_refresh_layout)
    SwipeRefreshLayout swipeLayout;
    FeedListAdapter itemsAdapter;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, FeedActivity.class);

        return intent;
    }

    @Override
    public void setFeedName(final String value) {
        feedName.setText(value);
    }

    @Override
    public void openSettings() {
        final Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
    }

    @Override
    public void openArticle() {
        final Intent intent = new Intent(this, ArticleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
    }

    @Override
    public void addItem(Article item) {
        itemsAdapter.add(item);
    }

    @Override
    public void clearItems() {
        itemsAdapter.clear();
    }

    @Override
    public void onRefresh() {
        feedPresenter.onRefresh();
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
        if (itemsAdapter == null) {
            itemsAdapter = new FeedListAdapter(this, R.layout.feed_item);
        }
        itemsList.setAdapter(itemsAdapter);
        itemsList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            IFeedItem item = (IFeedItem) parent.getItemAtPosition(position);
            feedPresenter.onItemClick(item.getId());
        });
    }

    private void initButtonsListeners() {
        settingsButton.setOnClickListener(view -> feedPresenter.onSettingsButtonClick());
    }

    private void initRefreshListener() {
        swipeLayout.setOnRefreshListener(this);
    }
}
