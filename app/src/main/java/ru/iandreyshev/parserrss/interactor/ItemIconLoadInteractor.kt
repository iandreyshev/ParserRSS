package ru.iandreyshev.parserrss.interactor

import android.graphics.Bitmap
import ru.iandreyshev.parserrss.models.async.GetArticleImageTask
import ru.iandreyshev.parserrss.models.imageProps.FeedListIconProps
import ru.iandreyshev.parserrss.ui.adapter.IFeedItem
import java.lang.ref.WeakReference
import java.util.HashSet

class ItemIconLoadInteractor(private val outputPort: IOutputPort) : BaseInteractor(outputPort) {

    private val iconsQueue = HashSet<Long>()

    interface IOutputPort : BaseInteractor.IInteractorOutputPort {
        fun insertImage(item: IFeedItem, imageBitmap: Bitmap)
    }

    fun getIconForItem(item: IFeedItem, isWithoutQueue: Boolean) {
        val id = item.id

        if (iconsQueue.contains(id) && !isWithoutQueue) {
            return
        } else if (item.isImageLoaded && !isWithoutQueue) {
            return
        }

        val listener = GetImageFromNetForFeedItem(item, id)
        GetArticleImageTask.execute(id, listener, FeedListIconProps.newInstance)
    }

    fun clearQueue() = iconsQueue.clear()

    private inner class GetImageFromNetForFeedItem constructor(
            item: IFeedItem,
            private val idBeforeLoad: Long) : GetArticleImageTask.IEventListener {

        private val item: WeakReference<IFeedItem> = WeakReference(item)

        override fun onPreExecute() {
            iconsQueue.add(idBeforeLoad)
        }

        override fun onPostExecute(result: Bitmap?) {
            iconsQueue.remove(idBeforeLoad)
            val curItem = item.get()

            result ?: return
            curItem ?: return

            if (curItem.id == idBeforeLoad) {
                outputPort.insertImage(curItem, result)
            }
        }
    }
}
