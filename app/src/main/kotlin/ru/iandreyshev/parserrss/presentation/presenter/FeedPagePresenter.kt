package ru.iandreyshev.parserrss.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.factory.useCase.IUseCaseFactory

import ru.iandreyshev.parserrss.interactor.FeedPageInteractor
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.useCase.LoadArticlesFirstTimeUseCase
import ru.iandreyshev.parserrss.models.useCase.UpdateRssUseCase
import ru.iandreyshev.parserrss.models.util.ProcessCounter
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast
import ru.iandreyshev.parserrss.presentation.view.IFeedPageView

@InjectViewState
class FeedPagePresenter(useCaseFactory: IUseCaseFactory, rss: ViewRss) : MvpPresenter<IFeedPageView>() {

    private val mProcessCounter = ProcessCounter(this::onChangeProcessCount)

    val interactor = FeedPageInteractor(useCaseFactory, FeedPageUseCaseListener(), rss)

    private inner class FeedPageUseCaseListener : UpdateRssUseCase.IListener,
            LoadArticlesFirstTimeUseCase.IListener {

        override fun invalidUrl() {
            toast(R.string.toast_invalid_url)
        }

        override fun connectionError(requestResult: IHttpRequestResult) {
            when (requestResult.state) {
                HttpRequestHandler.State.PERMISSION_DENIED -> viewState.openInternetPermissionDialog()
                else -> toast(R.string.toast_bad_connection)
            }
        }

        override fun parseError() {
            toast(R.string.toast_invalid_rss_format)
        }

        override fun rssNotExist() {
            toast(R.string.toast_rss_not_exist)
        }

        override fun updateSuccess(articles: MutableList<ViewArticle>) {
            viewState.setArticles(articles)
            viewState.openEmptyContentMessage(articles.isEmpty())
            viewState.updateImages()
        }

        override fun insertArticlesFirstTime(articles: MutableList<ViewArticle>) {
            updateSuccess(articles)
        }

        override fun processStart() = mProcessCounter.add()

        override fun processEnd() = mProcessCounter.remove()
    }

    private fun onChangeProcessCount(newCount: Int) {
        viewState.startUpdate(newCount > 0)
    }
}
