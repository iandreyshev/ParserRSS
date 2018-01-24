package ru.iandreyshev.parserrss.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.iandreyshev.parserrss.app.Utils

import java.util.ArrayList

import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.ui.fragment.FeedPageFragment

class FeedTabsAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
    private val mRssList = ArrayList<ViewRss>()

    fun insert(rss: ViewRss) {
        mRssList.add(rss)
        notifyDataSetChanged()
    }

    fun remove(rss: ViewRss) {
        mRssList.remove(rss)
        notifyDataSetChanged()
    }

    fun getRss(position: Int): ViewRss? {
        return mRssList.getOrNull(position)
    }

    override fun getItem(position: Int): Fragment {
        return FeedPageFragment.newInstance(getRss(position))
    }

    override fun getCount(): Int {
        return mRssList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return Utils.toTabTitle(getRss(position)?.title)
    }

    override fun getItemPosition(item: Any): Int {
        val position = mRssList.indexOf(item)

        return if (position < 0) POSITION_NONE else position
    }
}
