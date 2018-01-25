package ru.iandreyshev.parserrss.presentation.presenter

import android.graphics.Bitmap

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

import java.lang.ref.WeakReference
import java.util.HashSet

import ru.iandreyshev.parserrss.models.async.GetArticleImageTask
import ru.iandreyshev.parserrss.models.imageProps.FeedListIconProps
import ru.iandreyshev.parserrss.models.imageProps.FullImage
import ru.iandreyshev.parserrss.presentation.view.IImageView
import ru.iandreyshev.parserrss.ui.adapter.IFeedItem
import ru.iandreyshev.parserrss.ui.listeners.IItemImageRequestListener

@InjectViewState
class ImagesLoadPresenter : MvpPresenter<IImageView>(), IItemImageRequestListener {
    companion object {
        const val TAG = "ImagesLoadPresenter"
    }

    private val iconsQueue = HashSet<Long>()

    fun loadImage(articleId: Long) {
        GetArticleImageTask.execute(articleId, GetImageFromNetListener(), FullImage.newInstance)
    }

    override fun getIconForItem(item: IFeedItem, isWithoutQueue: Boolean) {
        val id = item.id

        if (iconsQueue.contains(id) && !isWithoutQueue) {
            return
        } else if (item.isImageLoaded && !isWithoutQueue) {
            return
        }

        val listener = GetImageFromNetForFeedItem(item, id)
        GetArticleImageTask.execute(id, listener, FeedListIconProps.newInstance)
    }

    private inner class GetImageFromNetListener : GetArticleImageTask.IEventListener {
        override fun onPostExecute(result: Bitmap?) {
            viewState.insertImage(result ?: return)
        }
    }

    private inner class GetImageFromNetForFeedItem constructor(item: IFeedItem, private val idBeforeLoad: Long)
        : GetArticleImageTask.IEventListener {

        private val item: WeakReference<IFeedItem> = WeakReference(item)

        override fun onPreExecute() {
            iconsQueue.add(idBeforeLoad)
        }

        override fun onPostExecute(result: Bitmap?) {
            iconsQueue.remove(idBeforeLoad)
            val curItem = item.get()

            if (result != null && curItem != null && curItem.id == idBeforeLoad) {
                curItem.updateImage(result)
            }
        }
    }
}
