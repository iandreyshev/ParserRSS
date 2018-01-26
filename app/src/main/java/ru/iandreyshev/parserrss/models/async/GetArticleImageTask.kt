package ru.iandreyshev.parserrss.models.async

import android.graphics.Bitmap

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.extention.bitmap
import ru.iandreyshev.parserrss.models.imageProps.IImageProps
import ru.iandreyshev.parserrss.models.repository.ArticleImage
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

class GetArticleImageTask private constructor(
        private val articleId: Long,
        listener: ITaskListener<Long, Void, Bitmap>,
        private val imageProps: IImageProps) : Task<Long, Void, Bitmap>(listener) {

    companion object {
        private const val MAX_BYTES_COUNT = 1048576 // 1MB

        fun execute(articleId: Long, listener: ITaskListener<Long, Void, Bitmap>, props: IImageProps) {
            GetArticleImageTask(articleId, listener, props).executeOnExecutor(Task.EXECUTOR)
        }
    }

    override fun doInBackground(vararg articles: Long?): Bitmap? {
        val article = App.database.getArticleById(articleId)
        val articleImage = App.database.getArticleImageByArticle(articleId)
        val bitmap = articleImage?.bitmap

        if (bitmap != null) {
            return imageProps.configure(bitmap)
        }

        val imageUrl = article?.imageUrl ?: return null
        val mRequestHandler = HttpRequestHandler(imageUrl)
        mRequestHandler.maxContentBytes = MAX_BYTES_COUNT

        val requestResult = mRequestHandler.sendGet()
        val imageBytes = mRequestHandler.body
        var imageBitmap = imageBytes?.bitmap

        if (requestResult === HttpRequestHandler.State.Success && imageBitmap != null) {
            App.database.putArticleImage(ArticleImage(articleId = articleId, bitmap = imageBitmap))
            imageBitmap = imageProps.configure(imageBitmap)
        }

        return imageBitmap
    }

    interface IEventListener : ITaskListener<Long, Void, Bitmap>
}
