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

public class FeedTabFragment extends Fragment {
    private static final String TAG = "FeedTabFragment";
    private static final String REFRESHING_KEY = "REFRESHING_KEY";
    private static final String ARTICLES_KEY = "ARTICLES_KEY";

    private ArticlesListAdapter mListAdapter;
    private IOnArticleClickListener mArticleClickListener;
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener;
    private SwipeRefreshLayout mRefreshLayout;

    public static FeedTabFragment newInstance(final ArrayList<RssArticle> articles) {
        final FeedTabFragment fragment = new FeedTabFragment();

        final Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(ARTICLES_KEY, articles);
        fragment.setArguments(arguments);

        return fragment;
    }

    public void startRefresh(boolean isStart) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(isStart);
        }
    }

    public void update(final List<RssArticle> newArticles) {
    }

    public FeedTabFragment setOnItemClickListener(final IOnArticleClickListener listener) {
        mArticleClickListener = listener;

        return this;
    }

    public FeedTabFragment setOnRefreshListener(final SwipeRefreshLayout.OnRefreshListener listener) {
        mRefreshListener = listener;

        return this;
    }

    @Override
    public void onCreate(final Bundle savedInstantState) {
        super.onCreate(savedInstantState);

        mListAdapter = new ArticlesListAdapter(getContext());
        if (this.getArguments() != null) {
            Log.e(TAG, "Load articles " + hashCode());
            mListAdapter.setArticles(this.getArguments().getParcelableArrayList(ARTICLES_KEY));
        }

        Log.e(TAG, "onCreate " + hashCode());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstantState) {
        final View view = inflater.inflate(R.layout.feed_list, viewGroup, false);
        final RecyclerView listView = view.findViewById(R.id.feed_items_list);

        mRefreshLayout = view.findViewById(R.id.feed_refresh_layout);
        mRefreshLayout.setOnRefreshListener(mRefreshListener);
        if (savedInstantState != null) {
            mRefreshLayout.setRefreshing(savedInstantState.getBoolean(REFRESHING_KEY));
        }

        listView.setAdapter(mListAdapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        mListAdapter.setItemClickListener(mArticleClickListener);

        Log.e(TAG, "onCreateView");
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(REFRESHING_KEY, mRefreshLayout.isRefreshing());
        outState.putParcelableArrayList(ARTICLES_KEY, mListAdapter.getArticles());
    }
}
