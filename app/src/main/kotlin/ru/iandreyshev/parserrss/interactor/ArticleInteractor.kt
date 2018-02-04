package ru.iandreyshev.parserrss.interactor

import android.graphics.Bitmap
import android.net.Uri
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.async.GetArticleFromDbTask
import ru.iandreyshev.parserrss.models.async.GetArticleImageTask
import ru.iandreyshev.parserrss.models.extention.uri
import ru.iandreyshev.parserrss.models.imageProps.ArticleImageProps
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss

class ArticleInteractor(
        private val mOutputPort: IOutputPort,
        private val mArticleId: Long) : BaseInteractor(mOutputPort) {

    init {
        updateProcessCount()
        GetArticleFromDbTask.execute(mArticleId, GetArticleFromDbListener())
    }

    private var mArticleUrl: String? = null

    interface IOutputPort : IInteractorOutputPort {
        fun initArticle(rss: ViewRss, article: ViewArticle)

        fun openOriginal(url: Uri)

        fun insertImage(imageBitmap: Bitmap)

        fun close()
    }

    fun onOpenOriginal() {
        mArticleUrl?.uri.let {
            when (it) {
                null -> mOutputPort.showMessage(R.string.toast_invalid_url)
                else -> mOutputPort.openOriginal(it)
            }
        }
    }

    private inner class GetArticleFromDbListener : GetArticleFromDbTask.IEventListener {
        override fun onSuccess(rss: ViewRss, article: ViewArticle) {
            mArticleUrl = article.originUrl
            mOutputPort.initArticle(rss, article)
            GetArticleImageTask.execute(mArticleId, GetImageFromNetListener(), ArticleImageProps.newInstance)
        }

        override fun onFail() {
            mOutputPort.showMessage(R.string.article_error_load)
            mOutputPort.close()
        }
    }

    private inner class GetImageFromNetListener : GetArticleImageTask.IEventListener {
        override fun onPostExecute(result: Bitmap?) {
            updateProcessCount(false)
            mOutputPort.insertImage(result ?: return)
        }
    }
}