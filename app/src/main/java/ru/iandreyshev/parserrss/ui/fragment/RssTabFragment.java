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

import java.util.List;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.rss.RssArticle;
import ru.iandreyshev.parserrss.ui.adapter.ArticlesListAdapter;
import ru.iandreyshev.parserrss.ui.adapter.IOnArticleClickListener;

public class RssTabFragment extends Fragment {
    private static final String TAG = "RssTabFragment";
    private static final String REFRESHING_KEY = "REFRESHING_KEY";

    private ArticlesListAdapter mListAdapter;
    private IOnArticleClickListener mArticleClickListener;
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener;
    private SwipeRefreshLayout mRefreshLayout;

    public void startRefresh(boolean isStart) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(isStart);
        }
    }

    public void update(final List<RssArticle> newArticles) {
        if (mListAdapter != null) {
            mListAdapter.setArticles(newArticles);
        }
    }

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        mListAdapter = new ArticlesListAdapter(getContext());
        Log.e(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstantState) {
        final View view = inflater.inflate(R.layout.feed_list, viewGroup, false);
        final RecyclerView itemsList = view.findViewById(R.id.feed_items_list);

        mRefreshLayout = view.findViewById(R.id.feed_refresh_layout);
        mRefreshLayout.setOnRefreshListener(mRefreshListener);
        if (savedInstantState != null) {
            mRefreshLayout.setRefreshing(savedInstantState.getBoolean(REFRESHING_KEY));
        }

        itemsList.setAdapter(mListAdapter);
        itemsList.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        mListAdapter.setItemClickListener(mArticleClickListener);
        mListAdapter.notifyDataSetChanged();

        Log.e(TAG, "onCreateView");
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(REFRESHING_KEY, mRefreshLayout.isRefreshing());
    }

    public static class Builder {
        private RssTabFragment fragment = new RssTabFragment();

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
