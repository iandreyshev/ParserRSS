package ru.iandreyshev.parserrss.ui.adapter

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.view_feed_item.view.*
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.viewModels.ViewArticle
import ru.iandreyshev.parserrss.ui.animation.ImageFadeChangeAnimation
import ru.iandreyshev.parserrss.ui.extention.dateString
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener
import java.lang.ref.WeakReference

class FeedListItem constructor(view: View) : RecyclerView.ViewHolder(view), IItemIcon, View.OnClickListener {

    companion object {
        private const val ICON_OUT_DURATION_MS: Long = 50
        private const val ICON_IN_DURATION_MS: Long = 300
    }

    override val id: Long
        get() = mId
    val isUpdateStart: Boolean
        get() = mIsUpdateStart
    val isUpdateEnd: Boolean
        get() = mIsUpdateEnd

    private var mId: Long = 0
    private var mIsUpdateStart = false
    private var mIsUpdateEnd = false
    private var mClickListener: WeakReference<IOnArticleClickListener>? = null
    private val mAnimation = ImageFadeChangeAnimation(view.context, ICON_IN_DURATION_MS, ICON_OUT_DURATION_MS)

    override fun updateImage(bitmap: Bitmap) {
        mAnimation.start(itemView.imageView, bitmap)
        mIsUpdateEnd = true
    }

    override fun onClick(view: View) {
        mClickListener?.get()?.onArticleClick(mId)
    }

    fun onStartUpdate() {
        mIsUpdateStart = true
    }

    fun resetUpdate() {
        mIsUpdateStart = false
    }

    fun bind(content: ViewArticle) {
        mId = content.id
        mIsUpdateStart = false

        itemView.titleView.text = content.title
        itemView.descriptionView.text = content.description

        itemView.imageView.setImageResource(R.drawable.ic_feed_item)
        itemView.dateView.text = content.date?.dateString

        itemView.setOnClickListener(this)
    }

    fun setClickListener(listener: WeakReference<IOnArticleClickListener>?) {
        mClickListener = listener
    }
}
