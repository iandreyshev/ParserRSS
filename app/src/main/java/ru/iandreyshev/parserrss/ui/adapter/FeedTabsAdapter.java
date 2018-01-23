package ru.iandreyshev.parserrss.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.app.Utils;
import ru.iandreyshev.parserrss.models.rss.ViewRss;
import ru.iandreyshev.parserrss.ui.fragment.FeedPageFragment;

public class FeedTabsAdapter extends FragmentStatePagerAdapter {
    private final List<ViewRss> mRssList = new ArrayList<>();

    public FeedTabsAdapter(final FragmentManager manager) {
        super(manager);
    }

    public void insert(final ViewRss rss) {
        mRssList.add(rss);
        notifyDataSetChanged();
    }

    public void remove(final ViewRss rss) {
        mRssList.remove(rss);
        notifyDataSetChanged();
    }

    public ViewRss getRss(int position) {
        if (position < 0 || position >= mRssList.size()) {
            return null;
        }

        return mRssList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return FeedPageFragment.newInstance(getRss(position));
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
    public int getItemPosition(@NonNull final Object item) {
        int position = mRssList.indexOf(item);

        if (position < 0) {
            return POSITION_NONE;
        }

        return position;
    }
}
