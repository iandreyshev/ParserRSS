package ru.iandreyshev.parserrss.ui.adapter

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
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

    private val _itemsOnWindow: HashSet<ListItem> = HashSet()
    private val _articles = ArrayList<ViewArticle>()
    private var _articleClickListener: WeakReference<IOnArticleClickListener>? = null

    fun setArticles(newItems: List<ViewArticle>) {
        _articles.clear()
        _articles.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setArticleClickListener(listener: IOnArticleClickListener) {
        _articleClickListener = WeakReference(listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItem {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_feed_item, parent, false)

        return ListItem(view)
    }

    override fun onBindViewHolder(item: ListItem, position: Int) {
        item.bind(_articles[position])
        item.setClickListener(_articleClickListener)
    }

    override fun onViewAttachedToWindow(item: ListItem) {
        _itemsOnWindow.add(item)
    }

    override fun onViewDetachedFromWindow(item: ListItem) {
        _itemsOnWindow.remove(item)
    }

    override fun getItemCount(): Int {
        return _articles.size
    }

    fun forEach(action: (ListItem) -> Unit) {
        _itemsOnWindow.forEach { it -> action(it) }
    }

    class ListItem constructor(view: View) : RecyclerView.ViewHolder(view),
            IItemIcon,
            View.OnClickListener {

        override val id: Long
            get() = _id
        override val isLoaded: Boolean
            get() = _isImageLoaded

        private var _id: Long = 0
        private var _isImageLoaded: Boolean = false
        private var _clickListener: WeakReference<IOnArticleClickListener>? = null

        override fun updateImage(bitmap: Bitmap) {
            _isImageLoaded = true
            itemView.imageView.setImageBitmap(bitmap)
        }

        fun bind(content: ViewArticle) {
            Log.e("Holder bind ", content.id.toString())
            _id = content.id
            _isImageLoaded = false

            itemView.titleView.text = content.title
            itemView.descriptionView.text = content.description

            itemView.imageView.setImageResource(R.drawable.ic_feed_item)
            itemView.dateView.text = content.date?.dateString

            itemView.setOnClickListener(this)
        }

        fun setClickListener(listener: WeakReference<IOnArticleClickListener>?) {
            _clickListener = listener
        }

        override fun onClick(view: View) {
            _clickListener?.get()?.onArticleClick(_id)
        }
    }
}
