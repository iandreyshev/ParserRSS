package ru.iandreyshev.parserrss.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import ru.iandreyshev.parserrss.app.Utils;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.ui.fragment.FeedTabFragment;

public class FeedTabsAdapter extends SmartFragmentStatePagerAdapter {
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
        return Utils.truncateTabsTitle(getRss(position).getTitle());
    }

    @Override
    public int getItemPosition(final Object item) {
        int position = mRssList.indexOf(item);

        return position < 0 ? POSITION_NONE : position;
    }
}
