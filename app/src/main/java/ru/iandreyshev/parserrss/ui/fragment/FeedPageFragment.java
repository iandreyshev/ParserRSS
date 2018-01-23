package ru.iandreyshev.parserrss.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.rss.ViewArticle;
import ru.iandreyshev.parserrss.models.rss.ViewRss;
import ru.iandreyshev.parserrss.presentation.presenter.FeedPagePresenter;
import ru.iandreyshev.parserrss.presentation.presenter.ImagesLoadPresenter;
import ru.iandreyshev.parserrss.presentation.view.IFeedPageView;
import ru.iandreyshev.parserrss.presentation.view.IImageView;
import ru.iandreyshev.parserrss.ui.adapter.FeedListAdapter;
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener;

public class FeedPageFragment extends BaseFragment implements IFeedPageView, IImageView {
    private static final int MAX_SCROLL_SPEED_TO_UPDATE_IMAGES = 15;

    @InjectPresenter(type = PresenterType.GLOBAL, tag = ImagesLoadPresenter.TAG)
    ImagesLoadPresenter mImageLoadPresenter;
    @InjectPresenter
    FeedPagePresenter mPresenter;

    @BindView(R.id.feed_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.feed_items_list)
    RecyclerView mListView;

    private ViewRss mRss;
    private FeedListAdapter mListAdapter;

    public static FeedPageFragment newInstance(final ViewRss rss) {
        final FeedPageFragment fragment = new FeedPageFragment();
        fragment.mRss = rss;

        return fragment;
    }

    @ProvidePresenter
    public FeedPagePresenter provideFeedPagePresenter() {
        return new FeedPagePresenter(mRss);
    }

    public ViewRss getRss() {
        return mRss;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstantState) {
        final View view = inflater.inflate(R.layout.feed_list, viewGroup, false);

        ButterKnife.bind(this, view);

        initListView();
        mRefreshLayout.setOnRefreshListener(mPresenter::onUpdate);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateImages(true);
    }

    @Override
    public void startUpdate(boolean isStart) {
        mRefreshLayout.setRefreshing(isStart);
    }

    @Override
    public void setArticles(final List<ViewArticle> newArticles) {
        mListAdapter.setArticles(newArticles);
        updateImages(true);
    }

    @Override
    public void updateImages(boolean isWithoutQueue) {
        for (final FeedListAdapter.ListItem item : mListAdapter.getItemsOnWindow()) {
            mImageLoadPresenter.getIconForItem(item, isWithoutQueue);
        }
    }

    private void initListView() {
        mListAdapter = new FeedListAdapter();
        mListAdapter.setArticleClickListener((IOnArticleClickListener) getContext());

        mListView.setAdapter(mListAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListView.addOnScrollListener(new ScrollListener(this));

        if (getContext() != null) {
            mListView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        }
    }

    @Override
    public void insertImage(@NonNull Bitmap bitmap) {
        // Needs declare to inject ImagesLoadPresenter presenter
    }

    static class ScrollListener extends RecyclerView.OnScrollListener {
        final WeakReference<FeedPageFragment> mFragment;

        ScrollListener(final FeedPageFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (Math.abs(dy) <= MAX_SCROLL_SPEED_TO_UPDATE_IMAGES && mFragment.get() != null) {
                mFragment.get().updateImages(false);
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mFragment.get() != null) {
                mFragment.get().updateImages(false);
            }
        }
    }
}
