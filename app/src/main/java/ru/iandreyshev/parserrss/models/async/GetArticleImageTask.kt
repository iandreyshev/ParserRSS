package ru.iandreyshev.parserrss.models.async

import android.graphics.Bitmap

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.app.Utils
import ru.iandreyshev.parserrss.models.imageProps.IImageProps
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

class GetArticleImageTask private constructor(private val articleId: Long, listener: ITaskListener<Long, Void, Bitmap>, private val imageProps: IImageProps)
    : Task<Long, Void, Bitmap>(listener) {

    companion object {
        private const val MAX_BYTES_COUNT = 1048576 // 1MB

        fun execute(articleId: Long, listener: ITaskListener<Long, Void, Bitmap>, props: IImageProps) {
            GetArticleImageTask(articleId, listener, props).executeOnExecutor(Task.EXECUTOR)
        }
    }

    interface IEventListener : ITaskListener<Long, Void, Bitmap>

    override fun doInBackground(vararg articles: Long?): Bitmap? {
        var imageBitmap: Bitmap?
        val article = App.getDatabase().getArticleById(articleId)

        if (article == null) {
            return null
        } else if (article.image != null) {
            return imageProps.configure(article.image)
        }

        val mRequestHandler = HttpRequestHandler(if (article.imageUrl == null) "" else article.imageUrl)
        mRequestHandler.maxContentBytes = MAX_BYTES_COUNT

        val requestResult = mRequestHandler.sendGet()

        val imageBytes = mRequestHandler.body
        imageBitmap = Utils.toBitmap(imageBytes)

        if (requestResult === HttpRequestHandler.State.Success && imageBitmap != null) {
            App.getDatabase().updateArticleImage(articleId, imageBitmap)
            imageBitmap = imageProps.configure(imageBitmap)
        }

        return imageBitmap
    }
}
