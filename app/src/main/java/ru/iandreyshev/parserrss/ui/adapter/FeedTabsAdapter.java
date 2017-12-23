package ru.iandreyshev.parserrss.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.ui.fragment.FeedTabFragment;

public class FeedTabsAdapter extends SmartFragmentStatePagerAdapter {
    private static final String TAG = FeedTabsAdapter.class.getName();
    private static final String CUT_TITLE_PATTERN = "%s...";
    private static final int MAX_TITLE_LENGTH = 16;

    private FragmentManager mFragmentManager;
    private ArrayList<Rss> mRssList = new ArrayList<>();

    public FeedTabsAdapter(final FragmentManager manager) {
        super(manager);
        mFragmentManager = manager;
    }

    public void insert(final Rss rss) {
        mRssList.add(rss);
        notifyDataSetChanged();
    }

    public void update(final Rss rss) {
        int position = getItemPosition(rss.getFeed());

        if (position < 0) {
            return;
        }

        final FeedTabFragment fragment = (FeedTabFragment) getRegisteredFragment(position);

        if (fragment != null) {
            fragment.update(rss.getArticles());

            return;
        }

        mRssList.set(position, rss);
    }

    public void remove(final Rss rss) {
        int position = mRssList.indexOf(rss);

        if (position < 0) {
            return;
        }

        if (getRegisteredFragment(position) != null) {
            mFragmentManager.beginTransaction()
                    .detach(getRegisteredFragment(position))
                    .commit();
        }

        mRssList.remove(position);
        notifyDataSetChanged();
    }

    public Rss getRss(int position) {
        if (position < 0 || position >= mRssList.size()) {
            return null;
        }

        return mRssList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return FeedTabFragment.newInstance(mRssList.get(position));
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

    @Override
    public int getItemPosition(Object item) {
        if (item instanceof Rss) {
            return mRssList.indexOf(item);
        }

        return super.getItemPosition(item);
    }
}
