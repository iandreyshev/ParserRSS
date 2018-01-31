package ru.iandreyshev.parserrss.interactor

import android.graphics.Bitmap
import ru.iandreyshev.parserrss.models.async.GetArticleImageTask
import ru.iandreyshev.parserrss.models.imageProps.FullImage

class ImagesLoadInteractor(private val outputPort: IOutputPort) : BaseInteractor(outputPort) {

    interface IOutputPort : IInteractorOutputPort {
        fun insertImage(imageBitmap: Bitmap)
    }

    fun loadImage(articleId: Long) {
        GetArticleImageTask.execute(articleId, GetImageFromNetListener(), FullImage.newInstance)
    }

    private inner class GetImageFromNetListener : GetArticleImageTask.IEventListener {
        override fun onPostExecute(result: Bitmap?) {
            outputPort.insertImage(result ?: return)
        }
    }
}