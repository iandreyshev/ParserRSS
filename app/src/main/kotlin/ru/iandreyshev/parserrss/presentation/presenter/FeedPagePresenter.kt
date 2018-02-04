package ru.iandreyshev.parserrss.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.factory.UseCaseFactory

import ru.iandreyshev.parserrss.interactor.FeedPageInteractor
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.util.ProcessCounter
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast
import ru.iandreyshev.parserrss.presentation.view.IFeedPageView

@InjectViewState
class FeedPagePresenter(rss: ViewRss) : MvpPresenter<IFeedPageView>() {

    private val mProcessCounter = ProcessCounter(this::onChangeProcessCount)

    val interactor = FeedPageInteractor(UseCaseFactory, FeedPageUseCaseListener(), rss)

    private inner class FeedPageUseCaseListener : FeedPageInteractor.IListener {
        override fun onInvalidUrl() {
            toast(R.string.toast_invalid_url)
        }

        override fun onNetError(requestResult: IHttpRequestResult) {
            when (requestResult.state) {
                HttpRequestHandler.State.PERMISSION_DENIED -> viewState.openInternetPermissionDialog()
                else -> toast(R.string.toast_bad_connection)
            }
        }

        override fun onParserError() {
            toast(R.string.toast_invalid_rss_format)
        }

        override fun onRssNotExist() {
            toast(R.string.toast_rss_not_exist)
        }

        override fun onDatabaseError() {
            toast(R.string.toast_error_saving_to_db)
        }

        override fun onUpdateSuccess(articles: MutableList<ViewArticle>) {
            viewState.setArticles(articles)
            viewState.openEmptyContentMessage(articles.isEmpty())
            viewState.updateImages()
        }

        override fun insertArticlesFirstTime(articles: MutableList<ViewArticle>) {
            onUpdateSuccess(articles)
        }

        override fun processStart() = mProcessCounter.startProcess()

        override fun processEnd() = mProcessCounter.endProcess()
    }

    private fun onChangeProcessCount(newCount: Int) {
        viewState.startUpdate(newCount > 0)
    }
}
