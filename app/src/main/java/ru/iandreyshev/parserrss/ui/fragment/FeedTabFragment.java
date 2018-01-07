package ru.iandreyshev.parserrss.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.presentation.presenter.FeedTabPresenter;
import ru.iandreyshev.parserrss.presentation.view.IFeedTabView;
import ru.iandreyshev.parserrss.ui.adapter.ArticlesListAdapter;
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener;

public class FeedTabFragment extends BaseFragment implements IFeedTabView {
    @InjectPresenter
    FeedTabPresenter mPresenter;

    private IViewRss mRss;
    private ArticlesListAdapter mListAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    public static FeedTabFragment newInstance(final IViewRss rss) {
        final FeedTabFragment fragment = new FeedTabFragment();
        fragment.mRss = rss;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.init(mRss);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstantState) {
        final View view = inflater.inflate(R.layout.feed_list, viewGroup, false);

        initListAdapter();
        initRefreshLayout(view);
        initRecyclerView(view);

        return view;
    }

    @Override
    public void startUpdate(boolean isStart) {
        mRefreshLayout.setRefreshing(isStart);
    }

    @Override
    public void setArticles(final List<IViewArticle> newArticles) {
        mListAdapter.setArticles(newArticles);
    }

    private void initListAdapter() {
        mListAdapter = new ArticlesListAdapter(getContext());
        mListAdapter.setArticleClickListener((IOnArticleClickListener) getContext());
    }

    private void initRefreshLayout(final View fragmentView) {
        mRefreshLayout = fragmentView.findViewById(R.id.feed_refresh_layout);
        mRefreshLayout.setOnRefreshListener(() -> mPresenter.onUpdate());
    }

    private void initRecyclerView(final View fragmentView) {
        final RecyclerView listView = fragmentView.findViewById(R.id.feed_items_list);
        listView.setAdapter(mListAdapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }
}
