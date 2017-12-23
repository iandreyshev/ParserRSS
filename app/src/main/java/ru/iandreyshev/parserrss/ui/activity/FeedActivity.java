package ru.iandreyshev.parserrss.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import ru.iandreyshev.parserrss.ui.adapter.RssTabsAdapter;
import ru.iandreyshev.parserrss.ui.adapter.IOnArticleClickListener;
import ru.iandreyshev.parserrss.ui.adapter.IOnRefreshFeedListener;
import ru.iandreyshev.parserrss.ui.fragment.AddingRssDialog;
import ru.iandreyshev.parserrss.ui.fragment.IOnSubmitAddingListener;

import com.arellomobile.mvp.presenter.InjectPresenter;

public class FeedActivity extends BaseActivity implements IFeedView {
    private static final String TAG = "FeedActivity";
    private static final String ADD_DIALOG_TAG = "AddingRssDialog";

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
    public void insertRss(final Rss rss) {
        mTabsAdapter.add(rss.getFeed());
        mPager.setCurrentItem(mTabsAdapter.getCount());
        updateMenuState();
    }

    @Override
    public void updateArticles(final Rss rss) {
        mTabsAdapter.update(rss);
        mTabsAdapter.startRefresh(rss.getFeed(), false);
        updateMenuState();
    }

    @Override
    public void removeRss(final RssFeed feed) {
        mTabsAdapter.remove(feed);
        updateMenuState();
    }

    @Override
    public void openArticle(RssArticle article) {
        final Intent intent = ArticleActivity.getIntent(this)
                .putExtra(ArticleActivity.ARTICLE_BOUND_KEY, article)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
    }

    @Override
    public void openAddingRssDialog() {
        final AddingRssDialog dialog = new AddingRssDialog()
                .setOnSubmitListener(new FeedListener());
        dialog.show(getSupportFragmentManager(), ADD_DIALOG_TAG);
    }

    @Override
    public void startProgressBar(boolean isStart) {
        mProgressBar.setVisibility(isStart ? View.VISIBLE : View.GONE);
    }

    @Override
    public void startRefresh(final RssFeed feed, boolean isStart) {
        mTabsAdapter.startRefresh(feed, isStart);
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
        final RssFeed feed = mTabsAdapter.getFeed(mPager.getCurrentItem());

        switch (item.getItemId()) {
            case R.id.feed_options_add:
                openAddingRssDialog();
                break;
            case R.id.feed_options_info:
                mFeedPresenter.openRssInfo(feed);
                break;
            case R.id.feed_options_delete:
                mFeedPresenter.onDeleteRss(feed);
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

    private void initToolbar() {
        mToolbar.setTitle(getResources().getString(R.string.feed_title));
        setSupportActionBar(mToolbar);
        startProgressBar(false);
    }

    private void initTabsView() {
        Log.e(TAG, "Create adapter");
        final FeedListener listener = new FeedListener();

        mTabsAdapter = new RssTabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setOnItemClickListener(listener);
        mTabsAdapter.setOnRefreshListener(listener);

        mPager.setAdapter(mTabsAdapter);
        mTabs.setupWithViewPager(mPager, true);
    }

    private class FeedListener implements IOnRefreshFeedListener, IOnArticleClickListener, IOnSubmitAddingListener {
        @Override
        public void onItemClick(final RssArticle item) {
            mFeedPresenter.openArticle(item);
        }

        @Override
        public void onRefresh(final RssFeed feed) {
            mFeedPresenter.onUpdateRss(feed);
        }

        @Override
        public void onSubmit(final DialogInterface dialogInterface, final String url) {
            mFeedPresenter.onSubmitInsertRss(url);
        }
    }
}
