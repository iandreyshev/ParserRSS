package ru.iandreyshev.parserrss.ui.adapter

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_feed_item.view.*

import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.HashSet

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.ui.extention.dateString
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener

class FeedListAdapter : RecyclerView.Adapter<FeedListAdapter.ListItem>() {

    private val mItemsOnWindow: HashSet<ListItem> = HashSet()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItem {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_feed_item, parent, false)

        return ListItem(view)
    }

    override fun onBindViewHolder(item: ListItem, position: Int) {
        item.bind(mArticles[position])
        item.setClickListener(mArticleClickListener)
    }

    override fun onViewAttachedToWindow(item: ListItem) {
        mItemsOnWindow.add(item)
    }

    override fun onViewDetachedFromWindow(item: ListItem) {
        mItemsOnWindow.remove(item)
    }

    override fun getItemCount(): Int {
        return mArticles.size
    }

    fun forEach(action: (ListItem) -> Unit) {
        mItemsOnWindow.forEach { it -> action(it) }
    }

    class ListItem constructor(view: View) : RecyclerView.ViewHolder(view),
            IItemIcon,
            View.OnClickListener {

        override val id: Long
            get() = mId
        override val isLoaded: Boolean
            get() = mIsImageLoaded

        private var mId: Long = 0
        private var mIsImageLoaded: Boolean = false
        private var mClickListener: WeakReference<IOnArticleClickListener>? = null

        override fun updateImage(bitmap: Bitmap) {
            mIsImageLoaded = true
            itemView.imageView.setImageBitmap(bitmap)
        }

        fun bind(content: ViewArticle) {
            mId = content.id
            mIsImageLoaded = false

            itemView.titleView.text = content.title
            itemView.descriptionView.text = content.description

            itemView.imageView.setImageResource(R.drawable.ic_feed_item)
            itemView.dateView.text = content.date?.dateString

            itemView.setOnClickListener(this)
        }

        fun setClickListener(listener: WeakReference<IOnArticleClickListener>?) {
            mClickListener = listener
        }

        override fun onClick(view: View) {
            mClickListener?.get()?.onArticleClick(mId)
        }
    }
}
