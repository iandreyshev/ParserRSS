package ru.iandreyshev.parserrss.interactor

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.async.GetArticleFromDbTask
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss

class ArticleInteractor(private val output: IOutput) : BaseInteractor(output) {

    private var articleUrl: String? = null

    interface IOutput : IInteractorOutput {
        fun initArticle(rss: ViewRss, article: ViewArticle)

        fun openOriginal(url: String?)

        fun close()
    }

    fun initArticle(articleId: Long) {
        GetArticleFromDbTask.execute(articleId, GetArticleFromDbListener())
    }

    fun openOriginal() = output.openOriginal(articleUrl)

    private inner class GetArticleFromDbListener : GetArticleFromDbTask.IEventListener {
        override fun onSuccess(rss: ViewRss, article: ViewArticle) {
            articleUrl = article.originUrl
            output.initArticle(rss, article)
        }

        override fun onFail() {
            output.showMessage(R.string.article_error_load)
            output.close()
        }
    }
}