package ru.iandreyshev.parserrss.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.PagerAdapter
import eu.inloop.pager.UpdatableFragmentPagerAdapter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.app.App

import java.util.ArrayList

import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.ui.extention.tabTitle
import ru.iandreyshev.parserrss.ui.fragment.FeedPageFragment

class FeedPagesAdapter(manager: FragmentManager) : UpdatableFragmentPagerAdapter(manager) {

    private val _rssList = ArrayList<ViewRss>()

    val isEmpty
        get() = _rssList.isEmpty()

    fun insert(rss: ViewRss) {
        _rssList.add(rss)
        notifyDataSetChanged()
    }

    fun remove(rss: ViewRss) {
        _rssList.remove(rss)
        notifyDataSetChanged()
    }

    fun getRss(position: Int): ViewRss? {
        return _rssList.getOrNull(position)
    }

    override fun getItem(position: Int): Fragment {
        val rss = getRss(position)

        when (rss) {
            null -> throw IllegalArgumentException("Invalid item position")
            else -> return FeedPageFragment.newInstance(rss)
        }
    }

    override fun getItemId(position: Int): Long {
        return _rssList.getOrNull(position)?.id ?: 0
    }

    override fun getCount(): Int {
        return _rssList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return getRss(position)?.title?.tabTitle ?: App.getStr(R.string.feed_tab_title)
    }

    override fun getItemPosition(item: Any): Int {
        val position = _rssList.indexOf(item)

        return if (position < 0) PagerAdapter.POSITION_NONE else position
    }
}
