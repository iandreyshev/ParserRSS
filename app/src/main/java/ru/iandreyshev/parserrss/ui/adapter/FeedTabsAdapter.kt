package ru.iandreyshev.parserrss.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.app.App

import java.util.ArrayList

import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.ui.extention.tabTitle
import ru.iandreyshev.parserrss.ui.fragment.FeedPageFragment

class FeedTabsAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
    private val rssList = ArrayList<ViewRss>()
    val isEmpty
        get() = rssList.isEmpty()

    fun insert(rss: ViewRss) {
        rssList.add(rss)
        notifyDataSetChanged()
    }

    fun remove(rss: ViewRss) {
        rssList.remove(rss)
        notifyDataSetChanged()
    }

    fun getRss(position: Int): ViewRss? {
        return rssList.getOrNull(position)
    }

    override fun getItem(position: Int): Fragment {
        val rss = getRss(position)

        when (rss) {
            null -> throw IllegalArgumentException("Invalid item position")
            else -> return FeedPageFragment.newInstance(rss)
        }
    }

    override fun getCount(): Int {
        return rssList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return getRss(position)?.title?.tabTitle ?: App.getStr(R.string.feed_tab_title)
    }

    override fun getItemPosition(item: Any): Int {
        val position = rssList.indexOf(item)

        return if (position < 0) POSITION_NONE else position
    }
}
