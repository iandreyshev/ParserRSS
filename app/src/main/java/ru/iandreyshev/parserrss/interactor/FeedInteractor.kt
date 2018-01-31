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

class FeedInteractor(private val output: IOutput) : BaseInteractor(output) {

    companion object {
        private const val MAX_RSS_COUNT = 5
    }

    private var rssCount: Int = 0

    init {
        output.onChangeRssCount(rssCount)
    }

    interface IOutput : IInteractorOutput {
        fun removeRss(rss: ViewRss)

        fun insertRss(rss: ViewRss)

        fun openRssInfo(rss: ViewRss)

        fun onChangeRssCount(newCount: Int)
    }

    fun insertRss(url: String) {
        when (rssCount >= MAX_RSS_COUNT) {
            true -> output.showMessage(R.string.feed_is_full)
            else -> {
                updateProcessCount()
                InsertNewRssTask.execute(InsertRssFromNetListener(), url, FilterByDate.newInstance())
            }
        }
    }

    fun deleteRss(rss: ViewRss?) {
        when (rss) {
            null -> output.showMessage(R.string.toast_error_deleting_from_db)
            else -> {
                updateProcessCount()
                DeleteRssFromDbTask.execute(DeletingRssListener(), rss)
            }
        }
    }

    fun openRssInfo(rss: ViewRss?) {
        when (rss) {
            null -> output.showMessage(R.string.toast_rss_not_exist)
            else -> output.openRssInfo(rss)
        }
    }

    fun loadFromDatabase() {
        updateProcessCount()
        GetAllRssFromDbTask.execute(LoadFromDatabaseListener(), FilterByDate.newInstance())
    }

    private fun updateRssCount(isAddRss: Boolean = true) {
        rssCount += if (isAddRss) 1 else -1
        output.onChangeRssCount(rssCount)
    }

    private inner class DeletingRssListener : ITaskListener<ViewRss, Any, ViewRss> {
        override fun onPostExecute(result: ViewRss?) {
            if (result == null) {
                output.showMessage(R.string.toast_error_deleting_from_db)
            } else {
                updateProcessCount(false)
                updateRssCount(false)
                output.removeRss(result)
            }
        }
    }

    private inner class InsertRssFromNetListener : InsertNewRssTask.IEventListener {
        override fun onPostExecute(result: Rss?) = updateProcessCount(false)

        override fun onInvalidUrl() = output.showMessage(R.string.toast_invalid_url)

        override fun onParserError() = output.showMessage(R.string.toast_invalid_rss_format)

        override fun onNetError(requestResult: IHttpRequestResult) {
            output.showMessage(when (requestResult.state) {
                HttpRequestHandler.State.BadConnection -> R.string.toast_bad_connection
                HttpRequestHandler.State.PermissionDenied -> R.string.toast_internet_permission_denied
                else -> return
            })
        }

        override fun onRssAlreadyExist() = output.showMessage(R.string.toast_rss_already_exist)

        override fun onDatabaseError() = output.showMessage(R.string.toast_error_saving_to_db)

        override fun onSuccess(rss: ViewRss) {
            output.insertRss(rss)
            updateRssCount()
        }
    }

    private inner class LoadFromDatabaseListener : GetAllRssFromDbTask.IEventListener {
        override fun onPostExecute(result: Any?) = updateProcessCount(false)

        override fun onLoad(rss: ViewRss) {
            output.insertRss(rss)
            updateRssCount()
        }
    }
}
