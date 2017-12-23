package ru.iandreyshev.parserrss.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import butterknife.BindView;

import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssArticle;
import ru.iandreyshev.parserrss.models.rss.RssFeed;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;
import ru.iandreyshev.parserrss.presentation.presenter.FeedPresenter;
import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.ui.adapter.FeedTabsAdapter;
import ru.iandreyshev.parserrss.ui.fragment.AddRssDialog;
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener;
import ru.iandreyshev.parserrss.ui.listeners.IOnSubmitAddRssListener;
import ru.iandreyshev.parserrss.ui.listeners.IOnUpdateRssListener;

import com.arellomobile.mvp.presenter.InjectPresenter;

public class FeedActivity extends BaseActivity
        implements IFeedView, IOnArticleClickListener, IOnSubmitAddRssListener, IOnUpdateRssListener {
    private static final String TAG = FeedActivity.class.getName();

    @InjectPresenter
    FeedPresenter mFeedPresenter;

    @BindView(R.id.feed_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.feed_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.feed_tabs_layout)
    TabLayout mTabs;
    @BindView(R.id.feed_view_pager)
    ViewPager mPager;

    private FeedTabsAdapter mTabsAdapter;
    private MenuItem mAddMenuItem;
    private MenuItem mInfoMenuItem;
    private MenuItem mDeleteMenuItem;

    public static Intent getIntent(final Context context) {
        return new Intent(context, FeedActivity.class);
    }

    @Override
    public void insertRss(final Rss rss) {
        mTabsAdapter.insert(rss);
        mPager.setCurrentItem(mTabsAdapter.getCount());
        updateMenuState();
    }

    @Override
    public void updateArticles(final Rss rss) {
        mTabsAdapter.update(rss);
        updateMenuState();
    }

    @Override
    public void removeRss(final Rss rss) {
        mTabsAdapter.remove(rss);
        updateMenuState();
    }

    @Override
    public void openArticle(final RssArticle article) {
        final Intent intent = ArticleActivity.getIntent(this)
                .putExtra(ArticleActivity.ARTICLE_BOUND_KEY, (Parcelable) article)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
    }

    @Override
    public void openAddingRssDialog() {
        AddRssDialog.show(getSupportFragmentManager());
    }

    @Override
    public void startProgressBar(boolean isStart) {
        mProgressBar.setVisibility(isStart ? View.VISIBLE : View.GONE);
    }

    @Override
    public void startUpdate(final RssFeed feed, boolean isStart) {
    }

    @Override
    public void openRssInfo(final RssFeed feed) {
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.feed_options_menu, menu);
        mAddMenuItem = menu.findItem(R.id.feed_options_add);
        mInfoMenuItem = menu.findItem(R.id.feed_options_info);
        mDeleteMenuItem = menu.findItem(R.id.feed_options_delete);

        updateMenuState();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Rss currentRss = mTabsAdapter.getRss(mPager.getCurrentItem());

        switch (item.getItemId()) {
            case R.id.feed_options_add:
                openAddingRssDialog();
                break;
            case R.id.feed_options_info:
                mFeedPresenter.openRssInfo(currentRss.getFeed());
                break;
            case R.id.feed_options_delete:
                mFeedPresenter.onDeleteRss(currentRss);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArticleClick(final RssArticle article) {
        mFeedPresenter.openArticle(article);
    }

    @Override
    public void onSubmitAddRss(final String url) {
        mFeedPresenter.onSubmitInsertRss(url);
    }

    @Override
    public void onUpdateRss(final RssFeed feed) {
        Log.e(TAG, feed.getUrl());
        mFeedPresenter.onUpdateRss(feed);
    }

    public void updateMenuState() {
        if (mAddMenuItem != null) {
            mAddMenuItem.setEnabled(mProgressBar.getVisibility() != View.VISIBLE);
        }
        if (mInfoMenuItem != null) {
            mInfoMenuItem.setEnabled(mTabsAdapter.getCount() > 0);
        }
        if (mDeleteMenuItem != null) {
            mDeleteMenuItem.setEnabled(mTabsAdapter.getCount() > 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_feed);

        ButterKnife.bind(this);

        initToolbar();
        initTabsView();
    }

    private void initToolbar() {
        mToolbar.setTitle(getResources().getString(R.string.feed_title));
        setSupportActionBar(mToolbar);
        startProgressBar(false);
    }

    private void initTabsView() {
        mTabsAdapter = new FeedTabsAdapter(getSupportFragmentManager());
        mPager.setAdapter(mTabsAdapter);
        mTabs.setupWithViewPager(mPager, true);
    }
}
