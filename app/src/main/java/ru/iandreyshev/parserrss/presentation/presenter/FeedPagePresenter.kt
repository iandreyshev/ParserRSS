package ru.iandreyshev.parserrss.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.async.UpdateRssFromNetTask
import ru.iandreyshev.parserrss.models.filters.FilterByDate
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast
import ru.iandreyshev.parserrss.presentation.view.IFeedPageView

@InjectViewState
class FeedPagePresenter(private val rss: ViewRss) : MvpPresenter<IFeedPageView>() {
    fun onUpdate() {
        rss.url.let {
            when (it) {
                null -> toast(R.string.toast_invalid_url)
                else -> UpdateRssFromNetTask.execute(UpdateFromNetListener(), it, FilterByDate.newInstance())
            }
        }
    }

    override fun onFirstViewAttach() = viewState.setArticles(rss.articles)

    private inner class UpdateFromNetListener : UpdateRssFromNetTask.IEventListener {
        override fun onPreExecute() = viewState.startUpdate(true)

        override fun onPostExecute(result: Rss?) = viewState.startUpdate(false)

        override fun onRssNotExist() = toast(R.string.toast_rss_not_exist)

        override fun onDatabaseError() = toast(R.string.toast_error_saving_to_db)

        override fun onInvalidUrl() = toast(R.string.toast_invalid_url)

        override fun onParserError() = toast(R.string.toast_invalid_rss_format)

        override fun onNetError(requestResult: IHttpRequestResult) {
            toast(when (requestResult.state) {
                HttpRequestHandler.State.PermissionDenied -> R.string.toast_internet_permission_denied
                else -> R.string.toast_bad_connection
            })
        }

        override fun onSuccess(articles: List<ViewArticle>) {
            viewState.setArticles(articles)
            viewState.updateImages(true)
        }
    }
}
