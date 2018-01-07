package ru.iandreyshev.parserrss.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.ui.fragment.FeedTabFragment;

public class FeedTabsAdapter extends FragmentStatePagerAdapter {
    private static final String CUT_TITLE_PATTERN = "%s...";
    private static final int MAX_TITLE_LENGTH = 16;

    private final ArrayList<IViewRss> mRssList = new ArrayList<>();

    public FeedTabsAdapter(final FragmentManager manager) {
        super(manager);
    }

    public void insert(final IViewRss rss) {
        mRssList.add(rss);
        notifyDataSetChanged();
    }

    public void remove(final IViewRss rss) {
        mRssList.remove(rss);
        notifyDataSetChanged();
    }

    public IViewRss getRss(int position) {
        if (position < 0 || position >= mRssList.size()) {
            return null;
        }

        return mRssList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return FeedTabFragment.newInstance(getRss(position));
    }

    @Override
    public int getCount() {
        return mRssList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = getRss(position).getTitle();

        if (title.length() > MAX_TITLE_LENGTH) {
            title = title.substring(0, MAX_TITLE_LENGTH - 1);
            title = String.format(CUT_TITLE_PATTERN, title);
        }

        return title;
    }

    @Override
    public int getItemPosition(final Object item) {
        return POSITION_NONE;
    }
}
