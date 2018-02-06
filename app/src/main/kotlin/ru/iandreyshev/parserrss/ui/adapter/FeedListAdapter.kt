package ru.iandreyshev.parserrss.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.HashSet

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener


class FeedListAdapter : RecyclerView.Adapter<FeedListItem>() {

    private val mItemsOnWindow: HashSet<FeedListItem> = HashSet()
    private val mArticles = ArrayList<ViewArticle>()
    private var mArticleClickListener: WeakReference<IOnArticleClickListener>? = null

    fun setArticles(newItems: List<ViewArticle>) {
        mArticles.clear()
        mArticles.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setArticleClickListener(listener: IOnArticleClickListener) {
        mArticleClickListener = WeakReference(listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedListItem {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_feed_item, parent, false)

        return FeedListItem(view)
    }

    override fun onBindViewHolder(item: FeedListItem, position: Int) {
        item.bind(mArticles[position])
        item.setClickListener(mArticleClickListener)
    }

    override fun onViewAttachedToWindow(item: FeedListItem) {
        mItemsOnWindow.add(item)
    }

    override fun onViewDetachedFromWindow(item: FeedListItem) {
        mItemsOnWindow.remove(item)
    }

    override fun getItemCount(): Int {
        return mArticles.size
    }

    fun forEach(action: (FeedListItem) -> Unit) {
        mItemsOnWindow.forEach { it -> action(it) }
    }
}
