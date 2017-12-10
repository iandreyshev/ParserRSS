package ru.iandreyshev.parserrss.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import butterknife.BindView;

import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;
import ru.iandreyshev.parserrss.presentation.presenter.FeedPresenter;
import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.ui.adapter.FeedListAdapter;
import ru.iandreyshev.parserrss.ui.adapter.IOnItemClickListener;
import ru.iandreyshev.parserrss.ui.fragment.AddFeedDialog;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

public class FeedActivity extends BaseActivity implements IFeedView {
    @InjectPresenter
    FeedPresenter mFeedPresenter;

    @BindView(R.id.feed_items_list)
    RecyclerView mItemsList;
    @BindView(R.id.feed_refresh_layout)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.feed_toolbar)
    Toolbar mToolbar;

    FeedListAdapter mItemsAdapter;
    FeedListListener mFeedListener = new FeedListListener();

    public static Intent getIntent(final Context context) {
        return new Intent(context, FeedActivity.class);
    }

    @Override
    public void openSettings() {
        final Intent intent = SettingsActivity.getIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
    }

    @Override
    public void openArticle(IArticleInfo article) {
        final Intent intent = ArticleActivity.getIntent(this)
                .putExtra(getResources().getString(R.string.const_article_bundle_key), article)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
    }

    @Override
    public void openAddingFeedDialog() {
        AddFeedDialog dialog = new AddFeedDialog();
        dialog.setOnSubmitListener((DialogInterface dialogInterface, int which) -> {
            try {
                EditText urlField = dialog.getView().findViewById(R.id.add_feed_dialog_url_field);
                mFeedPresenter.onSubmitAddingFeed(urlField.toString());
            } catch (Exception ex) {

                // TODO: error log

                dialogInterface.cancel();
            }
        });
        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void startProgressBar(boolean isStart) {
        setProgressBarIndeterminateVisibility(isStart);
    }

    @Override
    public void updateFeedList(IFeedInfo feed, List<IArticleInfo> newList) {
        mItemsAdapter.setItems(newList);
        setRefreshing(false);
    }

    @Override
    public void setRefreshing(boolean isRefresh) {
        mSwipeLayout.setRefreshing(isRefresh);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feed_options_add:
                mFeedPresenter.onAddingButtonClick();
                startProgressBar(true);
                break;
            case R.id.feed_options_edit:
                startProgressBar(false);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_feed);

        ButterKnife.bind(this);

        startProgressBar(false);
        initToolbar();
        initTabsView();
        initRefreshListener();
    }

    private void initToolbar() {
        mToolbar.setTitle(getResources().getString(R.string.feed_title));
        setSupportActionBar(mToolbar);
    }

    private void initTabsView() {
        if (mItemsAdapter != null) {
            return;
        }
        mItemsAdapter = new FeedListAdapter(this);
        mItemsAdapter.setOnItemClickListener(mFeedListener);
        mItemsList.setAdapter(mItemsAdapter);
    }

    private void initRefreshListener() {
        mSwipeLayout.setOnRefreshListener(mFeedListener);
    }

    private void onSubmitAddingFeed() {
    }

    private void onCancelFromAdding() {
    }

    private class FeedListListener
            implements SwipeRefreshLayout.OnRefreshListener, IOnItemClickListener<IArticleInfo> {
        @Override
        public void onRefresh() {
            mFeedPresenter.onRefresh();
        }

        @Override
        public void onItemClick(View view, IArticleInfo item) {
            mFeedPresenter.onItemClick(item);
        }
    }
}
