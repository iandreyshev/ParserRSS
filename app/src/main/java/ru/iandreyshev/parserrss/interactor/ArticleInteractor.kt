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
        private val _outputPort: IOutputPort,
        private val _articleId: Long) : BaseInteractor(_outputPort) {

    init {
        updateProcessCount()
        GetArticleFromDbTask.execute(_articleId, GetArticleFromDbListener())
    }

    private var _articleUrl: String? = null

    interface IOutputPort : IInteractorOutputPort {
        fun initArticle(rss: ViewRss, article: ViewArticle)

        fun openOriginal(url: Uri)

        fun insertImage(imageBitmap: Bitmap)

        fun close()
    }

    fun onOpenOriginal() {
        _articleUrl?.uri.let {
            when (it) {
                null -> _outputPort.showMessage(R.string.toast_invalid_url)
                else -> _outputPort.openOriginal(it)
            }
        }
    }

    private inner class GetArticleFromDbListener : GetArticleFromDbTask.IEventListener {
        override fun onSuccess(rss: ViewRss, article: ViewArticle) {
            _articleUrl = article.originUrl
            _outputPort.initArticle(rss, article)
            GetArticleImageTask.execute(_articleId, GetImageFromNetListener(), ArticleImageProps.newInstance)
        }

        override fun onFail() {
            _outputPort.showMessage(R.string.article_error_load)
            _outputPort.close()
        }
    }

    private inner class GetImageFromNetListener : GetArticleImageTask.IEventListener {
        override fun onPostExecute(result: Bitmap?) {
            updateProcessCount(false)
            _outputPort.insertImage(result ?: return)
        }
    }
}