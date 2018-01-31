package ru.iandreyshev.parserrss.interactor

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.async.UpdateRssFromNetTask
import ru.iandreyshev.parserrss.models.filters.FilterByDate
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

class FeedPageInteractor(private val outputPort: IOutputPort) : BaseInteractor(outputPort) {

    var rss: ViewRss? = null
        set(value) {
            field = value
            outputPort.setArticles(field?.articles ?: return)
        }

    interface IOutputPort : IInteractorOutputPort {
        fun setArticles(articles: List<ViewArticle>)
    }

    fun onUpdate() {
        val url = rss?.url
        when (url) {
            null -> outputPort.showMessage(R.string.toast_invalid_url)
            else -> {
                updateProcessCount()
                UpdateRssFromNetTask.execute(UpdateFromNetListener(), url, FilterByDate.newInstance())
            }
        }
    }

    private inner class UpdateFromNetListener : UpdateRssFromNetTask.IEventListener {
        override fun onPostExecute(result: Rss?) = updateProcessCount(false)

        override fun onRssNotExist() = outputPort.showMessage(R.string.toast_rss_not_exist)

        override fun onDatabaseError() = outputPort.showMessage(R.string.toast_error_saving_to_db)

        override fun onInvalidUrl() = outputPort.showMessage(R.string.toast_invalid_url)

        override fun onParserError() = outputPort.showMessage(R.string.toast_invalid_rss_format)

        override fun onNetError(requestResult: IHttpRequestResult) {
            outputPort.showMessage(when (requestResult.state) {
                HttpRequestHandler.State.PermissionDenied -> R.string.toast_internet_permission_denied
                else -> R.string.toast_bad_connection
            })
        }

        override fun onSuccess(articles: List<ViewArticle>) = outputPort.setArticles(articles)
    }
}