package ru.iandreyshev.parserrss.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssFeed;
import ru.iandreyshev.parserrss.ui.fragment.RssTabFragment;

public class RssTabsAdapter extends SmartFragmentStatePagerAdapter {
    private static final String TAG = "RssTabsAdapter";
    private static final String CUT_TITLE_PATTERN = "%s...";
    private static final int MAX_TITLE_LENGTH = 16;

    private ArrayList<RssFeed> mRssList = new ArrayList<>();
    private IOnArticleClickListener mItemClickListener;
    private IOnRefreshFeedListener mRefreshListener;
    private FragmentManager mManager;

    public RssTabsAdapter(FragmentManager manager) {
        super(manager);
    }

    public void add(final RssFeed feed) {
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
        final RssTabFragment fragment = (RssTabFragment) getItem(position);
        fragment.update(rss.getArticles());
    }

    public void remove(final RssFeed feed) {
    }

    public void startRefresh(final RssFeed feed, boolean isStart) {
    }

    public void setOnItemClickListener(IOnArticleClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnRefreshListener(IOnRefreshFeedListener listener) {
        mRefreshListener = listener;
    }

    public RssFeed getFeed(int position) {
        if (position < 0 || position >= mRssList.size()) {
            return null;
        }

        return mRssList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        final Fragment fragment = getRegisteredFragment(position);

        if (fragment == null) {
            Log.e(TAG, "Create new fragment");
            final RssFeed feed = mRssList.get(position);
            return new RssTabFragment.Builder()
                    .setOnItemClickListener(mItemClickListener)
                    .setOnRefreshListener(() -> onRefresh(feed))
                    .build();
        }

        Log.e(TAG, "Load fragment");

        return fragment;
    }

    @Override
    public int getItemPosition(final Object item) {
        if (item instanceof RssFeed) {
            return mRssList.indexOf(item);
        }

        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mRssList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = mRssList.get(position).getTitle();

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
