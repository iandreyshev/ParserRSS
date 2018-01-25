package ru.iandreyshev.parserrss.presentation.presenter

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.async.DeleteRssFromDbTask
import ru.iandreyshev.parserrss.models.async.GetAllRssFromDbTask
import ru.iandreyshev.parserrss.models.async.InsertNewRssTask
import ru.iandreyshev.parserrss.models.filters.FilterByDate
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult
import ru.iandreyshev.parserrss.presentation.view.IFeedView

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast

@InjectViewState
class FeedPresenter : MvpPresenter<IFeedView>() {
    companion object {
        private const val MAX_RSS_COUNT = 6
    }

    private var progressBarUserCount: Int = 0
    private var rssCount: Int = 0

    fun onInsertRss(url: String) {
        if (rssCount >= MAX_RSS_COUNT) {
            toast(R.string.feed_is_full)

            return
        }

        InsertNewRssTask.execute(InsertRssFromNetListener(), url, FilterByDate.newInstance())
    }

    fun onDeleteRss(rss: ViewRss?) {
        when (rss) {
            null -> viewState.showShortToast(App.getStr(R.string.toast_error_deleting_from_db))
            else -> DeleteRssFromDbTask.execute(DeletingRssListener(), rss)
        }
    }

    fun openArticle(articleId: Long) {
        viewState.openArticle(articleId)
    }

    fun openRssInfo(rss: ViewRss?) {
        when (rss) {
            null -> toast(R.string.toast_rss_not_exist)
            else -> viewState.openRssInfo(rss)
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setToolbarScrollable(rssCount > 0)
        GetAllRssFromDbTask.execute(LoadFromDatabaseListener(), FilterByDate.newInstance())
    }

    private fun startProgressBar(isStart: Boolean) {
        progressBarUserCount += if (isStart) 1 else -1
        viewState.startProgressBar(progressBarUserCount > 0)
    }

    private fun insertRss(rss: ViewRss) {
        ++rssCount
        viewState.insertRss(rss)
        viewState.setToolbarScrollable(rssCount > 0)
    }

    private fun removeRss(rss: ViewRss) {
        --rssCount
        viewState.removeRss(rss)
        viewState.setToolbarScrollable(rssCount > 0)
    }

    private inner class DeletingRssListener : DeleteRssFromDbTask.IEventListener {
        override fun onPostExecute(result: ViewRss?) {
            removeRss(result ?: return)
        }

        override fun onFail(rss: ViewRss) = toast(R.string.toast_error_deleting_from_db)
    }

    private inner class InsertRssFromNetListener : InsertNewRssTask.IEventListener {
        override fun onPreExecute() = startProgressBar(true)

        override fun onPostExecute(result: Rss?) = startProgressBar(false)

        override fun onInvalidUrl() = toast(R.string.toast_invalid_url)

        override fun onParserError() = toast(R.string.toast_invalid_rss_format)

        override fun onNetError(requestResult: IHttpRequestResult) {
            val message = when (requestResult.state) {
                HttpRequestHandler.State.BadConnection -> App.getStr(R.string.toast_bad_connection)
                HttpRequestHandler.State.PermissionDenied -> App.getStr(R.string.toast_internet_permission_denied)
                else -> null
            }

            viewState.showShortToast(message ?: return)
        }

        override fun onRssAlreadyExist() = toast(R.string.toast_rss_already_exist)

        override fun onDatabaseError() = toast(R.string.toast_error_saving_to_db)

        override fun onSuccess(rss: ViewRss) = insertRss(rss)
    }

    private inner class LoadFromDatabaseListener : GetAllRssFromDbTask.IEventListener {
        override fun onPreExecute() = startProgressBar(true)

        override fun onPostExecute(result: Any?) = startProgressBar(false)

        override fun onLoad(rss: ViewRss) = insertRss(rss)
    }
}
