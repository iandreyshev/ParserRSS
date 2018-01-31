package ru.iandreyshev.parserrss.interactor

import android.net.Uri
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.async.GetArticleFromDbTask
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss

class ArticleInteractor(private val outputPort: IOutputPort) : BaseInteractor(outputPort) {

    private var articleUrl: String? = null

    interface IOutputPort : IInteractorOutputPort {
        fun initArticle(rss: ViewRss, article: ViewArticle)

        fun openOriginal(url: Uri)

        fun close()
    }

    fun initArticle(articleId: Long) {
        GetArticleFromDbTask.execute(articleId, GetArticleFromDbListener())
    }

    fun onOpenOriginal() {
        try {
            val uri = Uri.parse(articleUrl)
            if (uri != null) {
                outputPort.openOriginal(uri)
            } else {
                outputPort.showMessage(R.string.toast_invalid_url)
            }
        } catch (ex: Exception) {
            outputPort.showMessage(R.string.toast_invalid_url)
        }
    }

    private inner class GetArticleFromDbListener : GetArticleFromDbTask.IEventListener {
        override fun onSuccess(rss: ViewRss, article: ViewArticle) {
            articleUrl = article.originUrl
            outputPort.initArticle(rss, article)
        }

        override fun onFail() {
            outputPort.showMessage(R.string.article_error_load)
            outputPort.close()
        }
    }
}