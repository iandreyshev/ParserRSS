package ru.iandreyshev.parserrss.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import butterknife.BindView;

import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.models.rss.IRssArticle;
import ru.iandreyshev.parserrss.models.rss.IRssFeed;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;
import ru.iandreyshev.parserrss.presentation.presenter.FeedPresenter;
import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.ui.adapter.RssTabsAdapter;
import ru.iandreyshev.parserrss.ui.adapter.IOnArticleClickListener;
import ru.iandreyshev.parserrss.ui.adapter.IOnRefreshListener;
import ru.iandreyshev.parserrss.ui.fragment.AddFeedDialog;
import ru.iandreyshev.parserrss.ui.fragment.IOnSubmitAddingListener;

import com.arellomobile.mvp.presenter.InjectPresenter;

public class FeedActivity extends BaseActivity implements IFeedView, IOnSubmitAddingListener {
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

    private RssTabsAdapter mTabsAdapter;
    private MenuItem mAddMenuItem;
    private MenuItem mInfoMenuItem;
    private MenuItem mDeleteMenuItem;

    public static Intent getIntent(final Context context) {
        return new Intent(context, FeedActivity.class);
    }

    @Override
    public void insertFeed(final Rss rss) {
        mTabsAdapter.add(rss);
        mPager.setCurrentItem(mTabsAdapter.getCount());
        updateMenuState();
    }

    @Override
    public void updateFeedList(final Rss rss) {
        updateMenuState();
        mTabsAdapter.update(rss);
        mTabsAdapter.startRefresh(rss.getFeed(), false);
    }

    public void removeRss(final Rss rss) {
        mTabsAdapter.remove(rss);
        updateMenuState();
    }

    @Override
    public void openArticle(IRssArticle article) {
        final Intent intent = ArticleActivity.getIntent(this)
                .putExtra(ArticleActivity.ARTICLE_BOUND_KEY, article)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
    }

    @Override
    public void openAddingFeedDialog() {
        final AddFeedDialog dialog = new AddFeedDialog();
        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void startProgressBar(boolean isStart) {
        mProgressBar.setVisibility(isStart ? View.VISIBLE : View.GONE);
    }

    @Override
    public void startRefresh(IRssFeed feed, boolean isStart) {
        mTabsAdapter.startRefresh(feed, isStart);
    }

    @Override
    public void openInfo(IRssFeed feed) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_options_menu, menu);
        mAddMenuItem = menu.findItem(R.id.feed_options_add);
        mInfoMenuItem = menu.findItem(R.id.feed_options_info);
        mDeleteMenuItem = menu.findItem(R.id.feed_options_delete);

        updateMenuState();

        return true;
    }

    @Override
    public void onSubmit(DialogInterface dialogInterface, String url) {
        mFeedPresenter.onSubmitAddingFeed(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feed_options_add:
                openAddingFeedDialog();
                break;
            case R.id.feed_options_info:
                mFeedPresenter.onOpenInfo(getCurrentRss().getFeed());
                break;
            case R.id.feed_options_delete:
                mFeedPresenter.onDeleteFeed(getCurrentRss());
                break;
        }

        return super.onOptionsItemSelected(item);
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

    private Rss getCurrentRss() {
        return mTabsAdapter.getRss(mPager.getCurrentItem());
    }

    private void initToolbar() {
        mToolbar.setTitle(getResources().getString(R.string.feed_title));
        setSupportActionBar(mToolbar);
        startProgressBar(false);
    }

    private void initTabsView() {
        final FeedListener listener = new FeedListener();

        mTabsAdapter = new RssTabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setOnItemClickListener(listener);
        mTabsAdapter.setOnRefreshListener(listener);

        mPager.setAdapter(mTabsAdapter);
        mTabs.setupWithViewPager(mPager, true);
    }

    private class FeedListener implements IOnRefreshListener, IOnArticleClickListener {
        @Override
        public void onItemClick(IRssArticle item) {
            mFeedPresenter.onArticleClick(item);
        }

        @Override
        public void onRefresh(IRssFeed feed) {
            mFeedPresenter.onRefreshFeed(feed);
        }
    }
}
