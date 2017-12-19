package ru.iandreyshev.parserrss.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.rss.IRssArticle;
import ru.iandreyshev.parserrss.ui.adapter.ArticlesListAdapter;
import ru.iandreyshev.parserrss.ui.adapter.IOnArticleClickListener;

public class FeedTabFragment extends Fragment {
    private ArticlesListAdapter mListAdapter;
    private List<IRssArticle> mArticles = new ArrayList<>();
    private IOnArticleClickListener mOnItemClickListener;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean mIsRefreshing = false;

    public FeedTabFragment() {
    }

    public void setArticles(List<IRssArticle> articles) {
        mArticles = articles;
    }

    public void setOnItemClickListener(IOnArticleClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    public void startRefresh(boolean isStart) {
        mIsRefreshing = isStart;

        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(isStart);
        }
    }

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        initListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstantState) {
        View listView = initList(inflater, viewGroup);
        mListAdapter.notifyDataSetChanged();

        return listView;
    }

    private View initList(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.feed_list, viewGroup, false);

        final RecyclerView itemsList = view.findViewById(R.id.feed_items_list);
        itemsList.setAdapter(mListAdapter);
        itemsList.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        mRefreshLayout = view.findViewById(R.id.feed_refresh_layout);
        mRefreshLayout.setOnRefreshListener(() -> {
            if (mOnRefreshListener != null) {
                mOnRefreshListener.onRefresh();
            }
            mIsRefreshing = true;
        });
        mRefreshLayout.setRefreshing(mIsRefreshing);

        return view;
    }

    private void initListAdapter() {
        mListAdapter = new ArticlesListAdapter(getContext());
        mListAdapter.setOnItemClickListener(mOnItemClickListener);
        mListAdapter.setArticles(mArticles);
    }
}
