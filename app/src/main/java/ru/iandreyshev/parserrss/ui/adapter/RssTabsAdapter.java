package ru.iandreyshev.parserrss.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssFeed;
import ru.iandreyshev.parserrss.ui.fragment.RssTabFragment;

public class RssTabsAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "RssTabsAdapter";
    private static final String CUT_TITLE_PATTERN = "%s...";
    private static final int MAX_TITLE_LENGTH = 16;

    private ArrayList<RssFeed> mRssList = new ArrayList<>();
    private HashMap<RssFeed, RssTabFragment> mFragments = new HashMap<>();
    private IOnArticleClickListener mItemClickListener;
    private IOnRefreshListener mRefreshListener;
    private FragmentManager mFragmentManager;

    public RssTabsAdapter(FragmentManager manager) {
        super(manager);
        mFragmentManager = manager;
    }

    public void add(final Rss rss) {
        if (mFragments.containsKey(rss.getFeed())) {
            return;
        }

        mRssList.add(rss.getFeed());
        mFragments.put(rss.getFeed(), new RssTabFragment.Builder()
                .setArticles(rss.getArticles())
                .setOnItemClickListener(mItemClickListener)
                .setOnRefreshListener(() -> onRefresh(rss))
                .build());

        notifyDataSetChanged();
    }

    public void update(final Rss rss) {
        if (mFragments.containsKey(rss.getFeed())) {
            mFragments.get(rss.getFeed()).update(rss.getArticles());
        }
    }

    public void remove(final RssFeed feed) {
    }

    public void startRefresh(final RssFeed feed, boolean isStart) {
        final RssTabFragment fragment = mFragments.get(feed);

        if (fragment != null) {
            fragment.startRefresh(isStart);
        }
    }

    public void setOnItemClickListener(IOnArticleClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnRefreshListener(IOnRefreshListener listener) {
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
        return mFragments.get(mRssList.get(position));
    }

    @Override
    public int getCount() {
        return mFragments.size();
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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        final FragmentTransaction transaction = mFragmentManager
                .beginTransaction()
                .remove((Fragment) object);
        transaction.commit();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final String tag = getFeed(position).toString();
        final FragmentTransaction transaction = mFragmentManager
                .beginTransaction()
                .add(container.getId(), getItem(position), tag);
        transaction.commit();

        return getItem(position);
    }

    private void onRefresh(final Rss rss) {
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh(rss.getFeed());
        }
    }
}
