package ru.iandreyshev.parserrss.interactor

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.async.DeleteRssFromDbTask
import ru.iandreyshev.parserrss.models.async.GetAllRssFromDbTask
import ru.iandreyshev.parserrss.models.async.ITaskListener
import ru.iandreyshev.parserrss.models.async.InsertNewRssTask
import ru.iandreyshev.parserrss.models.filters.ArticlesFilterByDate
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

class FeedInteractor(private val mOutputPort: IOutputPort) : BaseInteractor(mOutputPort) {

    companion object {
        private const val MAX_RSS_COUNT = 5
    }

    private var mRssCount: Int = 0

    init {
        updateRssCount()
    }

    interface IOutputPort : IInteractorOutputPort {
        fun removeRss(rss: ViewRss)

        fun insertRss(rss: ViewRss)

        fun openRssInfo(rss: ViewRss)

        fun onChangeRssCount(newCount: Int, isFull: Boolean)

        fun openArticle(articleId: Long)

        fun openInternetPermissionDialog()
    }

    fun onInsertRss(url: String) {
        when (mRssCount >= MAX_RSS_COUNT) {
            true -> mOutputPort.showMessage(R.string.feed_is_full)
            else -> {
                updateProcessCount()
                InsertNewRssTask.execute(InsertRssFromNetListener(), url, ArticlesFilterByDate)
            }
        }
    }

    fun onDeleteRss(rss: ViewRss?) {
        when (rss) {
            null -> mOutputPort.showMessage(R.string.toast_error_deleting_from_db)
            else -> {
                updateProcessCount()
                DeleteRssFromDbTask.execute(DeletingRssListener(), rss)
            }
        }
    }

    fun onOpenRssInfo(rss: ViewRss?) {
        when (rss) {
            null -> mOutputPort.showMessage(R.string.toast_rss_not_exist)
            else -> mOutputPort.openRssInfo(rss)
        }
    }

    fun onLoadFromDatabase() {
        updateProcessCount()
        GetAllRssFromDbTask.execute(LoadFromDatabaseListener(), ArticlesFilterByDate)
    }

    fun onOpenArticle(articleId: Long) = mOutputPort.openArticle(articleId)

    private fun updateRssCount(isAddRss: Boolean? = null) {
        isAddRss?.let { mRssCount += if (it) 1 else -1 }
        mOutputPort.onChangeRssCount(mRssCount, mRssCount >= MAX_RSS_COUNT)
    }

    private inner class DeletingRssListener : ITaskListener<ViewRss, Any, ViewRss> {
        override fun onPostExecute(result: ViewRss?) {
            if (result == null) {
                mOutputPort.showMessage(R.string.toast_error_deleting_from_db)
            } else {
                updateProcessCount(false)
                updateRssCount(false)
                mOutputPort.removeRss(result)
            }
        }
    }

    private inner class InsertRssFromNetListener : InsertNewRssTask.IEventListener {
        override fun onPostExecute(result: Rss?) = updateProcessCount(false)

        override fun onInvalidUrl() = mOutputPort.showMessage(R.string.toast_invalid_url)

        override fun onParserError() = mOutputPort.showMessage(R.string.toast_invalid_rss_format)

        override fun onNetError(requestResult: IHttpRequestResult) {
            mOutputPort.showMessage(when (requestResult.state) {
                HttpRequestHandler.State.PERMISSION_DENIED -> {
                    mOutputPort.openInternetPermissionDialog()
                    R.string.toast_internet_permission_denied
                }
                else -> R.string.toast_bad_connection
            })
        }

        override fun onRssAlreadyExist() = mOutputPort.showMessage(R.string.toast_rss_already_exist)

        override fun onDatabaseError() = mOutputPort.showMessage(R.string.toast_error_saving_to_db)

        override fun onSuccess(rss: ViewRss) {
            mOutputPort.insertRss(rss)
            updateRssCount(true)
        }
    }

    private inner class LoadFromDatabaseListener : GetAllRssFromDbTask.IEventListener {
        override fun onPostExecute(result: Any?) = updateProcessCount(false)

        override fun onLoad(rss: ViewRss) {
            mOutputPort.insertRss(rss)
            updateRssCount(true)
        }
    }
}
