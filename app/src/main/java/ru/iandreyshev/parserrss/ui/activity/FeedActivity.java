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
import ru.iandreyshev.parserrss.models.rss.ViewRss;
import ru.iandreyshev.parserrss.models.rss.ViewRssArticle;
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
        implements IFeedView, IOnArticleClickListener, IOnSubmitAddRssListener {
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
    private MenuItem mMenuAddButton;
    private MenuItem mMenuInfoButton;
    private MenuItem mMenuDeleteButton;

    public static Intent getIntent(final Context context) {
        return new Intent(context, FeedActivity.class);
    }

    @Override
    public void insertRss(final ViewRss rss) {
        mTabsAdapter.insert(rss);
        mPager.setCurrentItem(mTabsAdapter.getCount());
    }

    @Override
    public void removeRss(final ViewRss rss) {
        mTabsAdapter.remove(rss);
    }

    @Override
    public void openArticle(final ViewRssArticle article) {
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
    public void enableAddingButton(boolean isEnable) {
        if (mMenuAddButton != null) {
            mMenuAddButton.setEnabled(isEnable);
        }
    }

    @Override
    public void enableDeleteButton(boolean isEnable) {
        if (mMenuDeleteButton != null) {
            mMenuDeleteButton.setEnabled(isEnable);
        }
    }

    @Override
    public void enableInfoButton(boolean isEnable) {
        if (mMenuInfoButton != null) {
            mMenuInfoButton.setEnabled(isEnable);
        }
    }

    @Override
    public void openRssInfo(final ViewRss feed) {
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.feed_options_menu, menu);
        mMenuAddButton = menu.findItem(R.id.feed_options_add);
        mMenuInfoButton = menu.findItem(R.id.feed_options_info);
        mMenuDeleteButton = menu.findItem(R.id.feed_options_delete);

        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        boolean isMenuOpened = super.onMenuOpened(featureId, menu);

        mMenuInfoButton.setEnabled(mTabsAdapter.getCount() > 0);
        mMenuDeleteButton.setEnabled(mTabsAdapter.getCount() > 0);

        return isMenuOpened;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final ViewRss currentRss = mTabsAdapter.getRss(mPager.getCurrentItem());

        switch (item.getItemId()) {
            case R.id.feed_options_add:
                openAddingRssDialog();
                break;
            case R.id.feed_options_info:
                mFeedPresenter.openRssInfo(currentRss);
                break;
            case R.id.feed_options_delete:
                mFeedPresenter.onDeleteRss(currentRss);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArticleClick(final ViewRssArticle article) {
        mFeedPresenter.openArticle(article);
    }

    @Override
    public void onSubmitAddRss(final String url) {
        mFeedPresenter.onInsertRss(url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_feed);

        ButterKnife.bind(this);

        initToolbar();
        initTabsView();

        Log.e(TAG, "End create");
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
