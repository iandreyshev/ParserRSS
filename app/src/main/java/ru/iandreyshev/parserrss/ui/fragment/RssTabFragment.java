package ru.iandreyshev.parserrss.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.rss.RssArticle;
import ru.iandreyshev.parserrss.ui.adapter.ArticlesListAdapter;
import ru.iandreyshev.parserrss.ui.adapter.IOnArticleClickListener;

public class RssTabFragment extends Fragment {
    private static final String REFRESHING_KEY = "IsRefreshing";

    private ArticlesListAdapter mListAdapter;
    private List<RssArticle> mArticles = new ArrayList<>();
    private IOnArticleClickListener mArticleClickListener;
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener;
    private SwipeRefreshLayout mRefreshLayout;

    public void startRefresh(boolean isStart) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(isStart);
        }
    }

    public void update(final List<RssArticle> newArticles) {
    }

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        mListAdapter = new ArticlesListAdapter(getContext());
        Log.e("Fragment", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstantState) {
        final View view = inflater.inflate(R.layout.feed_list, viewGroup, false);
        final RecyclerView itemsList = view.findViewById(R.id.feed_items_list);
        mRefreshLayout = view.findViewById(R.id.feed_refresh_layout);
        mRefreshLayout.setOnRefreshListener(mRefreshListener);
        if (savedInstantState != null) {
            final Boolean isRefresh = savedInstantState.getBoolean(REFRESHING_KEY);
            Log.e("Fragment", isRefresh.toString());
            mRefreshLayout.setRefreshing(isRefresh);
        }

        itemsList.setAdapter(mListAdapter);
        itemsList.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        mListAdapter.setArticles(mArticles);
        mListAdapter.setItemClickListener(mArticleClickListener);
        mListAdapter.notifyDataSetChanged();
        Log.e("Fragment", "onCreateView");

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRefreshLayout != null) {
            outState.putBoolean(REFRESHING_KEY, mRefreshLayout.isRefreshing());
        }
    }

    public static class Builder {
        private RssTabFragment fragment = new RssTabFragment();

        public Builder setArticles(List<RssArticle> articles) {
            fragment.mArticles = articles;

            return this;
        }

        public Builder setOnItemClickListener(IOnArticleClickListener listener) {
            fragment.mArticleClickListener = listener;

            return this;
        }

        public Builder setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
            fragment.mRefreshListener = listener;

            return this;
        }

        public RssTabFragment build() {
            return fragment;
        }
    }
}
