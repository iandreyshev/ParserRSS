package ru.iandreyshev.parserrss.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssFeed;
import ru.iandreyshev.parserrss.ui.fragment.FeedTabFragment;

public class FeedTabsAdapter extends SmartFragmentStatePagerAdapter {
    private static final String TAG = FeedTabsAdapter.class.getName();
    private static final String CUT_TITLE_PATTERN = "%s...";
    private static final int MAX_TITLE_LENGTH = 16;

    private ArrayList<Rss> mRssList = new ArrayList<>();
    private IOnArticleClickListener mItemClickListener;
    private IOnRefreshFeedListener mRefreshListener;

    public FeedTabsAdapter(FragmentManager manager) {
        super(manager);
    }

    public void add(final Rss feed) {
        Log.e(TAG, "Add");
        mRssList.add(feed);
        notifyDataSetChanged();
    }

    public void update(final Rss rss) {
        int position = getItemPosition(rss.getFeed());

        if (position < 0) {
            Log.e(TAG, "Update fragment not found");
            return;
        }

        Log.e(TAG, "Update fragment");
        final FeedTabFragment fragment = (FeedTabFragment) getItem(position);
        fragment.update(rss.getArticles());
    }

    public void remove(final RssFeed feed) {
    }

    public void startRefresh(final RssFeed feed, boolean isStart) {
    }

    public void setOnItemClickListener(final IOnArticleClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnRefreshListener(final IOnRefreshFeedListener listener) {
        mRefreshListener = listener;
    }

    public RssFeed getFeed(int position) {
        if (position < 0 || position >= mRssList.size()) {
            return null;
        }

        return mRssList.get(position).getFeed();
    }

    @Override
    public Fragment getItem(int position) {
        FeedTabFragment fragment = (FeedTabFragment) getRegisteredFragment(position);

        if (fragment == null) {
            final Rss rss = mRssList.get(position);
            fragment = FeedTabFragment.newInstance(rss.getArticles())
                    .setOnItemClickListener(mItemClickListener)
                    .setOnRefreshListener(() -> onRefresh(rss.getFeed()));
            Log.e(TAG, "New instance");
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return mRssList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = mRssList.get(position).getFeed().getTitle();

        if (title.length() > MAX_TITLE_LENGTH) {
            title = title.substring(0, MAX_TITLE_LENGTH - 1);
            title = String.format(CUT_TITLE_PATTERN, title);
        }

        return title;
    }

    private void onRefresh(final RssFeed feed) {
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh(feed);
        }
    }
}
