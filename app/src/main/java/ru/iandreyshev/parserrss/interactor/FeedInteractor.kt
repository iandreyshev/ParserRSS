package ru.iandreyshev.parserrss.interactor

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.async.DeleteRssFromDbTask
import ru.iandreyshev.parserrss.models.async.GetAllRssFromDbTask
import ru.iandreyshev.parserrss.models.async.ITaskListener
import ru.iandreyshev.parserrss.models.async.InsertNewRssTask
import ru.iandreyshev.parserrss.models.filters.FilterByDate
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

class FeedInteractor(private val outputPort: IOutputPort) : BaseInteractor(outputPort) {

    companion object {
        private const val MAX_RSS_COUNT = 5
    }

    private var rssCount: Int = 0

    init {
        outputPort.onChangeRssCount(rssCount)
    }

    interface IOutputPort : IInteractorOutputPort {
        fun removeRss(rss: ViewRss)

        fun insertRss(rss: ViewRss)

        fun openRssInfo(rss: ViewRss)

        fun onChangeRssCount(newCount: Int)

        fun openArticle(articleId: Long)
    }

    fun onInsertRss(url: String) {
        when (rssCount >= MAX_RSS_COUNT) {
            true -> outputPort.showMessage(R.string.feed_is_full)
            else -> {
                updateProcessCount()
                InsertNewRssTask.execute(InsertRssFromNetListener(), url, FilterByDate.newInstance())
            }
        }
    }

    fun onDeleteRss(rss: ViewRss?) {
        when (rss) {
            null -> outputPort.showMessage(R.string.toast_error_deleting_from_db)
            else -> {
                updateProcessCount()
                DeleteRssFromDbTask.execute(DeletingRssListener(), rss)
            }
        }
    }

    fun onOpenRssInfo(rss: ViewRss?) {
        when (rss) {
            null -> outputPort.showMessage(R.string.toast_rss_not_exist)
            else -> outputPort.openRssInfo(rss)
        }
    }

    fun onLoadFromDatabase() {
        updateProcessCount()
        GetAllRssFromDbTask.execute(LoadFromDatabaseListener(), FilterByDate.newInstance())
    }

    fun onOpenArticle(articleId: Long) {
        outputPort.openArticle(articleId)
    }

    private fun updateRssCount(isAddRss: Boolean = true) {
        rssCount += if (isAddRss) 1 else -1
        outputPort.onChangeRssCount(rssCount)
    }

    private inner class DeletingRssListener : ITaskListener<ViewRss, Any, ViewRss> {
        override fun onPostExecute(result: ViewRss?) {
            if (result == null) {
                outputPort.showMessage(R.string.toast_error_deleting_from_db)
            } else {
                updateProcessCount(false)
                updateRssCount(false)
                outputPort.removeRss(result)
            }
        }
    }

    private inner class InsertRssFromNetListener : InsertNewRssTask.IEventListener {
        override fun onPostExecute(result: Rss?) = updateProcessCount(false)

        override fun onInvalidUrl() = outputPort.showMessage(R.string.toast_invalid_url)

        override fun onParserError() = outputPort.showMessage(R.string.toast_invalid_rss_format)

        override fun onNetError(requestResult: IHttpRequestResult) {
            outputPort.showMessage(when (requestResult.state) {
                HttpRequestHandler.State.BadConnection -> R.string.toast_bad_connection
                HttpRequestHandler.State.PermissionDenied -> R.string.toast_internet_permission_denied
                else -> return
            })
        }

        override fun onRssAlreadyExist() = outputPort.showMessage(R.string.toast_rss_already_exist)

        override fun onDatabaseError() = outputPort.showMessage(R.string.toast_error_saving_to_db)

        override fun onSuccess(rss: ViewRss) {
            outputPort.insertRss(rss)
            updateRssCount()
        }
    }

    private inner class LoadFromDatabaseListener : GetAllRssFromDbTask.IEventListener {
        override fun onPostExecute(result: Any?) = updateProcessCount(false)

        override fun onLoad(rss: ViewRss) {
            outputPort.insertRss(rss)
            updateRssCount()
        }
    }
}
