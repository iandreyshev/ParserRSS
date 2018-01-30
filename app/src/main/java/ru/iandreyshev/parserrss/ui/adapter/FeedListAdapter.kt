package ru.iandreyshev.parserrss.ui.adapter

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.HashSet

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.ui.extention.dateString
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener

class FeedListAdapter : RecyclerView.Adapter<FeedListAdapter.ListItem>() {
    private val itemsOnWindow: HashSet<ListItem> = HashSet()
    private val articles = ArrayList<ViewArticle>()
    private var articleClickListener: WeakReference<IOnArticleClickListener>? = null

    fun setArticles(newItems: List<ViewArticle>) {
        articles.clear()
        articles.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setArticleClickListener(listener: IOnArticleClickListener) {
        articleClickListener = WeakReference(listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItem {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_feed_item, parent, false)

        return ListItem(view)
    }

    override fun onBindViewHolder(item: ListItem, position: Int) {
        item.setContent(articles[position])
        item.setClickListener(articleClickListener)
    }

    override fun onViewAttachedToWindow(item: ListItem) {
        itemsOnWindow.add(item)
    }

    override fun onViewDetachedFromWindow(item: ListItem) {
        itemsOnWindow.remove(item)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun forEach(action: (ListItem) -> Unit) {
        itemsOnWindow.forEach { it -> action(it) }
    }

    class ListItem constructor(view: View) : RecyclerView.ViewHolder(view),
            IFeedItem,
            View.OnClickListener {
        override var id: Long = 0
        override var isImageLoaded: Boolean = false

        private var mClickListener: WeakReference<IOnArticleClickListener>? = null

        private var mTitle: TextView = view.findViewById(R.id.item_title)
        private var mDescription: TextView = view.findViewById(R.id.item_text)
        private var mImage: ImageView = view.findViewById(R.id.item_image)
        private var mDate: TextView = view.findViewById(R.id.item_date)

        override fun updateImage(bitmap: Bitmap) {
            isImageLoaded = true
            mImage.setImageBitmap(bitmap)
        }

        fun setContent(content: ViewArticle) {
            id = content.id
            mTitle.text = content.title
            mDescription.text = content.description

            mImage.setImageResource(R.drawable.ic_feed_item)
            mDate.text = content.date?.dateString

            itemView.setOnClickListener(this)
            isImageLoaded = false
        }

        fun setClickListener(listener: WeakReference<IOnArticleClickListener>?) {
            mClickListener = listener
        }

        override fun onClick(view: View) {
            val listener = mClickListener?.get()
            listener?.onArticleClick(id)
        }
    }
}
