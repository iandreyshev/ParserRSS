package ru.iandreyshev.parserrss.ui.activity;

import android.content.Context;
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
import android.widget.TextView;

import butterknife.BindView;

import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;
import ru.iandreyshev.parserrss.presentation.presenter.FeedPresenter;
import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.ui.adapter.FeedTabsAdapter;
import ru.iandreyshev.parserrss.ui.fragment.AddRssDialog;
import ru.iandreyshev.parserrss.ui.fragment.RssInfoDialog;
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener;
import ru.iandreyshev.parserrss.ui.listeners.IOnSubmitAddRssListener;

import com.arellomobile.mvp.presenter.InjectPresenter;

public class FeedActivity extends BaseActivity implements IFeedView, IOnArticleClickListener, IOnSubmitAddRssListener {
    @InjectPresenter
    FeedPresenter mFeedPresenter;

    @BindView(R.id.feed_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.feed_tabs_layout)
    TabLayout mTabs;
    @BindView(R.id.feed_view_pager)
    ViewPager mPager;
    @BindView(R.id.feed_content_message_title)
    TextView mContentMessage;
    @BindView(R.id.feed_progress_bar)
    ProgressBar mProgressBar;

    private FeedTabsAdapter mTabsAdapter;
    private MenuItem mMenuAddButton;
    private MenuItem mMenuInfoButton;
    private MenuItem mMenuDeleteButton;

    public static Intent getIntent(final Context context) {
        return new Intent(context, FeedActivity.class);
    }

    @Override
    public void insertRss(final IViewRss rss) {
        mTabsAdapter.insert(rss);
        mPager.setCurrentItem(mTabsAdapter.getCount());
        onFeedUpdate();
    }

    @Override
    public void removeRss(final IViewRss rss) {
        mTabsAdapter.remove(rss);
        onFeedUpdate();
    }

    @Override
    public void openArticle(long articleId) {
        final Intent intent = ArticleActivity.getIntent(this)
                .putExtra(ArticleActivity.ARTICLE_BOUND_KEY, articleId)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

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
        mMenuAddButton.setEnabled(isEnable);
    }

    @Override
    public void enableDeleteButton(boolean isEnable) {
        mMenuDeleteButton.setEnabled(isEnable);
    }

    @Override
    public void enableInfoButton(boolean isEnable) {
        mMenuInfoButton.setEnabled(isEnable);
    }

    @Override
    public void openRssInfo(final IViewRss feed) {
        RssInfoDialog.show(getSupportFragmentManager(), feed);
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
        mMenuInfoButton.setEnabled(mTabsAdapter.getCount() > 0);
        mMenuDeleteButton.setEnabled(mTabsAdapter.getCount() > 0);

        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final IViewRss currentRss = mTabsAdapter.getRss(mPager.getCurrentItem());

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
    public void onArticleClick(final IViewArticle article) {
        mFeedPresenter.openArticle(article);
    }

    @Override
    public void onSubmitAddRss(final String url) {
        mFeedPresenter.onInsertRss(url);
    }

    @Override
    public void onFeedUpdate() {
        boolean isFeedEmpty = mTabsAdapter.getCount() == 0;

        mContentMessage.setVisibility(isFeedEmpty ? View.VISIBLE : View.GONE);
        mPager.setVisibility(isFeedEmpty ? View.GONE : View.VISIBLE);
        mTabs.setVisibility(isFeedEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_feed);

        ButterKnife.bind(this);

        initToolbar();
        initTabsView();
        onFeedUpdate();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        startProgressBar(false);
    }

    private void initTabsView() {
        mTabsAdapter = new FeedTabsAdapter(getSupportFragmentManager());
        mPager.setAdapter(mTabsAdapter);
        mTabs.setupWithViewPager(mPager);
    }
}
