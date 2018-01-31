package ru.iandreyshev.parserrss.interactor

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.async.UpdateRssFromNetTask
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

class FeedPageInteractor(private val output: IOutput) : BaseInteractor(output) {

    interface IOutput : IInteractorOutput {
        fun setArticles(articles: List<ViewArticle>)
    }

    fun update(rss: ViewRss, filter: IArticlesFilter) {
        val url = rss.url
        when (url) {
            null -> output.showMessage(R.string.toast_invalid_url)
            else -> {
                updateProcessCount()
                UpdateRssFromNetTask.execute(UpdateFromNetListener(), url, filter)
            }
        }
    }

    private inner class UpdateFromNetListener : UpdateRssFromNetTask.IEventListener {
        override fun onPostExecute(result: Rss?) = updateProcessCount(false)

        override fun onRssNotExist() = output.showMessage(R.string.toast_rss_not_exist)

        override fun onDatabaseError() = output.showMessage(R.string.toast_error_saving_to_db)

        override fun onInvalidUrl() = output.showMessage(R.string.toast_invalid_url)

        override fun onParserError() = output.showMessage(R.string.toast_invalid_rss_format)

        override fun onNetError(requestResult: IHttpRequestResult) {
            output.showMessage(when (requestResult.state) {
                HttpRequestHandler.State.PermissionDenied -> R.string.toast_internet_permission_denied
                else -> R.string.toast_bad_connection
            })
        }

        override fun onSuccess(articles: List<ViewArticle>) = output.setArticles(articles)
    }
}