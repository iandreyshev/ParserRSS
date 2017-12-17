package ru.iandreyshev.parserrss.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.iandreyshev.parserrss.models.rss.IRssFeed;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.ui.fragment.FeedTabFragment;

public class FeedTabsAdapter extends FragmentStatePagerAdapter {
    private static final String CUT_TITLE_PATTERN = "%s...";
    private static final String DEFAULT_TITLE = "News...";
    private static final int MAX_TITLE_LENGTH = 12;

    private List<IRssFeed> mFeeds = new ArrayList<>();
    private HashMap<IRssFeed, FeedTabFragment> mFeedsPages = new HashMap<>();
    private IOnArticleClickListener mOnItemClickListener;
    private IOnRefreshListener mOnRefreshListener;

    public FeedTabsAdapter(FragmentManager manager) {
        super(manager);
    }

    public void add(final Rss rss) {
        final IRssFeed feed = rss.getFeed();
        mFeeds.add(feed);

        final FeedTabFragment page = new FeedTabFragment();
        page.setOnItemClickListener(mOnItemClickListener);
        page.setOnRefreshListener(() -> {
            if (mOnRefreshListener != null) {
                mOnRefreshListener.onRefresh(feed);
            }
        });
        page.setArticles(rss.getArticles());

        mFeedsPages.put(feed, page);

        notifyDataSetChanged();
    }

    public void update(final Rss rss) {
        final FeedTabFragment fragment = mFeedsPages.get(rss.getFeed());

        if (fragment == null) {
            return;
        }

        fragment.setArticles(rss.getArticles());
    }

    public void remove(final IRssFeed feed) {
    }

    public void startRefresh(IRssFeed feed, boolean isRefresh) {
    }

    public void setOnItemClickListener(IOnArticleClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnRefreshListener(IOnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        return getFragment(position);
    }

    @Override
    public int getCount() {
        return mFeeds.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        final String title = mFeeds.get(position).getTitle();

        if (title.length() < MAX_TITLE_LENGTH) {
            return (title.length() == 0) ? DEFAULT_TITLE : title;
        }

        return String.format(CUT_TITLE_PATTERN, title.substring(0, MAX_TITLE_LENGTH - 1));
    }

    private FeedTabFragment getFragment(int position) {
        return mFeedsPages.get(mFeeds.get(position));
    }
}
