package ru.iandreyshev.parserrss.models.async

import android.graphics.Bitmap

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.extention.bitmap
import ru.iandreyshev.parserrss.models.imageProps.IImageProps
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

class GetArticleImageTask private constructor(
        private val mArticleId: Long,
        listener: ITaskListener<Long, Void, Bitmap>,
        private val mImageProps: IImageProps) : Task<Long, Void, Bitmap>(listener) {

    companion object {
        private const val MAX_BYTES_COUNT = 1048576L // 1MB

        fun execute(id: Long, listener: ITaskListener<Long, Void, Bitmap>, props: IImageProps) {
            GetArticleImageTask(id, listener, props).executeOnExecutor(Task.EXECUTOR)
        }
    }

    override fun doInBackground(vararg articles: Long?): Bitmap? {
        val article = App.repository.getArticleById(mArticleId)
        val articleImage = App.repository.getArticleImageByArticle(mArticleId)

        articleImage?.bitmap?.let { return mImageProps.configureToView(it) }

        val imageUrl = article?.imageUrl ?: return null
        val mRequestHandler = HttpRequestHandler(imageUrl)
        mRequestHandler.maxContentBytes = MAX_BYTES_COUNT

        val requestResult = mRequestHandler.sendGet()
        val imageBytes = mRequestHandler.body
        var imageBitmap = imageBytes?.bitmap

        if (requestResult === HttpRequestHandler.State.SUCCESS && imageBitmap != null) {
            App.repository.putArticleImage(mArticleId, mImageProps.configureToMemory(imageBitmap))
            imageBitmap = mImageProps.configureToView(imageBitmap)
        }

        return imageBitmap
    }

    interface IEventListener : ITaskListener<Long, Void, Bitmap>
}
